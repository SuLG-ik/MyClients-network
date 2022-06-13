plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}

group = Config.Application.group
version = Config.Application.version

dependencies {
    implementation(Dependencies.Ksp.core)
    implementation(Dependencies.Exposed.core)
    implementation(Dependencies.KotlinPoet.core)
    implementation(Dependencies.KotlinPoet.ksp)
    implementation(projects.tableCore)
}