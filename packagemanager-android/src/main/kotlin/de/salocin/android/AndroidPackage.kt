package de.salocin.api

import de.salocin.android.AndroidPackageType
import de.salocin.task.Task
import javafx.beans.value.ObservableStringValue
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import java.nio.file.Path

/**
 * Abstraction of an Android package, that only knows its unique package name. The package can be at
 * any [InstallLocation] [I].
 */
interface AndroidPackage {

    /**
     * The package name.
     */
    val nameProperty: ObservableStringValue

    /**
     * The package name.
     */
    val name: String
        get() = nameProperty.value

    /**
     * The package type.
     */
    val typeProperty: ObservableValue<AndroidPackageType>

    /**
     * The package type.
     */
    val type: AndroidPackageType
        get() = typeProperty.value

    /**
     * The install location of this package.
     */
    val installLocation: ObservableList<Path>

    /**
     * Whether this package can be downloaded.
     */
    val downloadable: Boolean

    /**
     * Asynchronously refresh the [installLocation].
     */
    fun refreshInstallLocationAsync(): Task<ObservableList<Path>>
}
