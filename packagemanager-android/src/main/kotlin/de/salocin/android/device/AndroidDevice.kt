package de.salocin.android.device

interface AndroidDevice {

    val model: String

    val serialNumber: String

    val dataPackages: List<AndroidPackage>

    val systemPackages: List<AndroidPackage>

    val vendorPackages: List<AndroidPackage>

    val unknownPackages: List<AndroidPackage>

    suspend fun refreshPackages()
}
