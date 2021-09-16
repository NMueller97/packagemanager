package de.salocin.ui

import javafx.application.HostServices
import javafx.geometry.Orientation
import javafx.scene.control.SplitPane
import javafx.scene.layout.BorderPane
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope

class PackageView(
    coroutineScope: CoroutineScope,
    owner: Window,
    hostServices: HostServices
) : CoroutineView(coroutineScope) {

    private val toolbar = ToolbarView(coroutineScope)
    private val list = PackageListView(coroutineScope, toolbar.selectedDevice)
    private val details = PackageDetailsView(coroutineScope, owner, hostServices, list.selectedPackage)

    override val root = BorderPane().apply {
        top = toolbar.root

        center = SplitPane(list.root, details.root).apply {
            orientation = Orientation.HORIZONTAL
            details.root.prefHeightProperty().bind(heightProperty())
        }
    }
}
