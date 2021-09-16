package de.salocin.android.adb

import de.salocin.android.AndroidDevice
import de.salocin.android.AndroidFileSystem
import de.salocin.android.RemoteAndroidDevice
import de.salocin.android.parser.RegexOutputParser
import java.nio.file.Path

object AdbCommands {

    private val shellBaseCommand = AdbCommandBuilder("shell")
    private val devicesRegex = Regex("^([A-Z0-9]+).+model:([^\\s]*).*\$")

    val devicesCommand: AdbCommand<AndroidDevice> = AdbCommandBuilder("devices")
        .resolve("-l")
        .build(
            RegexOutputParser(devicesRegex)
                .takeAllGroups()
                .mapEachLineTo { RemoteAndroidDevice(it[0], it[1]) })

    private val pullCommand = AdbCommandBuilder("pull") + "-a"

    fun pull(androidPath: Path, target: Path): AdbCommand<String> {
        return pullCommand.resolve(androidPath.toString()).resolve(target.toString()).build()
    }

    object PackageManager {

        private val stripPackagePrefixRegex = Regex("^package:(.+)$")
        private val packagesRegex = Regex("^package:(.+)=(.+)$")
        private val stripPackagePrefix =
            RegexOutputParser(stripPackagePrefixRegex).takeGroup(1)
                .mapEachLineTo { AndroidFileSystem.getPath(it) }
        private val pmBaseCommand = shellBaseCommand + "pm"
        private val pathBaseCommand = pmBaseCommand + "path"
        private val listPackagesCommand = pmBaseCommand + "list" + "packages" + "-f"

        val packagesCommand =
            listPackagesCommand.build(RegexOutputParser(packagesRegex).takeAllGroups())

        fun pathCommand(packageName: String): AdbCommand<Path> =
            pathBaseCommand.resolve(packageName).build(stripPackagePrefix)
    }
}
