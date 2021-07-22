package de.salocin.device

import de.salocin.adb.AdbCommands
import de.salocin.api.AndroidPackage
import de.salocin.api.AndroidPackageType
import de.salocin.api.RemoteAndroidPackage
import de.salocin.task.Task
import de.salocin.util.mapInt
import javafx.beans.binding.Bindings
import javafx.beans.binding.IntegerExpression
import javafx.beans.property.ReadOnlyBooleanWrapper
import javafx.beans.value.ObservableBooleanValue
import javafx.beans.value.ObservableIntegerValue
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.collections.transformation.FilteredList

/**
 * Represents a connected Android device with a [model] and a unique [serialNumber]. The id is retrieved by running `adb list`.
 */
class SimpleAndroidDevice(
    override val serialNumber: String,
    override val model: String
) : AndroidDevice {

    private val mutableIsLoadingPackages = ReadOnlyBooleanWrapper()

    override val dataPackages: ObservableList<AndroidPackage> =
        FXCollections.observableArrayList()
    override val systemPackages: ObservableList<AndroidPackage> =
        FXCollections.observableArrayList()
    override val vendorPackages: ObservableList<AndroidPackage> =
        FXCollections.observableArrayList()
    override val unknownPackages: ObservableList<AndroidPackage> =
        FXCollections.observableArrayList()

    override val dataPackagesCount: ObservableIntegerValue = Bindings.size(dataPackages)
    override val systemPackagesCount: ObservableIntegerValue = Bindings.size(systemPackages)
    override val vendorPackagesCount: ObservableIntegerValue = Bindings.size(vendorPackages)
    override val unknownPackagesCount: ObservableIntegerValue = Bindings.size(unknownPackages)
    override val totalPackagesCount: ObservableIntegerValue =
        IntegerExpression.integerExpression(dataPackagesCount)
            .add(systemPackagesCount)
            .add(vendorPackagesCount)
            .add(unknownPackagesCount)
            .mapInt { it.toInt() }

    override val dataPackagesFiltered: FilteredList<AndroidPackage> =
        dataPackages.filtered { true }
    override val systemPackagesFiltered: FilteredList<AndroidPackage> =
        systemPackages.filtered { true }
    override val vendorPackagesFiltered: FilteredList<AndroidPackage> =
        vendorPackages.filtered { true }
    override val unknownPackagesFiltered: FilteredList<AndroidPackage> =
        unknownPackages.filtered { true }

    override val isLoadingPackagesProperty: ObservableBooleanValue =
        mutableIsLoadingPackages.readOnlyProperty

    override fun refreshPackagesAsync(): Task<Unit> {
        mutableIsLoadingPackages.value = true

        dataPackages.clear()
        systemPackages.clear()
        vendorPackages.clear()
        unknownPackages.clear()

        return AdbCommands.PackageManager.packagesCommand.executeAsync { lines ->
            lines.forEach { line ->
                val location = line[0]
                val name = line[1]

                val isData = location.startsWith("/data")
                val isSystem = location.startsWith("/system")
                val isVendor = location.startsWith("/system_ext") ||
                        location.startsWith("/vendor") || location.startsWith("/product")

                val type = when {
                    isData -> AndroidPackageType.DATA
                    isSystem -> AndroidPackageType.SYSTEM
                    isVendor -> AndroidPackageType.VENDOR
                    else -> AndroidPackageType.UNKNOWN
                }

                val remotePackage = RemoteAndroidPackage(this, name, type)

                when {
                    isData -> dataPackages.add(remotePackage)
                    isSystem -> systemPackages.add(remotePackage)
                    isVendor -> vendorPackages.add(remotePackage)
                    else -> unknownPackages.add(remotePackage)
                }
            }

            mutableIsLoadingPackages.value = false
        }
    }

    override fun toString() = "$model ($serialNumber)"

}
