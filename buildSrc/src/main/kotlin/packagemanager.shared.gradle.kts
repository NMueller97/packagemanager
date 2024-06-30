plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("se.solrike.sonarlint")
    id("org.sonarqube")
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

sonarqube {
    properties {
        property("sonar.projectName", "Package Manager")
        property("sonar.projectKey", "package-manager")
    }
}
