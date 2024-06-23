val kotlinCoroutinesVersion: String by extra
val javafxVersion: String by extra

plugins {
    id("packagemanager.shared")
    id("org.openjfx.javafxplugin") version "0.1.0"
    application
}

kotlin {
    target {
        version = JavaVersion.VERSION_17
    }
}

javafx {
    version = javafxVersion
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("de.salocin.ui.PackageManagerApplicationKt")
}

dependencies {
    api(project(":packagemanager-android"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$kotlinCoroutinesVersion")
}
