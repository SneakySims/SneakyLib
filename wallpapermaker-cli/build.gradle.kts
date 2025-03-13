plugins {
    kotlin("jvm")
    application
}

group = "net.sneakysims.wallpapermaker"

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
    this.mainClass = "net.sneakysims.wallpapermaker.cli.WallpaperMakerCLIKt"
}