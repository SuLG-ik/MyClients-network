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
    arg("tables_package", "beauty.shafran.network.companies.schema")
}


dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(projects.auth)
    implementation(projects.companiesData)
    implementation(projects.koinExt)
    implementation(projects.databaseTransactional)
    implementation(projects.exposedExt)
    implementation(projects.pagedData)
    implementation(projects.accountsStarter)
    implementation(projects.accountsData)
    ksp(projects.tableProcessor)
}