package de.salocin.android.io

import java.io.IOException
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream
import java.util.zip.ZipOutputStream
import kotlin.io.path.*

@JvmInline
value class ZipFile(val path: Path) {

    @Throws(IOException::class)
    fun outputStream(): ZipOutputStream {
        return ZipOutputStream(path.outputStream())
    }

    @Throws(IOException::class)
    fun inputStream(): ZipInputStream {
        return ZipInputStream(path.inputStream())
    }
}

@Throws(IOException::class)
fun List<Path>.zip(target: ZipFile) {
    target.path.deleteIfExists()
    target.path.createFile()

    target.outputStream().use { output ->
        forEach { file ->
            output.putNextEntry(ZipEntry(file.name))
            file.inputStream().copyTo(output)
        }
    }
}

@Throws(IOException::class)
fun ZipFile.unzip(target: Path) {
    inputStream().use { input ->
        input::getNextEntry.whileNotNull { entry ->
            val file = target.resolve(entry.name)
            file.deleteIfExists()
            file.createFile()
            input.copyTo(file.outputStream())
            input.closeEntry()
        }
    }
}

private typealias NullableProducer<T> = () -> T?

private inline fun <T> NullableProducer<T>.whileNotNull(consumer: (T) -> Unit) {
    var value: T? = this()

    while (value != null) {
        consumer(value)
        value = this()
    }
}
