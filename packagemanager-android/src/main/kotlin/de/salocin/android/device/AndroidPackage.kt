package de.salocin.android.device

import de.salocin.android.progress.ProgressObserver
import java.nio.file.Path

interface AndroidPackage {

    val name: String

    val type: AndroidPackageType

    val paths: List<Path>

    val detailsAvailable: Boolean

    val downloadable: Boolean

    suspend fun refreshInstallLocation()

    suspend fun download(target: Path, observer: ProgressObserver? = null)
}
