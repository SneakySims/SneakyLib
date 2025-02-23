plugins {
    kotlin("multiplatform") version "2.1.10" apply false
}

group = "net.sneakysims.sneakylib"
version = "1.0-SNAPSHOT"

val versionShortGitHash = getShortGitHash()

repositories {
    mavenCentral()
}

allprojects {
    // We use the git hash for now because everything here is still under construction and very hacky
    // And using snapshot versions sucks when trying to get a specific snapshot version, hashes you just look up
    // and go on with your life
    this.version = "0.0.0-$versionShortGitHash"
}

fun getShortGitHash(): String {
    val process = ProcessBuilder("git", "rev-parse", "--short", "HEAD")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .start()
    return process.inputStream.bufferedReader().readText().trim()
}