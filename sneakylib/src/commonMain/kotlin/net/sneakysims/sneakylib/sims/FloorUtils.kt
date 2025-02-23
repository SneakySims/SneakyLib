package net.sneakysims.sneakylib.sims

object FloorUtils {
    /**
     * Defines that the floor sound will be a soft step sound when Sims walk over it
     *
     * This character should be at the beginning of the chunk name of the "far" `SPR2` chunk for the floor
     */
    const val SOFT_FLOOR_SOUND_CHUNK_NAME = "S"

    /**
     * Defines that the floor sound will be a medium step sound when Sims walk over it
     *
     * This character should be at the beginning of the chunk name of the "far" `SPR2` chunk for the floor
     */
    const val MEDIUM_FLOOR_SOUND_CHUNK_NAME = "M"

    /**
     * Defines that the floor sound will be a hard step sound when Sims walk over it
     *
     * This character should be at the beginning of the chunk name of the "far" `SPR2` chunk for the floor
     */
    const val HARD_FLOOR_SOUND_CHUNK_NAME = "H"
}