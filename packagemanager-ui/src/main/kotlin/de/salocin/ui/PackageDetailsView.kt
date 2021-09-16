package de.salocin.ui

import de.salocin.android.AndroidPackage
import javafx.beans.value.ObservableValue
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TitledPane
import javafx.scene.layout.StackPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PackageDetailsView(
    coroutineScope: CoroutineScope,
    selectedPackage: ObservableValue<AndroidPackage?>
) : CoroutineView(coroutineScope) {

    private val buttons = PackageDetailsButtons()
    private val nameTextField = PackageDetailsTextField("Name")
    private val pathsList = PackageDetailsPathList("Paths")

    private val content = VBox(buttons.root, nameTextField.root, pathsList.root).apply { isVisible = false }
    private val contentLoader = ProgressIndicator().apply { isVisible = false }
    private val contentNoDetails = Text("No details for selected package")
    private val contentPane = StackPane(content, contentLoader, contentNoDetails)

    override val root = TitledPane("Details", contentPane).apply {
        isCollapsible = false
    }

    init {
        selectedPackage.addListener { _, _, newValue ->
            content.isVisible = false
            contentLoader.isVisible = false

            if (newValue == null || !newValue.detailsAvailable) {
                contentNoDetails.isVisible = true
            } else {
                contentNoDetails.isVisible = false
                refreshDetailsJob(newValue)
            }
        }
    }

    private fun refreshDetailsJob(pack: AndroidPackage) {
        coroutineScope.launch {
            contentLoader.isVisible = true

            pack.refreshInstallLocation()

            nameTextField.text = pack.name
            pathsList.items = pack.paths.observableList()

            content.isVisible = true
            contentLoader.isVisible = false
        }
    }
}
