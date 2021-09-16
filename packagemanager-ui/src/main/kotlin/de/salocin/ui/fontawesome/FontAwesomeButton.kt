package de.salocin.ui.fontawesome

import javafx.scene.control.Button
import javafx.scene.text.Text


fun FontAwesomeButton(title: String = "", icon: FontAwesomeIcon? = null): Button {
    return Button(title, icon?.let {
        Text(icon.unicode).apply {
            font = FontAwesomeFont
        }
    })
}
