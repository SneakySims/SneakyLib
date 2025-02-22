package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class IFF(val chunks: MutableList<net.sneakysims.sneakylib.iff.IFFChunk>) {
    companion object {
        val IFF_FILE_HEADER = "IFF FILE 2.5:TYPE FOLLOWED BY SIZE\u0000 JAMIE DOORNBOS & MAXIS 1"

        /**
         * Creates a empty IFF file
         */
        fun empty(): IFF {
            return IFF(mutableListOf())
        }

        fun read(byteArray: ByteArray): IFF {
            // IFF files are BIG ENDIAN!!
            val reader = ByteArrayReader(byteArray)

            val signatureAsBytes = reader.readBytes(60)
            val signature = signatureAsBytes.decodeToString()

            if (signature != IFF_FILE_HEADER)
                error("Not an IFF file! Signature is $signature while the expected signature is $IFF_FILE_HEADER")

            val resourceMapOffset = reader.readInt()

            println("Resource Map Offset: $resourceMapOffset")

            val chunks = mutableListOf<net.sneakysims.sneakylib.iff.IFFChunk>()

            while (reader.hasRemaining()) {
                val startingPosition = reader.position

                val typeCodeAsBytes = reader.readBytes(4)
                val typeCode = typeCodeAsBytes.decodeToString()

                println("Type Code: $typeCode")

                val size = reader.readInt()
                println("Size: $size")

                val id = reader.readShort()
                println("Chunk ID: $id")
                val flags = reader.readShort()

                // It is mostly garbage, so let's keep it as bytes
                // Also because decoding it as a string seems to be lossy and causes the IFF file to not match!
                val nameStringAsBytes = reader.readBytes(64)

                val diff = reader.position - startingPosition
                val data = reader.readBytes(size - diff)

                // Read the chunk!
                val chunkData = when (typeCode) {
                    net.sneakysims.sneakylib.iff.IFFChunk.PALT_CHUNK_CODE -> PALTChunkData.read(data)
                    net.sneakysims.sneakylib.iff.IFFChunk.STR_CHUNK_CODE -> STRChunkData.read(data)
                    net.sneakysims.sneakylib.iff.IFFChunk.SPR_CHUNK_CODE -> SPRChunkData.read(data)
                    net.sneakysims.sneakylib.iff.IFFChunk.SPR2_CHUNK_CODE -> SPR2ChunkData.read(data)

                    // If we don't know how to parse the chunk, just read as an unknown chunk (which will cause it to be written as-is)
                    else -> net.sneakysims.sneakylib.iff.UnknownChunkData(data)
                }

                val chunk = net.sneakysims.sneakylib.iff.IFFChunk(typeCode, id, flags, nameStringAsBytes, chunkData)

                chunks.add(chunk)
            }

            return IFF(chunks)
        }
    }

    fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeBytes(IFF_FILE_HEADER.encodeToByteArray())

        buffer.writeInt(0) // We'll go back and fix this later

        // TODO: How could we ACTUALLY handle rsmp chunks?
        //  It seems that The Sims doesn't actually care if the rsmp chunks are present, but it would be nice to support them y'know?
        var rsmpOffset: Int? = null

        for (chunk in this.chunks) {
            if (chunk.code == "rsmp") {
                // rsmpOffset = buffer.bytes.size
            }

            if (chunk.code != "rsmp") {
                val data = when (chunk.data) {
                    is PALTChunkData -> chunk.data.write()
                    is STRChunkData -> chunk.data.write()
                    is SPRChunkData -> chunk.data.write()
                    is SPR2ChunkData -> chunk.data.write()
                    is net.sneakysims.sneakylib.iff.UnknownChunkData -> chunk.data.data

                }

                buffer.writeBytes(chunk.code.encodeToByteArray())
                // data size + code size + this + chunk id + chunk flags + chunk name
                buffer.writeInt(data.size + 4 + 4 + 2 + 2 + 64)
                buffer.writeShort(chunk.id)
                buffer.writeShort(chunk.flags)
                buffer.writeBytes(chunk.name)

                buffer.writeBytes(data)
            }
        }

        if (rsmpOffset == null) {
            // error("Missing Resource Map (rsmp) chunk!")
            println("Missing Resource Map (rsmp) chunk! This could go wrong")
        } else {
            // TODO: This should be in the writer
            val byte0 = (rsmpOffset shr 24).toByte()
            val byte1 = (rsmpOffset shr 16).toByte()
            val byte2 = (rsmpOffset shr 8).toByte()
            val byte3 = rsmpOffset.toByte()

            buffer.bytes[60] = byte0
            buffer.bytes[61] = byte1
            buffer.bytes[62] = byte2
            buffer.bytes[63] = byte3
        }

        return buffer.asByteArray()
    }
}