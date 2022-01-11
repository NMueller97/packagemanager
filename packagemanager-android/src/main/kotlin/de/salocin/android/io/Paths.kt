package de.salocin.android.io

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createFile
import kotlin.io.path.deleteExisting
import kotlin.io.path.deleteIfExists
import kotlin.io.path.inputStream
import kotlin.io.path.isDirectory
import kotlin.io.path.name
import kotlin.io.path.outputStream

@Throws(IOException::class)
fun Path.deleteRecursive() {
    Files.list(this).forEach { child ->
        if (child.isDirectory()) {
            child.deleteRecursive()
        } else {
            child.deleteExisting()
        }
    }

    deleteExisting()
}

suspend inline fun createTemporaryDirectory(crossinline block: suspend (TemporaryDirectory) -> Unit) {
    coroutineScope {
        val createDirectoryJob: Deferred<Path> = async(Dispatchers.IO) {
            Files.createTempDirectory(null)
        }

        val directory = TemporaryDirectory(createDirectoryJob.await())

        try {
            block(directory)
        } finally {
            withContext(NonCancellable) {
                delay(500L)

                launch(Dispatchers.IO) {
                    directory.path.deleteRecursive()
                }
            }
        }
    }
}

@JvmInline
value class PlatformPath(val path: Path)

@JvmInline
value class RemotePath(val path: Path)

@JvmInline
value class TemporaryDirectory(val path: Path)
