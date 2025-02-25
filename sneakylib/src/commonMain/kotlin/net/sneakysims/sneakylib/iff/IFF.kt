package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class IFF(val chunks: MutableList<IFFChunk>) {
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

            val chunks = mutableListOf<IFFChunk>()

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

                // Read the chunk!
                val diff = reader.position - startingPosition
                val data = reader.readBytes(size - diff)

                val chunk = IFFChunk(typeCode, id, flags, nameStringAsBytes, data)

                chunks.add(chunk)
            }

            return IFF(chunks)
        }
    }

    /**
     * Adds a chunk to this IFF file
     *
     * @param code  the chunk code (example: `SPR2`)
     * @param id    the ID of the chunk, IDs should be unique for each code
     * @param flags the chunk flag, currently it is unknown what are the flags meanings,
     *  the game uses 0 and 16 as flags and if you don't use the correct one the chunk
     *  isn't loaded by the game
     * @param name  the name of the chunk, can be null, some attributes in the game are
     *  controlled by the chunk name, like floor step sounds
     * @param data  the data of the chunk
     */
    fun addChunk(code: String, id: Short, flags: Short, name: String?, data: IFFChunkData) {
        this.chunks.add(
            IFFChunk(
                code,
                id,
                flags,
                name?.let { IFFChunkUtils.createChunkName(it) } ?: IFFChunkUtils.EMPTY_CHUNK_NAME,
                data.write()
            )
        )
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
                buffer.writeBytes(chunk.code.encodeToByteArray())
                // data size + code size + this + chunk id + chunk flags + chunk name
                buffer.writeInt(chunk.data.size + 4 + 4 + 2 + 2 + 64)
                buffer.writeShort(chunk.id)
                buffer.writeShort(chunk.flags)
                buffer.writeBytes(chunk.name)

                buffer.writeBytes(chunk.data)
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