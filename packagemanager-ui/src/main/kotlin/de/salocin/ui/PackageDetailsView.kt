package de.salocin.ui

import javafx.scene.control.TitledPane
import javafx.scene.layout.BorderPane

class PackageDetailsView : View {

    private val content = BorderPane()

    override val root = TitledPane("Details", content).apply {
        isCollapsible = false
    }
}
