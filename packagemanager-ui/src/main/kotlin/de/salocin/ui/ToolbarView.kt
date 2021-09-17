package de.salocin.ui

import de.salocin.android.device.AndroidDevice
import de.salocin.android.device.AndroidDeviceHolder
import de.salocin.android.device.FakeAndroidDevice
import de.salocin.ui.dialog.CancelableProgressDialog
import de.salocin.ui.dialog.ProgressDialog
import de.salocin.ui.fontawesome.FA_SYNC
import de.salocin.ui.fontawesome.FA_UPLOAD
import de.salocin.ui.fontawesome.fontAwesomeButton
import javafx.beans.value.ObservableValue
import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Path

class ToolbarView(app: PackageManagerApplication, private val owner: Window) : ApplicationView(app) {

    private val devicesComboBox = ComboBox<AndroidDevice>().apply {
        selectFirstWhenNoSelection()
    }

    val selectedDevice: ObservableValue<AndroidDevice> = devicesComboBox.selectionModel.selectedItemProperty()

    private val refreshButton = fontAwesomeButton("Refresh", FA_SYNC) {
        refreshDevicesJob()
    }

    private val installButton = fontAwesomeButton("Install", FA_UPLOAD) {
        val dialog = CancelableProgressDialog(owner)
        dialog.cancelableJob = app.launch {
            onInstall(dialog)
        }
    }

    override val root = HBox().apply {
        spacing = 5.0

        children.add(devicesComboBox)
        children.add(refreshButton)
    }

    init {
        refreshDevicesJob()
    }

    private fun ComboBox<*>.selectFirstWhenNoSelection() {
        if (selectionModel.selectedItem == null) {
            selectionModel.selectFirst()
        }
    }

    private fun refreshDevicesJob() {
        app.launch {
            devicesComboBox.items = null
            devicesComboBox.placeholder = ProgressIndicator()
            AndroidDeviceHolder.refreshDevices()
            devicesComboBox.items = if (AndroidDeviceHolder.devices.isEmpty()) {
                FXCollections.singletonObservableList(FakeAndroidDevice.noDevicesConnected)
            } else {
                AndroidDeviceHolder.devices.observableList()
            }
            devicesComboBox.selectFirstWhenNoSelection()
        }
    }

    private suspend fun onInstall(dialog: ProgressDialog) {
        val file: File? = FileChooser().apply {
            extensionFilters.add(FileChooser.ExtensionFilter("Android Package", ".apk"))
            extensionFilters.add(FileChooser.ExtensionFilter("Split Android Packages", ".zip"))
        }.showOpenDialog(owner)

        if (file != null) {
            dialog.show()
            install(dialog, file.toPath())
        }
    }

    private suspend fun install(dialog: ProgressDialog, target: Path) {
    }
}
