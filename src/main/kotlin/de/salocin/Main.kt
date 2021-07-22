package de.salocin

import de.salocin.ui.PackageView
import javafx.application.Application
import javafx.scene.Scene
import tornadofx.App
import tornadofx.UIComponent

class Main : App(PackageView::class) {

    override fun createPrimaryScene(view: UIComponent): Scene =
        Scene(view.root, INITIAL_WIDTH, INITIAL_HEIGHT)
}

private const val INITIAL_WIDTH = 800.0
private const val INITIAL_HEIGHT = 600.0

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
