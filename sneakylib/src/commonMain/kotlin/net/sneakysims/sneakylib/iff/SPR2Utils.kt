package net.sneakysims.sneakylib.iff

object SPR2Utils {
    fun packPixelData(paletteIndex: UByte, alphaBlending: UByte, depthBuffer: UByte): Int {
        return (paletteIndex.toInt() and 0xFF shl 16) or
                (alphaBlending.toInt() and 0xFF shl 8) or
                (depthBuffer.toInt() and 0xFF)
    }

    fun unpackPixelData(packedInt: Int): Triple<UByte, UByte, UByte> {
        val paletteIndex = (packedInt shr 16 and 0xFF).toUByte()
        val alphaBlending = (packedInt shr 8 and 0xFF).toUByte()
        val depthBuffer = (packedInt and 0xFF).toUByte()
        return Triple(paletteIndex, alphaBlending, depthBuffer)
    }

    /**
     * Unpacks the command and count from a SPR2 section header
     *
     * @param input the input
     * @return a header containing the command and the count
     */
    fun unpackSectionHeader(input: Int): SPR2SectionHeader {
        val command = input shr 13
        val count = input and 0x1FFF

        return SPR2SectionHeader(command, count)
    }

    /**
     * Packs a [command] and a [count] into the format used by SPR2's section header marker
     *
     * @param command the command of the header
     * @param count the count of the header
     * @return a UShort section header marker
     */
    fun packSectionHeader(command: Int, count: Int): UShort {
        return ((command shl 13) or (count and 0x1FFF)).toUShort()
    }

    data class SPR2SectionHeader(
        val command: Int,
        val count: Int
    )
}