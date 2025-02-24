package net.sneakysims.sneakylib.utils

object BinaryUtils {
    /**
     * Extracts two bytes from a UShort [value]
     *
     * @param value the input
     * @return a list containing two UBytes
     */
    fun shortToUBytes(value: UShort): List<UByte> {
        return listOf(
            (value.toInt() shr 8 and 0xFF).toUByte(),  // High byte
            (value.toInt() and 0xFF).toUByte()         // Low byte
        )
    }

    fun combineBytesToInt(b1: Byte, b2: Byte, b3: Byte): Int {
        return (b1.toInt() and 0xFF shl 16) or
                (b2.toInt() and 0xFF shl 8) or
                (b3.toInt() and 0xFF)
    }

    // From kotlinx-io
    fun Int.reverseBytes(): Int {
        return (this and -0x1000000 ushr 24) or
                (this and 0x00ff0000 ushr 8) or
                (this and 0x0000ff00 shl 8) or
                (this and 0x000000ff shl 24)
    }

    fun Long.reverseBytes(): Long {
        return (this and -0x100000000000000L ushr 56) or
                (this and 0x00ff000000000000L ushr 40) or
                (this and 0x0000ff0000000000L ushr 24) or
                (this and 0x000000ff00000000L ushr 8) or
                (this and 0x00000000ff000000L shl 8) or
                (this and 0x0000000000ff0000L shl 24) or
                (this and 0x000000000000ff00L shl 40) or
                (this and 0x00000000000000ffL shl 56)
    }

    fun Short.reverseBytes(): Short {
        val i = toInt() and 0xffff
        val reversed = (i and 0xff00 ushr 8) or
                (i and 0x00ff shl 8)
        return reversed.toShort()
    }

    fun isMostSignificantBitSet(value: UInt): Boolean {
        return (value and 0x80000000u) != 0u
    }

    fun clearMostSignificantBit(value: UInt): UInt {
        return value and 0x7FFFFFFFu  // 0x7FFFFFFF masks out the MSB
    }
}