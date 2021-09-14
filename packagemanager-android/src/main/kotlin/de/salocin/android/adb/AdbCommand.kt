package de.salocin.android.adb

import de.salocin.android.parser.LineOutputParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import java.io.InputStreamReader
import java.io.PrintStream

class AdbCommand<T>(
    private val arguments: List<String>,
    private val outputParser: LineOutputParser<T>,
    private val commandPrinter: PrintStream?,
    private val stdoutPrinter: PrintStream?,
    private val stderrPrinter: PrintStream?
) {

    suspend fun execute(): List<T> {
        return runInterruptible(Dispatchers.IO) {
            val parsedLines = mutableListOf<T>()

            commandPrinter?.println(arguments.joinToString(" "))

            val process = ProcessBuilder(arguments).start()!!

            InputStreamReader(process.inputStream).useLines { lines ->
                lines.forEach { line ->
                    stdoutPrinter?.println(line)
                    outputParser.parseLine(line)?.let { parsedLine ->
                        parsedLines += parsedLine
                    }
                }
            }

            InputStreamReader(process.errorStream).useLines { lines ->
                lines.forEach { line ->
                    stderrPrinter?.println(line)
                }
            }

            return@runInterruptible parsedLines
        }
    }
}
