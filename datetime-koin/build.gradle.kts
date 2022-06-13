plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}

group = Config.Application.group
version = Config.Application.version

dependencies {
    implementation(projects.koinExt)
    implementation(Dependencies.Datetime.core)
}