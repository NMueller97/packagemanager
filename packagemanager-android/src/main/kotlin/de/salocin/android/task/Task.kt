package de.salocin.task

import javafx.application.Platform
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

class Task<T>(
    private val name: String? = null,
    initialTaskObserver: TaskObserver<T>? = null
) {

    private val lock = ReentrantLock()
    private val notRunningCondition = lock.newCondition()

    var taskObserver: TaskObserver<T>? = initialTaskObserver
        get() = lock.withLock { field }
        set(value) {
            lock.withLock { field = value }
        }

    private var thenRun: ThenRun<*>? = null
    private var state = TaskState.INITIALIZED

    /**
     * Immediately completes this task with the supplied [value].
     */
    fun complete(value: T): Task<T> {
        lock.withLock {
            if (state == TaskState.COMPLETED || state == TaskState.FAILED) {
                throw IllegalStateException("Task is already finished")
            } else {
                state = TaskState.COMPLETED
                notRunningCondition.signalAll()
            }
        }

        taskObserver?.onCompleted(this, value, thenRun?.task)

        lock.withLock {
            thenRun?.apply(value)
        }

        return this
    }

    /**
     * Immediately fails this task caused by the supplied [exception].
     */
    fun fail(exception: Exception): Task<T> {
        lock.withLock {
            if (state == TaskState.COMPLETED || state == TaskState.FAILED) {
                throw IllegalStateException("Task is already finished")
            } else {
                state = TaskState.FAILED
                notRunningCondition.signalAll()
            }
        }

        taskObserver?.onFailed(this, exception)

        return this
    }

    /**
     * Awaits the task to end. Halts the thread until the state is either [TaskState.COMPLETED] or
     * [TaskState.FAILED].
     */
    @Throws(TaskFailedException::class)
    fun await() = lock.withLock {
        while (state != TaskState.COMPLETED && state != TaskState.FAILED) {
            notRunningCondition.await()
        }
    }

    /**
     * Starts this task with the supplied value given in the [computation] which will be executed
     * synchronously (executed on the current thread).
     */
    @Throws(TaskFailedException::class)
    fun await(computation: () -> T): T = start(computation)

    /**
     * Starts this task with the supplied value given in the [computation] which will be executed
     * synchronously (executed on the current thread).
     */
    @Throws(TaskFailedException::class)
    fun start(computation: () -> T): T {
        lock.withLock {
            if (state != TaskState.INITIALIZED) {
                throw IllegalStateException("Task was already started")
            } else {
                state = TaskState.RUNNING
            }
        }
        taskObserver?.onStarted(this)


        val value = try {
            computation()
        } catch (e: Exception) {
            fail(e)
            throw TaskFailedException(e)
        }

        complete(value)
        return value
    }

    /**
     * Starts this task with the supplied value given in the [computation] which will be executed
     * asynchronously.
     */
    fun startAsync(computation: () -> T): Task<T> {
        val runnable: () -> Unit = { start(computation) }
        val thread = if (name == null) Thread(runnable) else Thread(runnable, name)
        thread.isDaemon = true
        thread.start()

        return this
    }

    /**
     * Runs another task once this task has finished, receiving this task's output. The supplied
     * block is run on the main thread to interact with JavaFX.
     */
    fun <V> thenRun(block: (T) -> V): Task<V> = thenRun(block, false)

    /**
     * Asynchronously runs another task once this task has finished, receiving this task's output.
     */
    fun <V> thenRunAsync(block: (T) -> V): Task<V> = thenRun(block, true)

    private fun <V> thenRun(block: (T) -> V, async: Boolean): Task<V> {
        lock.withLock {
            if (state == TaskState.COMPLETED || state == TaskState.FAILED) {
                throw IllegalStateException("Task is already finished")
            }
        }

        val observer = Task<V>(name)
        val thenRunPair = ThenRun(block, observer, async)

        lock.withLock {
            thenRun = thenRunPair
        }

        return observer
    }

    override fun toString(): String = name ?: ""

    private inner class ThenRun<V>(
        val block: (T) -> V,
        val task: Task<V>,
        val async: Boolean
    ) {

        fun apply(value: T) {
            if (async) {
                task.startAsync { block(value) }
            } else {
                if (Platform.isFxApplicationThread()) {
                    task.complete(block(value))
                } else {
                    Platform.runLater {
                        task.complete(block(value))
                    }
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun <T> completed(value: T, name: String? = null): Task<T> = Task<T>(name).complete(value)

        @JvmStatic
        fun <T> failed(exception: Exception, name: String? = null): Task<T> =
            Task<T>(name).fail(exception)

        @JvmStatic
        @Throws(TaskFailedException::class)
        fun <T> runAwait(name: String? = null, computation: () -> T): T =
            Task<T>(name).await(computation)

        @JvmStatic
        fun <T> runAsync(name: String? = null, computation: () -> T): Task<T> =
            Task<T>(name).startAsync(computation)
    }
}
