package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.Color
import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

/**
 * Reads The Sims' IFF `PALT` chunks
 */
class PALTChunkData(
    val unknown: Int,
    val unknown2: Int,
    val unknown3: Int,
    val entries: List<Color>
) : IFFChunkData() {
    companion object {
        fun read(byteArray: ByteArray): PALTChunkData {
            val reader = ByteArrayReader(byteArray)

            val unknown = reader.readIntLe() // Version or count, always one

            val paletteEntriesCount = reader.readIntLe()

            val unknown2 = reader.readIntLe()
            val unknown3 = reader.readIntLe()

            val entries = mutableListOf<Color>()

            repeat(paletteEntriesCount) {
                val red = reader.readUByte()
                val green = reader.readUByte()
                val blue = reader.readUByte()

                entries.add(Color(red, green, blue))
            }

            return PALTChunkData(unknown, unknown2, unknown3, entries)
        }
    }

    fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeIntLe(this.unknown)
        buffer.writeIntLe(this.entries.size)
        buffer.writeIntLe(this.unknown2)
        buffer.writeIntLe(this.unknown3)

        for (entry in this.entries) {
            buffer.writeUByte(entry.red)
            buffer.writeUByte(entry.green)
            buffer.writeUByte(entry.blue)
        }

        return buffer.asByteArray()
    }
}