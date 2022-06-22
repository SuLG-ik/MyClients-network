plugins {
    id("org.springframework.boot")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
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
    implementation(Dependencies.Spring.jpaStarter)
    implementation(Dependencies.Spring.webStarter)
    implementation(Dependencies.Spring.securityStarter)
    implementation(Dependencies.Spring.graphQlStarter)
    implementation(Dependencies.Spring.queryDslCore)
    implementation(Dependencies.Spring.queryDslJpa)
    implementation(projects.auth)
    implementation(projects.accountsStarter)
    implementation(projects.companiesStarter)
    kapt(Dependencies.Spring.queryDslKapt)
}