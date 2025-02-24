package net.sneakysims.sneakylib.refpack

import net.sneakysims.sneakylib.utils.BinaryUtils
import net.sneakysims.sneakylib.utils.ByteArrayReader

/**
 * Tools related to EA Games' RefPack compression
 *
 * https://github.com/riperiperi/FreeSO/blob/master/Other/tools/SimsLib/SimsLib/FAR3/Decompresser.cs
 * https://github.com/gibbed/Gibbed.RefPack
 * http://wiki.niotso.org/RefPack
 */
object RefPack {
    fun decompress(compressed: ByteArray, streamDecompressedSize: UInt) {
        val reader = ByteArrayReader(compressed)

        val flags = reader.readUByte()
        val magicValue = reader.readUByte()

        if (magicValue != 0xFB.toUByte())
            error("Incorrect magic value! Value: $magicValue")

        // The Sims Online understands the "compressed-size-present" but it none of the game files uses it
        // If the first bit of the flag is set, then that means that the compressed size is four bytes instead of three
        // The Sims Online does not use it tho
        // The size format is in big endian
        val byte0 = reader.readByte()
        val byte1 = reader.readByte()
        val byte2 = reader.readByte()

        println("Byte0: $byte0")
        println("Byte1: $byte1")
        println("Byte2: $byte2")

        val decompressedSize = BinaryUtils.combineBytesToInt(byte0, byte1, byte2)
        println(decompressedSize)

        if (streamDecompressedSize != decompressedSize.toUInt())
            error("Mismatch between the stream header decompressed data size and the RefPack decompressed data size! Something went wrong during the decoding process! Stream Size: $streamDecompressedSize RefPack Size: $decompressedSize")

        TODO("Compression!")
    }

    fun isBitSet(input: UByte, bitPosition: Int): Boolean {
        return (input.toInt() and (1 shl bitPosition)) != 0
    }
}