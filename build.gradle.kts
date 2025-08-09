plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.spotless)
}

group = "io.arthurkun"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.coroutines)
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
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
    compilerOptions {
        suppressWarnings = true
    }
}

spotless {
    ratchetFrom("origin/master")
    kotlin {
        target("**/*.kt", "**/*.kts")
        targetExclude("**/build/**/*.kt")
        ktlint(libs.ktlint.core.get().version).editorConfigOverride(
            mapOf("ktlint_standard_annotation" to "disabled"),
        )
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("xml") {
        target("**/*.xml")
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.register<Copy>("installLocalGitHook") {
    from(layout.projectDirectory.file("pre-push"))
    into(layout.projectDirectory.dir(".git/hooks"))
    doLast {
        val hookFile = File(destinationDir, "pre-push")
        hookFile.setExecutable(true, false)
    }
}
tasks.named("build") {
    dependsOn("installLocalGitHook")
}
