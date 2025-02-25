package net.sneakysims.sneakylib.tsostream

import net.sneakysims.sneakylib.refpack.RefPack
import net.sneakysims.sneakylib.utils.BinaryUtils
import net.sneakysims.sneakylib.utils.ByteArrayReader

class TSOStream {
    companion object {
        fun read(byteArray: ByteArray): ByteArray {
            val reader = ByteArrayReader(byteArray)

            val compressionFlag = reader.readByte()

            // Currently, the difference between 1 and 3 is not well understood;
            // it is guessed that 3 means "higher compression has been applied" compared to 1, or vice versa, or that 3 means "probably big-endian" and 1 means "probably little-endian".
            val isCompressed = compressionFlag == 0x01.toByte() || compressionFlag == 0x03.toByte()

            val decompressedDataSize = reader.readUIntLe()

            println("Decompressed data size: $decompressedDataSize")

            if (!isCompressed) {
                return reader.readBytes(decompressedDataSize.toInt())
            } else {
                // This is byte order dependant
                val compressedDataSize = reader.readUIntLe()

                // This is ALWAYS little endian!
                val compressedDataSizeAgain = reader.readUIntLe()

                if (compressedDataSize != compressedDataSizeAgain)
                    error("Mismatch between the stream header compressed data size and the body compressed data size! Something went wrong during the decoding process! Header: $compressedDataSize Body: $compressedDataSizeAgain")

                println("Compressed data size: $compressedDataSize -> $compressedDataSizeAgain")

                println(reader.byteArray.size)
                println("Compressed: ${compressedDataSize}")

                val reader = ByteArrayReader(reader.readBytes(compressedDataSize.toInt() - 4))

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

                val decompressedSize = BinaryUtils.combineBytesToInt(byte0, byte1, byte2)
                println(decompressedSize)

                if (decompressedDataSize != decompressedSize.toUInt())
                    error("Mismatch between the stream header decompressed data size and the RefPack decompressed data size! Something went wrong during the decoding process! Stream Size: $decompressedDataSize RefPack Size: $decompressedSize")

                return RefPack.decompress(reader.readBytes(reader.remaining), decompressedSize)
                // return RefPack.decompress(reader.readBytes(compressedDataSize.toInt() - 4), decompressedDataSize)
                // return reader.readBytes(compressedDataSize.toInt() - 4)
            }
        }
    }
}