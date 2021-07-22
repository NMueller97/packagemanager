package de.salocin.task

interface TaskObserver<T> {

    /**
     * Called once a [task] is started. This may be called on an asynchronous thread.
     */
    fun onStarted(task: Task<T>) {}

    /**
     * Called once a [task] is completed with a [value]. The supplied [thenRunTask] is another task
     * depends on the completed task and receives its output value. This may be called on an
     * asynchronous thread.
     *
     * @see [Task.thenRun]
     * @see [Task.thenRunAsync]
     */
    fun onCompleted(task: Task<T>, value: T, thenRunTask: Task<*>?) {}

    /**
     * Called once a [task] could not be completed, thus failed. The causing [exception] is supplied
     * to this method. This may be called on an asynchronous thread.
     */
    fun onFailed(task: Task<T>, exception: Exception) {}
}
