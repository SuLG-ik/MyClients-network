plugins {
    kotlin("jvm") version "1.6.21"
}

group = "beauty.shafran"
version = "0.0.1"

val exposedVersion: String by project

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.21-1.0.5")
    implementation("com.squareup:kotlinpoet:1.11.0")
    implementation("com.squareup:kotlinpoet-ksp:1.11.0")
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation(project(":table-core"))
}