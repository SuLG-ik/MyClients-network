plugins {
    id("org.springframework.boot") version Dependencies.Spring.version apply false
    kotlin("plugin.spring") version Dependencies.Kotlin.version apply false
    kotlin("plugin.jpa") version Dependencies.Kotlin.version apply false
    kotlin("jvm") version Dependencies.Kotlin.version apply false
    kotlin("kapt") version Dependencies.Kotlin.version apply false
}