plugins {
    kotlin("jvm") version "2.3.0"
    id("application")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "dev.apollointhehouse"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:5.0.1")
    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain(25)
}

application {
    mainClass.set("dev.apollointhehouse.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.apollointhehouse.MainKt"
    }
}

tasks.test {
    useJUnitPlatform()
}

tasks.run {
    group = "application"
    mainClass.set("dev.apollointhehouse.MainKt")
    standardInput = System.`in`
    classpath = sourceSets["main"].runtimeClasspath
}