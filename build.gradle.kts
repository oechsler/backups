import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val spigotVersion: String by project
val zip4jVersion: String by project

plugins {
    kotlin("jvm") version "1.5.0"

    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "it.oechsler"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("org.spigotmc:spigot-api:$spigotVersion")
    implementation("net.lingala.zip4j:zip4j:$zip4jVersion")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

sourceSets.main {
    java.srcDir("src")
    resources.srcDir("resources")
}

sourceSets.test {
    java.srcDir("test")
    resources.srcDir("testresources")
}



