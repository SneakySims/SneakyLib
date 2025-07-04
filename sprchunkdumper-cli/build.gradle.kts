plugins {
    kotlin("jvm")
    application
}

group = "net.sneakysims.sprchunkdumper"

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
    this.mainClass = "net.sneakysims.sprchunkdumper.cli.SPRChunkDumperCLIKt"
}