package de.salocin.ui

import de.salocin.ui.fontawesome.FA_TIMES
import de.salocin.ui.fontawesome.fontAwesomeButton
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.stage.Window
import kotlinx.coroutines.Job

class CancelableProgressDialog(owner: Window) : ProgressDialog(owner) {

    var cancelableJob: Job? = null

    private val closeButton = fontAwesomeButton("Close", FA_TIMES) {
        cancelableJob?.cancel()
    }

    private val buttonLayout = HBox(closeButton).apply {
        alignment = Pos.BASELINE_RIGHT
    }

    init {
        contentLayout.children.add(buttonLayout)

        stage.setOnCloseRequest {
            cancelableJob?.cancel()
        }
    }
}
