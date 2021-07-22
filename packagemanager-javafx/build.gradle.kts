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
    mainClass.set("de.salocin.Main")
}

dependencies {
    api(project(":packagemanager-android"))

    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "1.7.26")
    implementation(group = "no.tornado", name = "tornadofx", version = "1.7.20")
    implementation(group = "org.controlsfx", name = "controlsfx", version = "11.1.0")
}
