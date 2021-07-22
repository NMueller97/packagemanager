package de.salocin.device

import de.salocin.api.AndroidPackage
import de.salocin.task.Task
import de.salocin.util.constantBooleanBinding
import de.salocin.util.constantIntBinding
import javafx.beans.binding.IntegerBinding
import javafx.beans.value.ObservableBooleanValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList

/**
 * A dummy [AndroidDevice] indicating that no other devices are connected.
 */
class DummyAndroidDevice(
    override val serialNumber: String,
    override val model: String
) : AndroidDevice {

    override val dataPackages: ObservableList<AndroidPackage> =
        FXCollections.emptyObservableList()
    override val systemPackages: ObservableList<AndroidPackage> =
        FXCollections.emptyObservableList()
    override val vendorPackages: ObservableList<AndroidPackage> =
        FXCollections.emptyObservableList()
    override val unknownPackages: ObservableList<AndroidPackage> =
        FXCollections.emptyObservableList()

    override val dataPackagesCount: IntegerBinding = constantIntBinding(0)
    override val systemPackagesCount: IntegerBinding = constantIntBinding(0)
    override val vendorPackagesCount: IntegerBinding = constantIntBinding(0)
    override val unknownPackagesCount: IntegerBinding = constantIntBinding(0)
    override val totalPackagesCount: IntegerBinding = constantIntBinding(0)

    override val dataPackagesFiltered: FilteredList<AndroidPackage> =
        FilteredList(dataPackages)
    override val systemPackagesFiltered: FilteredList<AndroidPackage> =
        FilteredList(systemPackages)
    override val vendorPackagesFiltered: FilteredList<AndroidPackage> =
        FilteredList(vendorPackages)
    override val unknownPackagesFiltered: FilteredList<AndroidPackage> =
        FilteredList(unknownPackages)

    override val isLoadingPackagesProperty: ObservableBooleanValue = constantBooleanBinding(false)

    private val completedPackagesTask = Task.completed(Unit)

    override fun refreshPackagesAsync() = completedPackagesTask

    override fun toString() = model

    override fun equals(other: Any?) = other === this || other is DummyAndroidDevice

    override fun hashCode() = model.hashCode()

    companion object {

        private const val SERIAL_NUMBER = "null"

        val noDevicesConnected = DummyAndroidDevice(SERIAL_NUMBER, "No devices connected")
        val loadingDevices = DummyAndroidDevice(SERIAL_NUMBER, "Loading devices...")
    }
}
