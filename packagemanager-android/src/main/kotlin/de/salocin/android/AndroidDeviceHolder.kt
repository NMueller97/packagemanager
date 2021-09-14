package de.salocin.android

import de.salocin.android.adb.AdbCommands

object AndroidDeviceHolder {

    var devices = emptyList<AndroidDevice>()
        private set

    suspend fun refreshDevices() {
        devices = AdbCommands.devicesCommand.execute()
    }
}
