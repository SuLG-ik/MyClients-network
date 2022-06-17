plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
    kotlin("plugin.serialization") version Dependencies.Kotlin.version
}

group = "ru.myclients"
version = "0.0.1"

dependencies {
    implementation(Dependencies.Ktor.core)
    implementation(Dependencies.Serialization.core)
    implementation(Dependencies.Datetime.core)
    implementation(projects.pagedData)
    implementation(projects.accountsData)
    implementation(projects.companiesData)
    api(projects.exceptionsData)
}