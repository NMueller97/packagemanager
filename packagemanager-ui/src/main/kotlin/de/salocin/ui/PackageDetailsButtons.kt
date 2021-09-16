package de.salocin.ui

import de.salocin.ui.fontawesome.FA_DOWNLOAD
import de.salocin.ui.fontawesome.FontAwesomeButton
import javafx.scene.layout.HBox

class PackageDetailsButtons : View {

    private val downloadButton = FontAwesomeButton(title = "Download", icon = FA_DOWNLOAD)

    override val root = HBox(downloadButton).apply {

    }
}
