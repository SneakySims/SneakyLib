package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.sims.TheSimsLanguage
import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class STRChunkData(val format: StringFormat) : IFFChunkData() {
    sealed class StringFormat(val formatId: Short) {
        class StringFormatFDFF(val strings: MutableList<SimsString>) : StringFormat(-3) {
            data class SimsString(
                var language: net.sneakysims.sneakylib.sims.TheSimsLanguage,
                var text: String,
                var description: String
            )
        }
    }

    companion object {
        fun read(byteArray: ByteArray): STRChunkData {
            val reader = ByteArrayReader(byteArray)

            val formatId = reader.readShortLe().toInt()

            when (formatId) {
                -3 -> {
                    val strings = mutableListOf<StringFormat.StringFormatFDFF.SimsString>()

                    val count = reader.readUShortLe()

                    repeat(count.toInt()) {
                        val language = net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(reader.readByte())

                        println("Language: $language")

                        val string = language.decodeStringFromIFF(reader.readBytesUntilNull())
                        println("String ééé: $string")

                        val string2 = language.decodeStringFromIFF(reader.readBytesUntilNull())
                        println("String2: $string2")

                        strings.add(
                            StringFormat.StringFormatFDFF.SimsString(
                                language,
                                string,
                                string2
                            )
                        )
                    }

                    return STRChunkData(StringFormat.StringFormatFDFF(strings))
                }

                else -> error("Unsupported string format! $formatId")
            }
        }
    }

    fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeShortLe(format.formatId)

        when (format) {
            is StringFormat.StringFormatFDFF -> {
                buffer.writeUShortLe(format.strings.size.toUShort())

                for (string in format.strings) {
                    buffer.writeByte(string.language.code)

                    println("Using language ${string.language}")
                    buffer.writeBytes(string.language.encodeStringForIFF(string.text))
                    buffer.writeByte(0x00)
                    buffer.writeBytes(string.language.encodeStringForIFF(string.description))
                    buffer.writeByte(0x00)
                }

                // In addition, there is some extra data at the end. In all files observed, the extra data is simply N bytes that all contain the value 0xA3 (163).
                // It's possible that this is a second table containing a third set of strings, which just happen to be always empty.
                // -SimTech

                // In reality, it doesn't seem to always be 0xA3, some files do seem to have different characters, some (like Behavior.iff) do have 0xA3
                // Because it is unused, we'll just keep it as is and carry on, it doesn't seem to affect anything in the game
            }
        }

        return buffer.asByteArray()
    }
}