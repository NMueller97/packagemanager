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
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinCoroutinesVersion")
}


