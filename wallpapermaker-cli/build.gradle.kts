plugins {
    kotlin("jvm")
    application
}

group = "net.sneakysims.floormaker"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":sneakylib"))
    implementation(project(":slippyimage"))
    implementation(project(":wallpapermaker-common"))
    implementation("com.github.ajalt.clikt:clikt:5.0.3")
}

application {
    this.mainClass = "net.sneakysims.floormaker.cli.FloorMakerCLIKt"
}