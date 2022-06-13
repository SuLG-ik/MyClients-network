plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}

group = Config.Application.group
version = Config.Application.version

dependencies {
    api(Dependencies.Exposed.core)
    api(Dependencies.Exposed.datetime)
    api(Dependencies.Datetime.core)
    api(projects.tableCore)
}