plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

group = "net.sneakysims.wallpapermaker"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    jvm {
        withJava()
    }

    js(IR) {
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":sneakylib"))
                implementation(project(":slippyimage"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

publishing {
    repositories {
        maven {
            name = "PerfectDreams"
            url = uri("https://repo.perfectdreams.net/")
            credentials(PasswordCredentials::class)
        }
    }
}