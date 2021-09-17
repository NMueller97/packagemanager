package de.salocin.android.device

class FakeAndroidDevice(
    override val serialNumber: String,
    override val model: String
) : AndroidDevice {

    override val dataPackages = emptyList<AndroidPackage>()
    override val systemPackages = emptyList<AndroidPackage>()
    override val vendorPackages = emptyList<AndroidPackage>()
    override val unknownPackages = emptyList<AndroidPackage>()

    override suspend fun refreshPackages() {
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
