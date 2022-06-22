dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://repo.spring.io/release")
    }
}
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://repo.spring.io/release")
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "MyClients-network"

include("app")

//include(
//    "auth-data",
//    "auth",
//    "auth-starter",
//)
include(
    "accounts-starter",
    "companies-starter",
    "services-starter",
    "sessions-starter",
    "auth-starter",
    "employees-starter",
    "customers-starter",
    "auth",
)
//include(
//
//    "employees-data",
//)
//include(
//    "companies-starter",
//    "companies-data",
//)
//include(
//    "services-starter",
//    "services-data",
//)
//
//include(
//    "logging-starter",
//    "https-redirect-starter",
//    "routing-v1-starter",
//    "serialization-starter",
//    "exceptions-starter",
//    "exposed-starter",
//)
//include(
//    "table-core",
//    "table-processor",
//    "config-core",
//    "config-processor",
//)
//include(
//    "koin-ext",
//    "exposed-ext",
//    "exceptions-data",
//    "paged-data",
//    "datetime-koin",
//    "database-transactional",
//)