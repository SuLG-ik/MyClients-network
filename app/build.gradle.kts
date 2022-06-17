plugins {
    application
    id(Dependencies.Shadow.plugin) version Dependencies.Shadow.version
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}

tasks.withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>() {
    archiveBaseName.set("network")
    archiveClassifier.set("")
    archiveVersion.set("")
}

application {
    mainClass.set("beauty.shafran.ApplicationKt")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}

group = "beauty.shafran"
version = "0.0.1"


dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.netty)
    implementation(Dependencies.Logback.classic)
    implementation(projects.koinExt)
    implementation(projects.loggingStarter)
    implementation(projects.httpsRedirectStarter)
    implementation(projects.serializationStarter)
    implementation(projects.exceptionsStarter)
    implementation(projects.routingV1Starter)
    implementation(projects.exposedStarter)
    implementation(projects.datetimeKoin)
    implementation(projects.authStarter)
    implementation(projects.accountsStarter)
    implementation(projects.companiesStarter)
    implementation(projects.employeesStarter)
    implementation(projects.servicesStarter)
}