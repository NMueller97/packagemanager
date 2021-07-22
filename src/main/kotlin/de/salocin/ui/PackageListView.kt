package de.salocin.ui

import de.salocin.api.AndroidPackage
import de.salocin.api.DummyAndroidPackage
import de.salocin.device.AndroidDevice
import de.salocin.util.map
import de.salocin.util.selectList
import javafx.beans.binding.Binding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableView
import tornadofx.*

class PackageListView : View() {

    override val root = treetableview<AndroidPackage> {
        column("Package", AndroidPackage::nameProperty)

        root = TreeItem(DummyAndroidPackage("Root"))
        root.isExpanded = true
        root.valueProperty().bind(selectedDevice { DummyAndroidPackage(it?.model ?: "") })

        val dataPackagesItem = root.treeitem(DummyAndroidPackage("Data"))
        val systemPackagesItem = root.treeitem(DummyAndroidPackage("System"))
        val vendorPackagesItem = root.treeitem(DummyAndroidPackage("Vendor"))
        val unknownPackagesItem = root.treeitem(DummyAndroidPackage("Unknown"))

        dataPackagesItem.bindToSelectedDevice {
            it?.dataPackagesFiltered ?: FXCollections.emptyObservableList()
        }
        systemPackagesItem.bindToSelectedDevice {
            it?.systemPackagesFiltered ?: FXCollections.emptyObservableList()
        }
        vendorPackagesItem.bindToSelectedDevice {
            it?.vendorPackagesFiltered ?: FXCollections.emptyObservableList()
        }
        unknownPackagesItem.bindToSelectedDevice {
            it?.unknownPackagesFiltered ?: FXCollections.emptyObservableList()
        }

        columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY
        UiContext.selectedPackageItemProperty.bind(selectionModel.selectedItemProperty())
    }

    init {
        AndroidDevice.selectedDeviceProperty.addListener { _, _, newValue ->
            newValue?.refreshPackagesAsync()
        }
    }

    private inline fun <reified T> selectedDevice(noinline mapping: (AndroidDevice?) -> T): Binding<T> =
        AndroidDevice.selectedDeviceProperty.map(mapping)

    private fun TreeItem<AndroidPackage>.bindToSelectedDevice(selector: (AndroidDevice?) -> ObservableList<AndroidPackage>) {
        children.bind(AndroidDevice.selectedDeviceProperty.selectList(selector)) { TreeItem(it) }
    }
}
