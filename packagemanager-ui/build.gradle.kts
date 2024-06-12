val kotlinCoroutinesVersion: String by extra

plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    application
}

kotlin {
    target {
        version = JavaVersion.VERSION_17
    }
}

javafx {
    val javafxVersion: String by extra
    version = javafxVersion
    modules = listOf("javafx.controls")
}

application {
    mainClass.set("de.salocin.ui.PackageManagerApplicationKt")
}

dependencies {
    api(project(":packagemanager-android"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.slf4j:slf4j-simple:1.7.32")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$kotlinCoroutinesVersion")
}
