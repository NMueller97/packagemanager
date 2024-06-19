plugins {
    id("packagemanager.shared")
    application
}

group = "de.salocin"
version = "1.0.0"

application {
    mainClass = "de.salocin.packagemanager.swing.MainKt"
}

repositories {
    mavenCentral()
}

dependencies {
//    api(project(":packagemanager-android"))
//    implementation(kotlin("stdlib"))
//    implementation(kotlin("reflect"))
//    implementation(libs.logger)
//    implementation(libs.coroutines)
}
