package de.salocin.packagemanager.io

import java.io.InputStream
import java.io.PrintStream
import java.nio.ByteBuffer

private const val BUFFER_SIZE = 8 * 1024

/**
 * An output from a system process from the [inputStream], which might be piped to a print stream with [outputPipe].
 * The output lines are parsed according to the supplied [outputParser], if available.
 */
class SystemProcessOutput<T>(
    private val inputStream: InputStream,
    private val outputParser: OutputParser<T>?,
    private val outputPipe: PrintStream?,
    private val encoding: OutputEncoding = OutputEncoding(),
) {
    private val buffer = ByteBuffer.allocate(BUFFER_SIZE)

    /**
     * Tries to read the next available bytes, without blocking the current thread.
     * It will not wait for more bytes if no bytes are available.
     */
    fun readAvailable(): List<T> {
        val list = mutableListOf<T>()

        do {
            val moreBytesAvailable = readAvailableBytes(list)
        } while (moreBytesAvailable)

        return list
    }

    private fun readAvailableBytes(list: MutableList<T>): Boolean {
        val available = inputStream.available()

        if (available <= 0) {
            return false
        }

        val bytes = inputStream.readNBytes(maxOf(available, BUFFER_SIZE))
        list.addAll(parseBytes(bytes))
        return true
    }

    /**
     * Reads all remaining bytes from the process output. This method will block until an end of stream is reached.
     */
    fun readRemaining(): List<T> {
        return parseBytes(inputStream.readAllBytes())
    }

    private fun parseBytes(bytes: ByteArray): List<T> {
        val lines = mutableListOf<T>()

        for (byte in bytes) {
            outputPipe?.print(byte.toInt().toChar())

            if (byte.toInt() == '\n'.code) {
                parseLine()?.let { lines.add(it) }
            } else {
                buffer.put(byte)
            }
        }

        return lines
    }

    private fun parseLine(): T? = outputParser?.let { parser ->
        val bytes = ByteArray(buffer.position())
        val endIndex = buffer.position() - encoding.lineEnding.length + 1

        for (i in 0 until endIndex) {
            bytes[i] = buffer[i]
        }

        val line = String(bytes, encoding.charset).replace(0.toChar().toString(), "")
        val parsedLine = parser.parseLine(line)
        buffer.position(0)
        return parsedLine
    }
}
