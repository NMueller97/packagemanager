package de.salocin.android.progress

interface ProgressObserver {

    suspend fun notifyProgressChange(progress: Int)

    suspend fun notifyMaxProgressChange(maxProgress: Int)

    suspend fun notifyMessageChange(message: String)

    suspend fun notifyFinish()
}
