val kotlinCoroutinesVersion: String by extra

plugins {
    kotlin("jvm")
}

kotlin {
    target {
        version = JavaVersion.VERSION_17
    }
}

dependencies {
    api(project(":packagemanager-api"))
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
}


