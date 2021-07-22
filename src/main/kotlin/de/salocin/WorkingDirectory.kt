package de.salocin

import de.salocin.api.AndroidPackageType
import de.salocin.api.RemoteAndroidPackage
import javafx.beans.property.ReadOnlyObjectWrapper
import javafx.beans.value.ObservableValue
import java.nio.file.Files
import java.nio.file.Path

object WorkingDirectory {

    private val workingDir: Path = Path.of(Config.workingDirPath).mkdir()

    private val rawPackagesDir: Path = workingDir.resolve("raw").mkdir()
    val rawDataPackagesDir: Path = rawPackagesDir.resolve("data").mkdir()
    val rawSystemPackagesDir: Path = rawPackagesDir.resolve("system").mkdir()
    val rawVendorPackagesDir: Path = rawPackagesDir.resolve("vendor").mkdir()
    val rawUnknownPackagesDir: Path = rawPackagesDir.resolve("unknown").mkdir()

    private val resignedPackagesDir: Path = workingDir.resolve("resigned").mkdir()
    val resignedDataPackagesDir: Path = resignedPackagesDir.resolve("data").mkdir()
    val resignedSystemPackagesDir: Path = resignedPackagesDir.resolve("system").mkdir()
    val resignedVendorPackagesDir: Path = resignedPackagesDir.resolve("vendor").mkdir()
    val resignedUnknownPackagesDir: Path = resignedPackagesDir.resolve("unknown").mkdir()

    fun RemoteAndroidPackage.newLocalPathObservable(): ObservableValue<Path> {
        return ReadOnlyObjectWrapper(type.rawDirectoryPath.resolve("$name.apk")).readOnlyProperty
    }

    fun RemoteAndroidPackage.newLocalResignedPathObservable(): ObservableValue<Path> {
        return ReadOnlyObjectWrapper(type.resignedDirectoryPath.resolve("$name.apk")).readOnlyProperty
    }

    fun getPackageTypeFromFile(file: Path): AndroidPackageType? {
        val result = AndroidPackageType.values().find { type ->
            when (type) {
                AndroidPackageType.DATA -> file == rawDataPackagesDir || file == resignedSystemPackagesDir
                AndroidPackageType.SYSTEM -> file == rawSystemPackagesDir || file == resignedSystemPackagesDir
                AndroidPackageType.VENDOR -> file == rawVendorPackagesDir || file == resignedVendorPackagesDir
                AndroidPackageType.UNKNOWN -> file == rawUnknownPackagesDir || file == resignedUnknownPackagesDir
            }
        }

        val parent = file.parent

        return if (result == null && parent != null) {
            getPackageTypeFromFile(parent)
        } else {
            result
        }
    }

    private fun Path.mkdir(): Path = apply {
        if (!Files.exists(this)) {
            Files.createDirectory(this)
        }
    }

}
