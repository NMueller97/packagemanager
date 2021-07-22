plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
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

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation(group = "no.tornado", name = "tornadofx", version = "1.7.20")
}


