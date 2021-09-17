package de.salocin.ui.details

import de.salocin.android.device.AndroidPackage
import de.salocin.ui.CoroutineView
import de.salocin.ui.dialog.CancelableProgressDialog
import de.salocin.ui.dialog.ProgressDialog
import de.salocin.ui.fontawesome.FA_DOWNLOAD
import de.salocin.ui.fontawesome.fontAwesomeButton
import javafx.application.HostServices
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path

class PackageDetailsButtons(
    coroutineScope: CoroutineScope,
    private val owner: Window,
    private val hostServices: HostServices,
    private val selectedPackage: ObservableValue<AndroidPackage?>
) : CoroutineView(coroutineScope) {

    private val downloadButton = fontAwesomeButton(title = "Download", icon = FA_DOWNLOAD) {
        val dialog = CancelableProgressDialog(owner)
        dialog.cancelableJob = coroutineScope.launch {
            onDownload(dialog)
        }
    }

    override val root = HBox(downloadButton).apply {
        alignment = Pos.BASELINE_RIGHT
        padding = Insets(10.0)
    }

    private suspend fun onDownload(dialog: ProgressDialog) {
        val pack: AndroidPackage = selectedPackage.value ?: return
        pack.refreshInstallLocation()

        if (pack.paths.isEmpty()) {
            TODO("Show error for user, that there is no file to download")
        }

        val file: File? = FileChooser().apply {
            if (pack.paths.size == 1) {
                initialFileName = "${pack.name}.apk"
                extensionFilters.add(FileChooser.ExtensionFilter("Android Package", ".apk"))
            } else {
                initialFileName = "${pack.name}.zip"
                extensionFilters.add(FileChooser.ExtensionFilter("Split Android Packages", ".zip"))
            }
        }.showSaveDialog(owner)

        if (file != null) {
            dialog.show()
            download(dialog, pack, file.toPath())
        }
    }

    private suspend fun download(dialog: ProgressDialog, pack: AndroidPackage, target: Path) {
        pack.download(target, observer = dialog)
        hostServices.showDocument(target.parent.toUri().toString())
    }
}
