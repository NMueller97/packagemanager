package de.salocin.adb

import de.salocin.android.adb.AdbCommand
import de.salocin.android.adb.AdbCommandBuilder
import de.salocin.device.AndroidDevice
import de.salocin.device.SimpleAndroidDevice
import de.salocin.parser.LineOutputParser
import de.salocin.parser.RegexOutputParser
import java.nio.file.Path

object AdbCommands {

    private val shellBaseCommand = AdbCommandBuilder("shell")
    private val devicesRegex = Regex("^([A-Z0-9]+).+model:([^\\s]*).*\$")

    val devicesCommand: AdbCommand<AndroidDevice> = AdbCommandBuilder("devices")
        .resolve("-l")
        .build(RegexOutputParser(devicesRegex)
            .takeAllGroups()
            .mapEachLineTo { SimpleAndroidDevice(it[0], it[1]) })

    object PackageManager {

        private val stripPackagePrefixRegex = Regex("^package:(.+)$")
        private val packagesRegex = Regex("^package:(.+)=(.+)$")
        private val stripPackagePrefix: LineOutputParser<Path> =
            RegexOutputParser(stripPackagePrefixRegex).takeGroup(1).mapEachLineTo { Path.of(it) }
        private val pmBaseCommand = shellBaseCommand.resolve("pm")
        private val pathBaseCommand = pmBaseCommand.resolve("path")

        val packagesCommand = pmBaseCommand.resolve("list").resolve("packages").resolve("-f")
            .build(RegexOutputParser(packagesRegex).takeAllGroups())

        fun pathCommand(packageName: String): AdbCommand<Path> =
            pathBaseCommand.resolve(packageName).build(stripPackagePrefix)
    }
}
