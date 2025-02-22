package net.sneakysims.sneakylib.far

import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class FAR1a(val files: MutableList<FARFile>) {
    companion object {
        const val FAR_HEADER = "FAR!byAZ"

        fun read(byteArray: ByteArray): FAR1a {
            val reader = ByteArrayReader(byteArray)

            val signature = reader.readBytes(8).map { it.toInt().toChar() }.toCharArray().concatToString()

            if (signature != FAR_HEADER)
                error("Invalid FAR file!")

            val version = reader.readIntLe()
            if (version != 1)
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

            val files = mutableListOf<FARFile>()

            repeat(numberOfFiles) {
                val fileLength = reader.readIntLe()
                val fileLengthUnused = reader.readIntLe()
                val fileOffset = reader.readIntLe()

                // This is a bit tricky, FAR archives 1a and 1b are the same, with the only difference is that one uses int for the file name length, another uses shorts
                // There isn't a good way of detecting which FAR version we are using... (we don't support 3 yet)
                val fileNameLength = reader.readIntLe()
                val fileNameBytes = reader.readBytes(fileNameLength)
                val fileName = fileNameBytes.map { it.toInt().toChar() }.toCharArray().concatToString()

                val lastManifestOffset = reader.position
                println(fileName)

                reader.position = fileOffset

                val fileContent = reader.readBytes(fileLength)

                files.add(FARFile(fileName, fileContent))

                // Revert to where we were before
                reader.position = lastManifestOffset
            }

            return FAR1a(files)
        }
    }

    fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeBytes(FAR_HEADER.encodeToByteArray())
        buffer.writeIntLe(1)

        // The +4 is due to the file count written before the manifest
        val manifestOffset = buffer.bytes.size + 4 + this.files.sumOf { it.content.size }

        println("Manifest Offset: ${manifestOffset}")

        buffer.writeIntLe(manifestOffset)

        // Write all FAR files (the contents) and annotate where in the archive they are
        val indexToOffset = mutableMapOf<Int, Int>()

        for ((index, file) in this.files.withIndex()) {
            indexToOffset[index] = buffer.bytes.size // hacky!!

            buffer.writeBytes(file.content)
        }

        buffer.writeIntLe(this.files.size)

        // Write manifests
        for ((index, file) in this.files.withIndex()) {
            val fileOffset = indexToOffset[index]!!

            buffer.writeIntLe(file.content.size)
            buffer.writeIntLe(file.content.size)

            buffer.writeIntLe(fileOffset)

            buffer.writeIntLe(file.fileName.length)
            buffer.writeBytes(file.fileName.encodeToByteArray())
        }

        return buffer.asByteArray()
    }
}