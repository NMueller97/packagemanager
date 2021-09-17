package de.salocin.ui.fontawesome

import de.salocin.ui.PackageManagerApplication
import javafx.scene.text.Font

val FontAwesomeFont: Font =
    Font.loadFont(PackageManagerApplication::class.java.getResourceAsStream("/fa-solid.ttf"), 16.0)
