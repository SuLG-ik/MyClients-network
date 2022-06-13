plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    id(Dependencies.Ksp.plugin) version Dependencies.Ksp.version
}

group = "ru.myclients"
version = "0.0.1"

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + "-Xcontext-receivers"
    }
}
ksp {
    arg("tables_package", "beauty.shafran.network.auth.schema")
}

dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Ktor.auth)
    implementation(Dependencies.Ktor.authJwt)
    implementation(Dependencies.BCrypt.mindrotLib)
    implementation(projects.auth)
    implementation(projects.authData)
    implementation(projects.configCore)
    implementation(projects.koinExt)
    implementation(projects.exposedExt)
    implementation(projects.databaseTransactional)
    implementation(projects.accountsStarter)
    ksp(projects.configProcessor)
    ksp(projects.tableProcessor)
}