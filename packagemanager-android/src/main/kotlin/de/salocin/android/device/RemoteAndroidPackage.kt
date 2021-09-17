package de.salocin.android.device

import de.salocin.android.adb.AdbCommands
import de.salocin.android.io.createTemporaryDirectory
import de.salocin.android.io.zipTo
import de.salocin.android.progress.ProgressObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Path
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.moveTo
import kotlin.io.path.name

/**
 * An Android package that is installed on a remote Android [device] using a unique [name] at the
 * specified [installLocation].
 */
class RemoteAndroidPackage(
    val device: AndroidDevice,
    override val name: String,
    override val type: AndroidPackageType
) : AndroidPackage {

    override var paths = listOf<Path>()
    override val detailsAvailable = true
    override val downloadable = true

    override suspend fun refreshInstallLocation() {
        paths = AdbCommands.PackageManager.pathCommand(name).execute()
    }

    override suspend fun download(target: Path, observer: ProgressObserver?) {
        createTemporaryDirectory { directory ->
            try {
                var progress = 0
                observer?.notifyProgressChange(progress)
                observer?.notifyMaxProgressChange(paths.size)

                for (source in paths) {
                    observer?.notifyMessageChange("Downloading ${source.name}")
                    val tempTarget = directory.resolve(source.name)
                    AdbCommands.pull(source, tempTarget).execute()
                    observer?.notifyProgressChange(++progress)
                }

                observer?.notifyMessageChange("Packing downloaded files")

                coroutineScope {
                    val zipJob = launch(Dispatchers.IO) {
                        val entries = directory.listDirectoryEntries()

                        if (entries.size == 1) {
                            entries.first().moveTo(target, overwrite = true)
                        } else {
                            entries.zipTo(target)
                        }
                    }

                    zipJob.join()
                }
            } finally {
                observer?.notifyFinish()
            }
        }
    }
}
