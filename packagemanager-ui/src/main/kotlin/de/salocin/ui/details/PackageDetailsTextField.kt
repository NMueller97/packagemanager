package de.salocin.ui.details

import de.salocin.ui.View
import de.salocin.ui.getValue
import de.salocin.ui.setValue
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import javafx.scene.text.Text

class PackageDetailsTextField(label: String, text: String = "") : View {

    private val textLabel = Text(label)
    private val textField = TextField(text)

    var text: String by textField.textProperty()

    override val root = VBox(textLabel, textField).apply {
        isDisable = true
    }
}
