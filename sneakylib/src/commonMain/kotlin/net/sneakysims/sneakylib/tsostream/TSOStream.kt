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
                println("compressed: ${compressedDataSize}")

                // -4 because of the header
                RefPack.decompress(reader.readBytes(compressedDataSize.toInt() - 4), decompressedDataSize)

                TODO("Compression!")
            }
        }
    }
}