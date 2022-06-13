plugins {
    kotlin("jvm") version Dependencies.Kotlin.version
}

group = "ru.myclients"
version = "0.0.1"

dependencies {
    implementation(Dependencies.Coroutines.core)
}