package de.salocin.ui

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.Stage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class PackageManagerApplication : Application(), CoroutineScope {
    override val coroutineContext: CoroutineContext = Dispatchers.Main

    override fun start(primaryStage: Stage?) {
        primaryStage?.let { stage ->
            stage.scene = Scene(PackageView(this, stage, hostServices).root, INITIAL_WIDTH, INITIAL_HEIGHT)
            stage.show()
        }
    }
}

private const val INITIAL_WIDTH = 800.0
private const val INITIAL_HEIGHT = 600.0

fun main(args: Array<String>) {
    Application.launch(PackageManagerApplication::class.java, *args)
}
