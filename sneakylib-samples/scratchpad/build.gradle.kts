plugins {
    kotlin("jvm")
}

group = "net.sneakysims.sneakylib"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sneakylib"))
    implementation(project(":slippyimage"))
}