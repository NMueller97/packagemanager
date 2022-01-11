package de.salocin.android.device

import de.salocin.android.adb.Adb
import de.salocin.packagemanager.ProgressObserver
import de.salocin.packagemanager.device.App
import de.salocin.packagemanager.device.DevicePath
import java.nio.file.Path

data class AndroidApp(
    val device: AndroidDevice,
    override val name: String,
    override val type: AndroidAppType
) : App {

    override var paths: List<DevicePath> = emptyList()
        private set

    override suspend fun refreshPaths(observer: ProgressObserver?) {
        paths = Adb.packagePaths(device, name)
    }

    override suspend fun download(destination: Path, observer: ProgressObserver?) {
        TODO("Not yet implemented")
    }
}
