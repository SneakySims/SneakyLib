plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "SneakyLib"

include(":harmonymath")
include(":slippyimage")
include(":sneakylib")
include(":sneakylib-samples:scratchpad")
include(":floormaker-common")
include(":floormaker-cli")