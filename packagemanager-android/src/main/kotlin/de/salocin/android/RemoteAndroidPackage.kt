package de.salocin.api

import de.salocin.adb.AdbCommands
import de.salocin.android.AndroidPackageType
import de.salocin.device.AndroidDevice
import de.salocin.task.Task
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.objectProperty
import tornadofx.stringProperty
import java.nio.file.Path

/**
 * An Android package that is installed on a remote Android [device] using a unique [name] at the
 * specified [installLocation].
 */
class RemoteAndroidPackage(
    val device: AndroidDevice,
    name: String,
    type: AndroidPackageType
) : AndroidPackage {

    override val nameProperty = stringProperty(name)
    override val typeProperty = objectProperty(type)
    override val installLocation: ObservableList<Path> = FXCollections.observableArrayList()
    override val downloadable = true

    override fun refreshInstallLocationAsync(): Task<ObservableList<Path>> =
        AdbCommands.PackageManager.pathCommand(name).executeAsync(installLocation)
}
