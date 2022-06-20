plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    kotlin("jvm")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    this.kotlinOptions {
        this.jvmTarget = "17"
    }
}

group = "beauty.shafran"
version = "0.0.1"


dependencies {
    implementation(platform(Dependencies.Spring.bom))
    implementation(Dependencies.Spring.jpaStarter)
    implementation(Dependencies.Spring.webStarter)
    implementation(Dependencies.Kotlin.reflect)
    implementation(Dependencies.Postgres.driver)
    implementation(projects.accountsStarter)
    implementation(projects.companiesStarter)
    implementation(projects.servicesStarter)
    implementation(projects.authStarter)
    implementation(projects.employeesStarter)
}