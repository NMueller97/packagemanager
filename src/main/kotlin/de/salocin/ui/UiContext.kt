package de.salocin.ui

import de.salocin.api.AndroidPackage
import de.salocin.api.AndroidPackageType
import de.salocin.util.nullable
import de.salocin.util.selectList
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import tornadofx.objectProperty
import tornadofx.observableListOf
import tornadofx.select
import java.nio.file.Path

object UiContext {

    val selectedPackageItemProperty = objectProperty<TreeItem<AndroidPackage>?>()

    val selectedPackageProperty: ObservableValue<AndroidPackage?> =
        selectedPackageItemProperty.select { it?.valueProperty() ?: objectProperty() }

    val selectedPackageInstallLocationProperty: ObservableList<Path> =
        selectedPackageProperty.selectList { it?.installLocation ?: observableListOf() }

    val selectedPackageTypeProperty: ObservableValue<AndroidPackageType?> =
        selectedPackageProperty.select {
            it?.typeProperty?.nullable() ?: objectProperty()
        }
}
