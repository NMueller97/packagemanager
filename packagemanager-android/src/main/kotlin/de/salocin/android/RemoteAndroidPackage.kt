package de.salocin.android

import de.salocin.android.adb.AdbCommands
import java.nio.file.Path

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
    override val downloadable = true

    override suspend fun refreshInstallLocation() {
        paths = AdbCommands.PackageManager.pathCommand(name).execute()
    }
}
