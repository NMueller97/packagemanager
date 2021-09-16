package de.salocin.ui

import de.salocin.android.AndroidPackage
import de.salocin.android.adb.AdbCommands
import de.salocin.ui.fontawesome.FA_DOWNLOAD
import de.salocin.ui.fontawesome.fontAwesomeButton
import javafx.application.HostServices
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.layout.HBox
import javafx.stage.FileChooser
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.createFile
import kotlin.io.path.deleteIfExists
import kotlin.io.path.inputStream
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.moveTo
import kotlin.io.path.name
import kotlin.io.path.outputStream

class PackageDetailsButtons(
    coroutineScope: CoroutineScope,
    private val owner: Window,
    private val hostServices: HostServices,
    private val selectedPackage: ObservableValue<AndroidPackage?>
) : CoroutineView(coroutineScope) {

    private val downloadButton = fontAwesomeButton(title = "Download", icon = FA_DOWNLOAD).apply {
        setOnAction {
            coroutineScope.launch {
                onDownload()
            }
        }
    }

    override val root = HBox(downloadButton).apply {
        alignment = Pos.BASELINE_RIGHT
        padding = Insets(10.0)
    }

    private suspend fun onDownload() {
        val pack: AndroidPackage = selectedPackage.value ?: return
        pack.refreshInstallLocation()

        if (pack.paths.isEmpty()) {
            TODO("Show error for user, that there is no file to download")
        }

        val file: File? = FileChooser().apply {
            if (pack.paths.size == 1) {
                initialFileName = "${pack.name}.apk"
                extensionFilters.add(FileChooser.ExtensionFilter("Android Package", ".apk"))
            } else {
                initialFileName = "${pack.name}.zip"
                extensionFilters.add(FileChooser.ExtensionFilter("Split Android Packages", ".zip"))
            }
        }.showSaveDialog(owner)

        if (file != null) {
            val dialog = ProgressDialog(owner)
            dialog.show()
            download(dialog, pack, file.toPath())
        }
    }

    private suspend fun download(dialog: ProgressDialog, pack: AndroidPackage, target: Path) {
        val tempDirectoryJob = coroutineScope.async(Dispatchers.IO) {
            Files.createTempDirectory(null) as Path
        }

        val tempDir = tempDirectoryJob.await()

        dialog.progress = 0.0
        dialog.maxProgress = pack.paths.size.toDouble()

        for (source in pack.paths) {
            dialog.message = "Downloading ${source.name}"
            val tempTarget = tempDir.resolve(source.name)
            AdbCommands.pull(source, tempTarget).execute()
            dialog.progress++
        }

        dialog.message = "Packing downloaded files"

        coroutineScope.launch(Dispatchers.IO) {
            val entries = tempDir.listDirectoryEntries()

            if (entries.size == 1) {
                entries.first().moveTo(target, overwrite = true)
            } else {
                entries.zipTo(target)
            }

            entries.forEach { entry ->
                entry.deleteIfExists()
            }

            tempDir.deleteIfExists()
        }

        hostServices.showDocument(target.parent.toUri().toString())
        dialog.setResult(Unit)
        dialog.close()
        TODO("Make cancelable")
    }

    private fun List<Path>.zipTo(target: Path) {
        target.deleteIfExists()
        target.createFile()

        target.zipOutputStream().use { out ->
            forEach { file ->
                out.putNextEntry(file.asZipEntry())
                file.inputStream().copyTo(out)
            }
        }
    }

    private fun Path.zipOutputStream(): ZipOutputStream {
        return ZipOutputStream(outputStream())
    }

    private fun Path.asZipEntry(): ZipEntry {
        return ZipEntry(name)
    }
}
