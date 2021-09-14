package de.salocin.ui

import javafx.collections.ObservableList
import javafx.scene.control.ListView
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class PackageDetailsList<T>(label: String) : View {

    private val listLabel = Text(label)
    private val listView = ListView<T>().apply {

    }

    var items: ObservableList<T> by listView.itemsProperty()

    override val root = VBox(listLabel, listView)
}
