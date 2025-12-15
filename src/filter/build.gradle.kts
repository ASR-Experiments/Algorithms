plugins {
    kotlin("jvm") version "2.2.0"
    id("org.sonarqube") version "7.2.1.6560"
}

group = "org.asr.example"
version = "0.0.1-SNAPSHOT"

sonar {
    properties {
        property("sonar.projectKey", "asr-experiments_Algorithms_Filter")
        property("sonar.organization", "asr-experiments")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}