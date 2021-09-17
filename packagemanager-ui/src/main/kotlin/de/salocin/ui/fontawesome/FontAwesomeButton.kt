package de.salocin.ui.fontawesome

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.text.Text
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun fontAwesomeButton(
    title: String = "",
    icon: FontAwesomeIcon? = null,
    onAction: Button.() -> Unit
): Button {
    return Button(title, icon?.let { fontAwesomeIcon(it) }).apply {
        setOnAction {
            onAction()
        }
    }
}

fun fontAwesomeCoroutineButton(
    coroutineScope: CoroutineScope,
    title: String = "",
    icon: FontAwesomeIcon? = null,
    onAction: suspend Button.() -> Unit
): Button {
    return fontAwesomeButton(title, icon) {
        coroutineScope.launch {
            onAction()
        }
    }
}

fun fontAwesomeIcon(icon: FontAwesomeIcon): Node {
    return Text(icon.unicode).apply {
        font = FontAwesomeFont
    }
}
