plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}
sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

group = "ru.myclients"
version = "0.0.1"

dependencies {
    implementation(Dependencies.Koin.core)
    implementation(Dependencies.Ktor.core)
    implementation(projects.configCore)
    ksp(projects.configProcessor)
    implementation(Dependencies.Exposed.core)
    implementation(Dependencies.Exposed.jdbc)
    implementation(Dependencies.Postgres.driver)
    implementation(projects.databaseTransactional)
}