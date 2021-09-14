package de.salocin.android

import java.nio.file.Path

class DummyAndroidPackage(override val name: String) : AndroidPackage {

    override val type = AndroidPackageType.UNKNOWN
    override val paths = emptyList<Path>()
    override val detailsAvailable = false
    override val downloadable = false

    override suspend fun refreshInstallLocation() {
        // nothing to do
    }
}
