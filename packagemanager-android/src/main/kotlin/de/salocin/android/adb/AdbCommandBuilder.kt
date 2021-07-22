package de.salocin.android.adb

import de.salocin.parser.LineOutputParser
import de.salocin.parser.PlainOutputParser
import java.io.PrintStream

class AdbCommandBuilder private constructor(private val subcommands: ArrayDeque<String>) {

    constructor(subcommand: String) : this(ArrayDeque(listOf(BASE_COMMAND, subcommand)))

    private var commandPrinter: PrintStream? = null
    private var stdoutPrinter: PrintStream? = null
    private var stderrPrinter: PrintStream? = null

    fun withCommandPrinter(printStream: PrintStream): AdbCommandBuilder {
        commandPrinter = printStream
        return this
    }

    fun withStdoutPrinter(printStream: PrintStream): AdbCommandBuilder {
        stdoutPrinter = printStream
        return this
    }

    fun withStderrPrinter(printStream: PrintStream): AdbCommandBuilder {
        stderrPrinter = printStream
        return this
    }

    fun resolve(subcommand: String): AdbCommandBuilder {
        val newArrayDeque = ArrayDeque(subcommands)
        newArrayDeque.addLast(subcommand)
        return AdbCommandBuilder(newArrayDeque)
    }

    fun build(): AdbCommand<String> = build(PlainOutputParser)

    fun <T> build(outputParser: LineOutputParser<T>): AdbCommand<T> =
        AdbCommand(subcommands, outputParser, commandPrinter, stdoutPrinter, stderrPrinter)

    companion object {

        private const val BASE_COMMAND = "adb"
    }
}
