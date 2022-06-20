plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("jvm")
    kotlin("kapt")
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    this.kotlinOptions {
        this.jvmTarget = "17"
    }
}



group = "ru.myclients"
version = "0.0.1"

dependencies {
    implementation(platform(Dependencies.Spring.bom))
    implementation(Dependencies.Spring.securityStarter)
}