plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    application
}

kotlin {
    target {
        version = JavaVersion.VERSION_11
    }
}

javafx {
    val javafxVersion: String by extra

    version = javafxVersion
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("de.salocin.ui.Main")
}

dependencies {
    api(project(":packagemanager-android"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.2")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.26")
    implementation(group = "org.controlsfx", name = "controlsfx", version = "11.1.0")
}
