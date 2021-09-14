pluginManagement {
    repositories {
        gradlePluginPortal()
    }
    plugins {
        val kotlinVersion: String by extra

        kotlin("jvm") version kotlinVersion
        id("org.openjfx.javafxplugin") version "0.0.9"
    }
}

rootProject.name = "packagemanager"
include("packagemanager-android")
include("packagemanager-ui")
include("packagemanager-utils")
