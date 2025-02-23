package net.sneakysims.floormaker

import net.sneakysims.sneakylib.sims.FloorUtils

enum class FloorSound(val chunkPrefix: String) {
    SOFT_FLOOR(FloorUtils.SOFT_FLOOR_SOUND_CHUNK_NAME),
    MEDIUM_FLOOR(FloorUtils.MEDIUM_FLOOR_SOUND_CHUNK_NAME),
    HARD_FLOOR(FloorUtils.HARD_FLOOR_SOUND_CHUNK_NAME)
}