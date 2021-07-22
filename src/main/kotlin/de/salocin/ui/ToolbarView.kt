package de.salocin.ui

import de.salocin.device.AndroidDevice
import de.salocin.util.bind
import javafx.scene.control.ComboBox
import tornadofx.*

class ToolbarView : View() {

    override val root = hbox(5.0) {
        combobox<AndroidDevice> {
            placeholder = progressindicator {}

            items.bind(AndroidDevice.devices)
            items.onChange {
                selectFirstIfNoSelection()
            }

            selectFirstIfNoSelection()

            AndroidDevice.selectedDeviceProperty.bind(selectionModel.selectedItemProperty())
            AndroidDevice.refreshDevicesAsync()
        }

        button("Refresh") {
            action {
                AndroidDevice.refreshDevicesAsync()
            }
        }
    }

    private fun ComboBox<*>.selectFirstIfNoSelection() {
        if (selectionModel.selectedItem == null) {
            selectionModel.selectFirst()
        }
    }
}
