dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven("https://jitpack.io")
    }
}
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "MyClients-network"

include("app")

include(
    "auth-data",
    "auth",
    "auth-starter",
)
include(
    "accounts-starter",
    "accounts-data",
)
include(
    "companies-starter",
    "companies-data",
)

include(
    "logging-starter",
    "https-redirect-starter",
    "routing-v1-starter",
    "serialization-starter",
    "exceptions-starter",
    "exposed-starter",
)
include(
    "table-core",
    "table-processor",
    "config-core",
    "config-processor",
)
include(
    "koin-ext",
    "exposed-ext",
    "exceptions-data",
    "paged-data",
    "datetime-koin",
    "database-transactional",
)