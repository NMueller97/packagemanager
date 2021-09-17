package de.salocin.android.device

import de.salocin.android.adb.AdbCommands

class RemoteAndroidDevice(
    override val serialNumber: String,
    override val model: String
) : AndroidDevice {

    override var dataPackages = mutableListOf<AndroidPackage>()
        private set
    override var systemPackages = mutableListOf<AndroidPackage>()
        private set
    override var vendorPackages = mutableListOf<AndroidPackage>()
        private set
    override var unknownPackages = mutableListOf<AndroidPackage>()
        private set

    override suspend fun refreshPackages() {
        dataPackages.clear()
        systemPackages.clear()
        vendorPackages.clear()
        unknownPackages.clear()

        AdbCommands.PackageManager.packagesCommand.execute().forEach { line ->
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
    }

    override fun toString(): String {
        return model
    }
}
