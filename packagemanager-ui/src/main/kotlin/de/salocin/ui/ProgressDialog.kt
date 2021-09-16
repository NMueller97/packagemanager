package de.salocin.ui

import javafx.beans.property.SimpleDoubleProperty
import javafx.scene.control.Dialog
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Window

class ProgressDialog(owner: Window) : Dialog<Unit>() {

    private val progressProperty = SimpleDoubleProperty(0.0)
    var progress: Double by progressProperty

    private val maxProgressProperty = SimpleDoubleProperty(1.0)
    var maxProgress: Double by maxProgressProperty

    private val progressBar = ProgressBar().apply {
        progressProperty().bind(progressProperty.divide(maxProgressProperty))
        prefWidth = 500.0
    }

    private val messageText = Text()
    var message: String by messageText.textProperty()

    init {
        initOwner(owner)
        initModality(Modality.WINDOW_MODAL)
        dialogPane.content = VBox(messageText, progressBar)
    }
}
