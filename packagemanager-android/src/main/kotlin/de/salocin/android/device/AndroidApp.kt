package de.salocin.android.device

import de.salocin.android.adb.Adb
import de.salocin.android.io.ZipFile
import de.salocin.android.io.createTemporaryDirectory
import de.salocin.android.io.filename
import de.salocin.android.io.zip
import de.salocin.packagemanager.ProgressObserver
import de.salocin.packagemanager.device.App
import de.salocin.packagemanager.device.DevicePath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.copyTo
import kotlin.io.path.listDirectoryEntries

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
        observer?.notifyMessageChange("Refreshing installation paths")
        refreshPaths(observer)
        observer?.notifyProgressChange(0)
        observer?.notifyMaxProgressChange(paths.size)

        createTemporaryDirectory { temporaryDirectory ->
            if (paths.size == 1) {
                val source = downloadSingleApk(observer, paths[0], temporaryDirectory, 0)

                withContext(Dispatchers.IO) {
                    source.copyTo(destination, StandardCopyOption.REPLACE_EXISTING)
                }
            } else {
                paths.forEachIndexed { index, devicePath ->
                    downloadSingleApk(observer, devicePath, temporaryDirectory, index)
                }

                withContext(Dispatchers.IO) {
                    temporaryDirectory.listDirectoryEntries().zip(ZipFile(destination))
                }
            }
        }
    }

    private suspend fun downloadSingleApk(
        observer: ProgressObserver?,
        devicePath: DevicePath,
        temporaryDirectory: Path,
        index: Int
    ): Path {
        observer?.notifyMessageChange("Downloading ${devicePath.path.filename}")
        val path = temporaryDirectory.resolve(devicePath.path.filename)
        Adb.pull(device, devicePath, path)
        observer?.notifyProgressChange(index + 1)
        return path
    }
}
