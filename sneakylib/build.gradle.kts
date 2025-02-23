plugins {
    kotlin("multiplatform")
    id("maven-publish")
}

group = "net.sneakysims.sneakylib"

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
                implementation(kotlin("stdlib-common"))
                implementation(project(":slippyimage"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(npm("iconv-lite", "0.6.3"))
                implementation(npm("buffer", "6.0.3")) // iconv-lite needs this
                // Not strictly required but it does have some nice extension methods
                implementation("org.jetbrains.kotlin-wrappers:kotlin-browser:2025.2.5")
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