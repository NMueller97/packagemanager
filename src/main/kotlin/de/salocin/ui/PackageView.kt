package de.salocin.ui

import javafx.geometry.Orientation
import tornadofx.View
import tornadofx.borderpane
import tornadofx.splitpane

class PackageView : View() {

    private val listView by inject<PackageListView>()
    private val detailsView by inject<PackageDetailsView>()

    override val root = borderpane {
        top<ToolbarView>()
        center = splitpane(Orientation.HORIZONTAL) {
            add(listView)
            add(detailsView)

            detailsView.root.prefHeightProperty().bind(heightProperty())
        }
    }
}
