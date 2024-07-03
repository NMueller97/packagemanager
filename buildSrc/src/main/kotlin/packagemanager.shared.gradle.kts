plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("se.solrike.sonarlint")
}

group = "de.salocin"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
}

kotlin {
    target {
        version = JavaVersion.VERSION_17
    }
}
