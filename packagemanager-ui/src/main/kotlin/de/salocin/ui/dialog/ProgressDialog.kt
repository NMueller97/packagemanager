package de.salocin.ui.dialog

import de.salocin.packagemanager.ProgressObserver
import de.salocin.ui.getValue
import de.salocin.ui.setValue
import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.ProgressBar
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window

private const val PROGRESS_BAR_WIDTH = 500.0
private const val CONTENT_SPACING = 10.0

open class ProgressDialog(owner: Window) : ProgressObserver {
    private val progressProperty = SimpleDoubleProperty(0.0)
    var progress: Double by progressProperty

    private val maxProgressProperty = SimpleDoubleProperty(1.0)
    var maxProgress: Double by maxProgressProperty

    private val progressBar =
        ProgressBar().apply {
            progressProperty().bind(progressProperty.divide(maxProgressProperty))
            prefWidth = PROGRESS_BAR_WIDTH
        }

    private val messageText = Text()
    var message: String by messageText.textProperty()

    protected val contentLayout =
        VBox(CONTENT_SPACING, messageText, progressBar).apply {
            padding = Insets(CONTENT_SPACING)
        }

    protected val stage =
        Stage().apply {
            initOwner(owner)
            initModality(Modality.WINDOW_MODAL)
            scene = Scene(contentLayout)
            contentLayout.autosize()
            sizeToScene()
        }

    fun show() {
        stage.show()
    }

    fun close() {
        stage.close()
    }

    override suspend fun notifyProgressChange(progress: Int) {
        this.progress = progress.toDouble()
    }

    override suspend fun notifyMaxProgressChange(maxProgress: Int) {
        this.maxProgress = maxProgress.toDouble()
    }

    override suspend fun notifyMessageChange(message: String) {
        this.message = message
    }
}
