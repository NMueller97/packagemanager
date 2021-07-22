package de.salocin.device

import de.salocin.adb.AdbCommands
import de.salocin.api.AndroidPackage
import de.salocin.task.Task
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableIntegerValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList
import tornadofx.asUnmodifiable

/**
 * A Android device with an associated name and an unique identifier.
 */
interface AndroidDevice {

    /**
     * The device's model.
     */
    val model: String

    /**
     * The device's serial number.
     */
    val serialNumber: String

    /**
     * The data packages installed on this device.
     */
    val dataPackages: ObservableList<AndroidPackage>

    /**
     * The system packages installed on this device.
     */
    val systemPackages: ObservableList<AndroidPackage>

    /**
     * The vendor packages installed on this device.
     */
    val vendorPackages: ObservableList<AndroidPackage>

    /**
     * The unknown packages installed on this device, which can not be categorized to [data][dataPackages],
     * [system][systemPackages] or [vendor][vendorPackages].
     */
    val unknownPackages: ObservableList<AndroidPackage>

    /**
     * A binding to the total count of installed packages, which is always equal to the sum of
     * [dataPackagesCount] + [systemPackagesCount] + [vendorPackagesCount] + [unknownPackagesCount].
     */
    val totalPackagesCount: ObservableIntegerValue

    /**
     * A binding to the [dataPackages] count.
     */
    val dataPackagesCount: ObservableIntegerValue

    /**
     * A binding to the [systemPackages] count.
     */
    val systemPackagesCount: ObservableIntegerValue

    /**
     * A binding to the [vendorPackages] count.
     */
    val vendorPackagesCount: ObservableIntegerValue

    /**
     * A binding to the [unknownPackages] count.
     */
    val unknownPackagesCount: ObservableIntegerValue

    /**
     * Whether the [dataPackages], [systemPackages], [vendorPackages] and [unknownPackages] list are currently be
     * updated or not.
     */
    val isLoadingPackagesProperty: ObservableBooleanValue

    /**
     * A filtered view for [dataPackages] for UI handling.
     */
    val dataPackagesFiltered: FilteredList<AndroidPackage>

    /**
     * A filtered view for [systemPackages] for UI handling.
     */
    val systemPackagesFiltered: FilteredList<AndroidPackage>

    /**
     * A filtered view for [vendorPackages] for UI handling.
     */
    val vendorPackagesFiltered: FilteredList<AndroidPackage>

    /**
     * A filtered view for [unknownPackages] for UI handling.
     */
    val unknownPackagesFiltered: FilteredList<AndroidPackage>

    /**
     * Asynchronously refreshes the [dataPackages], [systemPackages], [vendorPackages] and [unknownPackages] lists.
     */
    fun refreshPackagesAsync(): Task<Unit>

    companion object {

        private val mutableDevices = FXCollections.observableArrayList<AndroidDevice>()

        /**
         * All devices that are currently connected, updated with [refreshDevicesAsync]. If no device is connected, the
         * underlying list will have a single [DummyAndroidDevice], this means the list will never be empty.
         */
        val devices = mutableDevices.asUnmodifiable()


        private val mutableIsLoadingDevices = ReadOnlyBooleanWrapper()

        /**
         * Asynchronously refreshes the [devices] list and pulls fresh values taken from ADB.
         */
        fun refreshDevicesAsync(): Task<ObservableList<AndroidDevice>> {
            mutableIsLoadingDevices.value = true
            mutableDevices.clear()

            return AdbCommands.devicesCommand.executeAsync(mutableDevices).thenRun {
                if (it.isEmpty()) {
                    it.add(DummyAndroidDevice.noDevicesConnected)
                }

                it
            }
        }
    }
}
