plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}

group = Config.Application.group
version = Config.Application.version

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Koin.core)
    implementation(projects.authStarter)
    implementation(projects.accountsStarter)
    implementation(projects.companiesStarter)
    implementation(projects.employeesStarter)
    implementation(projects.servicesStarter)
    implementation(projects.configCore)
    ksp(projects.configProcessor)
}