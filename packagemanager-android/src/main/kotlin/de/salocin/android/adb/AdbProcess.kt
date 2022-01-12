package de.salocin.android.adb

import de.salocin.android.device.AndroidDevice
import de.salocin.packagemanager.io.OutputParser
import de.salocin.packagemanager.io.SystemProcess

class AdbProcess<T>(device: String?, arguments: List<String>, parser: OutputParser<T>?) :
    SystemProcess<T>(
        arguments.concatBaseCommand(device),
        stdoutParser = parser,
        commandPipe = System.out,
        stdoutPipe = System.out,
        stderrPipe = System.err
    ) {

    companion object {

        fun build(device: AndroidDevice?, arguments: List<String>): SystemProcess<String> {
            return AdbProcess(device?.serialNumber, arguments, null)
        }

        fun <T> build(
            device: AndroidDevice?,
            arguments: List<String>,
            stdoutParser: OutputParser<T>
        ): SystemProcess<T> {
            return AdbProcess(device?.serialNumber, arguments, stdoutParser)
        }

        private fun List<String>.concatBaseCommand(device: String?): List<String> {
            return if (device == null) {
                listOf("adb") + this
            } else {
                listOf("adb", "-s", device) + this
            }
        }
    }
}
