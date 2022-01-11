package de.salocin.ui.details

import de.salocin.packagemanager.device.DevicePath
import de.salocin.ui.View
import de.salocin.ui.getValue
import de.salocin.ui.setValue
import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class PackageDetailsPathList(label: String) : View {

    private val listLabel = Text(label)
    private val listView = ListView<DevicePath>().apply {
    }

    var items: ObservableList<DevicePath> by listView.itemsProperty()

    override val root = VBox(listLabel, listView)
}
