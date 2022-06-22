plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("jvm")
    kotlin("kapt")
}

group = "ru.myclients"
version = "0.0.1"


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    this.kotlinOptions {
        this.jvmTarget = "17"
    }
}



dependencies {
    implementation(platform(Dependencies.Spring.bom))
    implementation(Dependencies.Spring.jpaStarter)
    implementation(Dependencies.Spring.webStarter)
    implementation(Dependencies.Spring.securityStarter)
    implementation(Dependencies.Datetime.core)
    implementation(Dependencies.Auth0.jwt)
    implementation(projects.accountsStarter)
    implementation(projects.auth)
}