package de.salocin.android

import java.nio.file.Path

interface AndroidPackage {

    val name: String

    val type: AndroidPackageType

    val paths: List<Path>

    val downloadable: Boolean

    suspend fun refreshInstallLocation()
}
