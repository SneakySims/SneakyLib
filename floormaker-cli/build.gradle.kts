plugins {
    kotlin("jvm")
    application
}

group = "net.sneakysims.floormaker.cli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sneakylib"))
    implementation(project(":slippyimage"))
    implementation(project(":floormaker-common"))
    implementation("com.github.ajalt.clikt:clikt:5.0.3")
}

application {
    this.mainClass = "net.sneakysims.floormaker.cli.FloorMakerCLIKt"
}