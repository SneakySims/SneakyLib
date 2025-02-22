package net.sneakysims.sneakylib.iff

import kotlin.test.Test

class SPR2Tester {
    @Test
    fun `test spr2 header packer`() {
        val command = 0x04
        val count = 128

        val packed = SPR2Utils.packSectionHeader(command, count)
        val unpacked = SPR2Utils.unpackSectionHeader(packed.toInt())

        assert(command == unpacked.command)
        assert(count == unpacked.count)
    }

    @Test
    fun `test spr2 header packer 2`() {
        val marker = 12 // Straight from the game's flr files!

        val packed = SPR2Utils.unpackSectionHeader(marker)

        assert(packed.count == 12)
        assert(packed.command == 0)
    }
}