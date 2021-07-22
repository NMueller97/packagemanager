package de.salocin.api

import de.salocin.android.AndroidPackageType
import de.salocin.task.Task
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import tornadofx.objectProperty
import tornadofx.stringProperty
import java.nio.file.Path

class DummyAndroidPackage(name: String) : AndroidPackage {

    override val nameProperty = stringProperty(name)
    override val typeProperty = objectProperty(AndroidPackageType.UNKNOWN)
    override val installLocation: ObservableList<Path> = FXCollections.emptyObservableList()
    override val downloadable = false

    override fun refreshInstallLocationAsync() = Task.completed(installLocation)
}
