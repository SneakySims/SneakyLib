plugins {
    kotlin("jvm")
}

group = "net.sneakysims.sneakylib"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sneakylib"))
    implementation(project(":slippyimage"))
}