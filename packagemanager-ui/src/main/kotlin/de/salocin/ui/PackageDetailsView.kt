package de.salocin.ui

import de.salocin.android.AndroidPackage
import javafx.beans.value.ObservableValue
import javafx.scene.control.TitledPane
import javafx.scene.layout.VBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.nio.file.Path

class PackageDetailsView(
    coroutineScope: CoroutineScope,
    selectedPackage: ObservableValue<AndroidPackage?>
) : CoroutineView(coroutineScope) {

    private val nameTextField = PackageDetailsTextField("Name")
    private val pathsList = PackageDetailsList<Path>("Paths")

    private val content = VBox(nameTextField.root, pathsList.root)

    override val root = TitledPane("Details", content).apply {
        isCollapsible = false
    }

    init {
        selectedPackage.addListener { _, _, newValue ->
            newValue?.let {
                if (it.detailsAvailable) {
                    refreshDetailsJob(it)
                }
            }
        }
    }

    private fun refreshDetailsJob(pack: AndroidPackage) {
        coroutineScope.launch {
            nameTextField.text = pack.name
            pathsList.items = pack.paths.observableList()
        }
    }
}
