package de.salocin.android.device

import de.salocin.packagemanager.ProgressObserver
import de.salocin.packagemanager.device.Device
import java.nio.file.Path

data class FakeAndroidDevice(
    override val serialNumber: String,
    override val model: String
) : Device {

    override val apps: List<FakeAndroidApp> = emptyList()

    override suspend fun refreshApps(observer: ProgressObserver?) {
        // nothing to do
    }

    override suspend fun installApp(path: Path, observer: ProgressObserver?) {
        // nothing to do
    }

    override fun toString(): String {
        return model
    }

    override fun equals(other: Any?): Boolean {
        return other === this || other is FakeAndroidDevice
    }

    override fun hashCode() = model.hashCode()

    companion object {

        private const val SERIAL_NUMBER = "null"

        val noDevicesConnected = FakeAndroidDevice(SERIAL_NUMBER, "No devices connected")
    }
}
