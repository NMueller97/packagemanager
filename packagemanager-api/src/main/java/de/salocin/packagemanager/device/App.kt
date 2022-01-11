package de.salocin.packagemanager.device

import de.salocin.packagemanager.ProgressObserver
import java.nio.file.Path

interface App {

    val name: String

    val type: AppType

    val paths: List<DevicePath>

    suspend fun refreshPaths(observer: ProgressObserver? = null)

    suspend fun download(destination: Path, observer: ProgressObserver? = null)
}
