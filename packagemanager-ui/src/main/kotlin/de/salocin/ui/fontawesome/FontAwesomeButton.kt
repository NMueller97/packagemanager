package de.salocin.ui.fontawesome

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.text.Text

fun fontAwesomeButton(title: String = "", icon: FontAwesomeIcon? = null): Button {
    return Button(title, icon?.let { fontAwesomeIcon(it) })
}

fun fontAwesomeIcon(icon: FontAwesomeIcon): Node {
    return Text(icon.unicode).apply {
        font = FontAwesomeFont
    }
}
