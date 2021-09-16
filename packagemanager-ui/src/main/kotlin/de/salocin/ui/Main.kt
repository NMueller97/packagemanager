package de.salocin.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

class Main : Application() {

    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun start(primaryStage: Stage?) {
        primaryStage?.apply {
            scene = Scene(PackageView(mainScope, this, hostServices).root, INITIAL_WIDTH, INITIAL_HEIGHT)
            show()
        }
    }

    override fun stop() {
        mainScope.cancel()
    }
}

private const val INITIAL_WIDTH = 800.0
private const val INITIAL_HEIGHT = 600.0

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
