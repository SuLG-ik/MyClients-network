plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}
group = "ru.sulgik.config.processor"
version = "0.0.1"

dependencies {
    implementation(Dependencies.Ksp.core)
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.KotlinPoet.core)
    implementation(Dependencies.KotlinPoet.ksp)
    implementation(projects.configCore)
}