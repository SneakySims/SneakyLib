package net.sneakysims.sneakylib.far

import net.sneakysims.sneakylib.tsostream.TSOStream
import net.sneakysims.sneakylib.utils.BinaryUtils
import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class FAR3(val files: MutableList<FAR3File>) {
    companion object {
        const val FAR_HEADER = "FAR!byAZ"

        fun read(byteArray: ByteArray): FAR3 {
            val reader = ByteArrayReader(byteArray)

            val signature = reader.readBytes(8).map { it.toInt().toChar() }.toCharArray().concatToString()

            if (signature != FAR_HEADER)
                error("Invalid FAR file!")

            val version = reader.readIntLe()
            if (version != 3)
                error("Invalid FAR version!")

            // The manifest offset is the byte offset from the beginning of the file to the manifest.
            val manifestOffset = reader.readIntLe()
            println("Signature: $signature")
            println("Version: $version")
            println("Manifest Offset: $manifestOffset")

            // Now we need to jump to the manifest to read the information about the files BEFORE reading information about the files themselves!
            reader.position = manifestOffset
            val numberOfFiles = reader.readIntLe()
            println("Number of Files: $numberOfFiles")

            val files = mutableListOf<FAR3File>()

            repeat(numberOfFiles) {
                val fileLength = reader.readUIntLe()
                val compressedFileLength = reader.readUIntLe()
                val fileOffset = reader.readIntLe()

                val flags = reader.readUShortLe() // Always 0, except in packingslips.dat where it is 1
                val fileNameLength = reader.readShortLe().toInt()
                val typeId = reader.readUIntLe()
                val fileId = reader.readUIntLe()
                val fileNameBytes = reader.readBytes(fileNameLength)
                val fileName = fileNameBytes.map { it.toInt().toChar() }.toCharArray().concatToString()

                val lastManifestOffset = reader.position
                println("decompressedFileLength: $fileLength")
                println("compressedFileLength: $compressedFileLength")
                println("fileOffset: $fileOffset")
                println("flags: $flags")
                println("typeId: $typeId")
                println("fileId: $fileId")
                println("fileName ($fileNameLength): $fileName")

                reader.position = fileOffset

                // If the most significant bit is set, then it means that this is compressed!
                val isCompressed = BinaryUtils.isMostSignificantBitSet(compressedFileLength)

                val fileContent = if (isCompressed) {
                    TSOStream.read(reader.readBytes(BinaryUtils.clearMostSignificantBit(compressedFileLength).toInt()))
                } else {
                    reader.readBytes(fileLength.toInt())
                }

                files.add(FAR3File(fileName, typeId, fileId, fileContent))

                // Revert to where we were before
                reader.position = lastManifestOffset
            }

            return FAR3(files)
        }
    }

    fun write(): ByteArray {
        TODO("FAR3 writing is not implemented yet")
    }
}