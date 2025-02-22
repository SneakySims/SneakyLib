plugins {
    kotlin("multiplatform")
}

group = "net.sneakysims.sneakylib"
version = "1.0-SNAPSHOT"

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