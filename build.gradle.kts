plugins {
    kotlin("jvm") version "1.6.0" apply false
    id("org.openjfx.javafxplugin") version "0.0.9" apply false
}

allprojects {
    group = "de.salocin"
    version = "1.0.0"
}

subprojects {
    repositories {
        mavenCentral()
    }
}
