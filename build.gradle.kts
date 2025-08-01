plugins {
    kotlin("jvm") version "2.2.0"
}

group = "io.arthurkun"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}
tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "failed", "skipped")
    }
}
kotlin {
    jvmToolchain(17)
}