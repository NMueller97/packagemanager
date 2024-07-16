plugins {
    kotlin("jvm")
    kotlin("plugin.power-assert")
    id("org.jlleitschuh.gradle.ktlint")
    id("se.solrike.sonarlint")
    id("io.gitlab.arturbosch.detekt")
}

val javaVersion = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0-RC")
    testImplementation(kotlin("test"))
    testImplementation("io.mockk:mockk:1.9.3")
    testRuntimeOnly("net.bytebuddy:byte-buddy:1.14.18")
}

kotlin {
    target {
        version = javaVersion
    }
}

detekt {
    buildUponDefaultConfig = true
    allRules = false
    config.setFrom("$rootDir/detekt.yml")
}

tasks.detekt {
    jvmTarget = javaVersion.majorVersion
}

tasks.test {
    useJUnitPlatform()
}

