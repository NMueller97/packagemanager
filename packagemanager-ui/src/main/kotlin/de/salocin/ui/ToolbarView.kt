package de.salocin.ui

import de.salocin.android.AndroidDevice
import de.salocin.android.AndroidDeviceHolder
import de.salocin.android.FakeAndroidDevice
import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.ProgressIndicator
import javafx.scene.layout.HBox
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ToolbarView(coroutineScope: CoroutineScope) : CoroutineView(coroutineScope) {

    private val devicesComboBox = ComboBox<AndroidDevice>().apply {
        selectFirstWhenNoSelection()
    }

    val selectedDevice = devicesComboBox.selectionModel.selectedItemProperty()

    private val refreshButton = Button("Refresh").apply {
        setOnAction {
            refreshDevicesJob()
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
        coroutineScope.launch {
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
}
