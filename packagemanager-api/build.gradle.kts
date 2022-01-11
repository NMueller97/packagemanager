plugins {
    kotlin("jvm")
}

kotlin {
    target {
        version = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}


