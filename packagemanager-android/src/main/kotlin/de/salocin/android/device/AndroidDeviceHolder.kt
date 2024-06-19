package de.salocin.android.device

import de.salocin.android.adb.Adb

object AndroidDeviceHolder {
    var devices = emptyList<AndroidDevice>()
        private set

    suspend fun refreshDevices(): List<AndroidDevice> {
        devices = Adb.devices()
        return devices
    }
}
