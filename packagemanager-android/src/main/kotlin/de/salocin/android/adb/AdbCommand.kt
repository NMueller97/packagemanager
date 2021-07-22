package de.salocin.android.adb

import de.salocin.parser.LineOutputParser
import de.salocin.task.Task
import de.salocin.task.TaskFailedException
import javafx.application.Platform
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import java.io.InputStreamReader
import java.io.PrintStream

class AdbCommand<T>(
    private val arguments: List<String>,
    private val outputParser: LineOutputParser<T>,
    private val commandPrinter: PrintStream?,
    private val stdoutPrinter: PrintStream?,
    private val stderrPrinter: PrintStream?
) {

    @Throws(TaskFailedException::class)
    fun executeAwait(): ObservableList<T> = with(FXCollections.observableArrayList<T>()) {
        executeAwait {
            add(it)
        }

        this
    }

    @Throws(TaskFailedException::class)
    fun executeAwait(parsedLineCallback: (T) -> Unit) = Task.runAwait(arguments.joinToString(" ")) {
        val process = ProcessBuilder(arguments).start()
        commandPrinter?.println(arguments.joinToString(" "))

        InputStreamReader(process.inputStream).useLines { lines ->
            lines.forEach { line ->
                stdoutPrinter?.println(line)
                outputParser.parseLine(line)?.let { parsedLine ->
                    parsedLineCallback(parsedLine)
                }
            }
        }

        InputStreamReader(process.errorStream).useLines { lines ->
            lines.forEach { line ->
                stderrPrinter?.println(line)
            }
        }
    }

    fun <V> executeAsync(thenRun: (ObservableList<T>) -> V): Task<V> = Task.runAsync {
        executeAwait()
    }.thenRun(thenRun)

    fun executeAsync(observableList: ObservableList<T>): Task<ObservableList<T>> =
        executeAsync(observableList) { it }

    fun <V> executeAsync(
        observableList: ObservableList<V>,
        mapper: (T) -> V
    ): Task<ObservableList<V>> {
        observableList.clear()

        return Task.runAsync {
            executeAwait { value ->
                Platform.runLater {
                    observableList.add(mapper(value))
                }
            }
            observableList
        }
    }
}
