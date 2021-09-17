package de.salocin.ui

import de.salocin.android.device.AndroidDevice
import de.salocin.android.device.AndroidPackage
import de.salocin.android.device.DummyAndroidPackage
import javafx.beans.value.ObservableValue
import javafx.scene.control.ProgressIndicator
import javafx.scene.control.TreeItem
import javafx.scene.control.TreeTableColumn
import javafx.scene.control.TreeTableView
import javafx.util.Callback
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty1

class PackageListView(
    app: PackageManagerApplication,
    selectedDevice: ObservableValue<AndroidDevice>
) : ApplicationView(app) {

    private val rootItem = TreeItem<AndroidPackage>().apply {
        isExpanded = true
    }

    private val dataPackagesItem =
        TreeItem<AndroidPackage>(DummyAndroidPackage("Data")).apply {
            rootItem.children.add(this)
            isExpanded = true
        }

    private val systemPackagesItem =
        TreeItem<AndroidPackage>(DummyAndroidPackage("System")).apply {
            rootItem.children.add(this)
        }

    private val vendorPackagesItem =
        TreeItem<AndroidPackage>(DummyAndroidPackage("Vendor")).apply {
            rootItem.children.add(this)
        }

    private val unknownPackagesItem =
        TreeItem<AndroidPackage>(DummyAndroidPackage("Unknown")).apply {
            rootItem.children.add(this)
        }

    override val root = TreeTableView<AndroidPackage>().apply {
        column("Name", AndroidPackage::name)
        columnResizePolicy = TreeTableView.CONSTRAINED_RESIZE_POLICY
        placeholder = ProgressIndicator()
    }

    val selectedPackage = root.selectionModel.selectedItemProperty().mapTo { it?.value }

    init {
        selectedDevice.addListener { _, _, device ->
            device?.let(this::refreshListJob)
        }

        selectedDevice.value?.let { device ->
            refreshListJob(device)
        }
    }

    private fun refreshListJob(device: AndroidDevice) {
        app.launch {
            root.root = null

            device.refreshPackages()
            dataPackagesItem.addPackages(device.dataPackages)
            systemPackagesItem.addPackages(device.systemPackages)
            vendorPackagesItem.addPackages(device.vendorPackages)
            unknownPackagesItem.addPackages(device.unknownPackages)

            rootItem.value = DummyAndroidPackage(device.toString())
            root.root = rootItem
        }
    }

    private fun <T : Any?> TreeTableView<AndroidPackage>.column(
        title: String,
        prop: KProperty1<AndroidPackage, T>
    ) {
        val column = TreeTableColumn<AndroidPackage, T>(title)
        column.cellValueFactory = Callback { prop.get(it.value.value).observable() }
        columns.add(column)
    }

    private fun TreeItem<AndroidPackage>.addPackages(packages: List<AndroidPackage>) {
        packages.sortedBy(AndroidPackage::name).forEach { pack ->
            children.add(TreeItem(pack))
        }
    }
}
