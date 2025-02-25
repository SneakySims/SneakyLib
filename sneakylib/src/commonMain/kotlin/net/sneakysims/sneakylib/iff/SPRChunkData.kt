package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.utils.BinaryUtils.reverseBytes
import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class SPRChunkData(
    var version: Int,
    var paletteId: Int,
    val sprites: MutableList<SPRSprite>,
) : IFFChunkData() {
    class SPRSprite(
        val unknown0: Short, // Always 0
        val unknown1: Short, // Always 0
        // var height: Short,
        // var width: Short,
        val unknown2: Short,
        val imageData: SPRImageData
    )

    class SPRImageData(
        val width: Short,
        val height: Short,
        /**
         * The image data, each image row is a array
         */
        val data: List<IntArray>
    ) {
        companion object {
            /**
             * The index that indicates transparency in the image data
             *
             * This is an implementation detail, The Sims does not use this index, the decoder/encodes this index
             */
            val TRANSPRENCY_INDEX = Int.MIN_VALUE
        }

        // TODO: Add JVM extensions to set a java.awt.BufferedImage as the image data?
        // TODO: Add a setColor(x, y, paletteIndex)
    }

    companion object {
        fun read(byteArray: ByteArray): SPRChunkData {
            val byteBuffer = ByteArrayReader(byteArray)

            // TODO: We need to support big endian here, the game supports both
            val version = byteBuffer.readIntLe()

            println("Version: $version")

            val spriteCount = byteBuffer.readIntLe()

            val paletteId = byteBuffer.readIntLe()
            println("fc: $spriteCount")
            println("pal id: $paletteId")
            repeat(spriteCount) {
                val offsetTable = byteBuffer.readIntLe()

                println("offset table: $offsetTable")
            }

            val sprites = mutableListOf<SPRSprite>()

            repeat(spriteCount) {
                // TODO: Add offset table validation, check if all sprites match the entry in the offset table
                println("Parsing Sprite $it")

                println(byteBuffer.position)
                // Version 1001 has +2 fields here: Version and Size

                val unknown0 = byteBuffer.readShortLe() // Always 0
                val unknown1 = byteBuffer.readShortLe() // Always 0
                val height = byteBuffer.readShortLe()
                val width = byteBuffer.readShortLe()
                val unknown2 = byteBuffer.readShortLe()

                println("Width: $width; Height: $height")

                // val image = BufferedImage(width.toInt(), height.toInt(), BufferedImage.TYPE_INT_ARGB)
                // val graphics = image.createGraphics()

                val imageDataRows = mutableListOf<IntArray>()

                while (true) {
                    val encoding = byteBuffer.readByte()
                    val count = byteBuffer.readUByte()

                    println("Encoding: $encoding")
                    println("Count: $count")

                    // This is a horizontal row
                    when (encoding) {
                        0x09.toByte() -> {
                            println("Empty Horizontal Row: Repeat $count x $width")
                            repeat(count.toInt()) {
                                // In other words, the entirety of the next N rows are transparent.
                                // There are no row segments associated with this encoding, so the next row header occurs immediately.
                                // -SimTech
                                imageDataRows.add(IntArray(width.toInt()) { SPRImageData.TRANSPRENCY_INDEX })
                            }
                        }

                        0x04.toByte() -> {
                            val imageDataRow = IntArray(width.toInt())
                            var imageDataIndex = 0

                            val sectionBytes = byteBuffer.readBytes(count.toInt() - 2) // -2 because this includes the encoding + header
                            val section = ByteArrayReader(sectionBytes)

                            // This is a bit confusing but not impossible!
                            while (section.hasRemaining()) {
                                val formatCode = section.readByte()
                                val pixelCount = section.readUByte()

                                println("-- Format Code: $formatCode")
                                println("-- Pixel Count: $pixelCount")
                                println("-- Remaining bytes: ${section.remaining}")

                                when (formatCode) {
                                    0x01.toByte() -> {
                                        // Code one has no pixel data. The pixel count is the number of pixels that are transparent (show the background).
                                        repeat(pixelCount.toInt()) {
                                            imageDataRow[imageDataIndex++] = SPRImageData.TRANSPRENCY_INDEX
                                        }
                                    }

                                    0x02.toByte() -> {
                                        // Code two has two bytes of pixel data. The first byte is the palette color index to fill into the next number of pixels.
                                        // The second byte is always the same value as the first byte; it is apparently unused: tests show that the second color index
                                        // is not alternated with the first, for example.
                                        val paletteColorIndex = section.readUByte()
                                        val unknown = section.readUByte()

                                        println("Palette Color Index: $paletteColorIndex")
                                        println("Unknown: $unknown")

                                        if (paletteColorIndex != unknown)
                                            error("Palette Color Index is not the same as the unknown value! Something went wrong during the decoding process! Palette Color Index: $paletteColorIndex, Unknown: $unknown")

                                        repeat(pixelCount.toInt()) {
                                            imageDataRow[imageDataIndex++] = paletteColorIndex.toInt()
                                        }
                                    }

                                    0x03.toByte() -> {
                                        // Code three has one byte of data per pixel, the palette color index. If the pixel count is odd, an alignment byte with value zero pads the length to even.
                                        repeat(pixelCount.toInt()) {
                                            val paletteColorIndex = section.readUByte()
                                            imageDataRow[imageDataIndex++] = paletteColorIndex.toInt()
                                        }

                                        val isOdd = pixelCount.toInt() % 2 == 1
                                        if (isOdd) {
                                            println("is odd!")
                                            val padding = section.readUByte()
                                            if (padding != 0.toUByte())
                                                error("Padding is not 0! Something went wrong during the decoding process! Padding value: $padding")
                                        }
                                    }

                                    else -> error("Unknown format code! $formatCode")
                                }
                            }

                            imageDataRows.add(imageDataRow)
                        }

                        // The end marker contains the value 0x05 in the first byte and the second byte is zero.
                        0x05.toByte() -> {
                            if (count == 0x00.toUByte()) {
                                println("Finished!")
                                if (imageDataRows.size != height.toInt())
                                    error("Image data rows and height mismatch! Something went wrong during the decoding process! We have ${imageDataRows.size} rows, but the image height is $height!")
                                sprites.add(
                                    SPRSprite(
                                        unknown0,
                                        unknown1,
                                        unknown2,
                                        SPRImageData(width, height, imageDataRows)
                                    )
                                )
                                // ImageIO.write(image, "png", File("spr$it.png"))
                                return@repeat
                            } else {
                                error("Something went wrong during the decoding process! Expected end marker, but the second byte is not 0x00!")
                            }
                        }

                        else -> error("Unknown encoding! $encoding")
                    }
                }
            }

            return SPRChunkData(version, paletteId, sprites)
        }
    }

    override fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeIntLe(this.version) // 0..3
        buffer.writeIntLe(this.sprites.size) // 4..7
        buffer.writeIntLe(this.paletteId) // 8..11

        // Technically The Sims Online's SPR# table does NOT have a offset table (version 1001)
        repeat(this.sprites.size) {
            buffer.writeIntLe(0) // We will come back to here later to fill this up
        }

        val offsetTable = mutableMapOf<Int, Int>()

        for ((index, sprite) in this.sprites.withIndex()) {
            offsetTable[index] = buffer.bytes.size

            buffer.writeShortLe(sprite.unknown0)
            buffer.writeShortLe(sprite.unknown1)
            buffer.writeShortLe(sprite.imageData.height)
            buffer.writeShortLe(sprite.imageData.width)
            buffer.writeShortLe(sprite.unknown2)

            // TODO: This encoder is not *that* good yet
            //  Example: The empty row write could be optimized
            for (row in sprite.imageData.data) {
                val isThisRowFullyEmpty = row.all { it == SPRImageData.TRANSPRENCY_INDEX }

                // This row is fully empty,
                if (isThisRowFullyEmpty) {
                    // Row Header
                    buffer.writeByte(0x09) // Empty row
                    buffer.writeByte(0x01) // Only one row, we could optimize this later by doing an "are all next rows empty?" check
                } else {
                    // We will create a new buffer because we will need to get the section size here
                    val pixelCommandBuffer = ByteArrayWriter()
                    pixelCommandBuffer.writeByte(0x03) // We want to write palette color indexes
                    pixelCommandBuffer.writeUByte(row.size.toUByte()) // The entire row size INCLUDING the header
                    for (paletteIndex in row) {
                        // TODO: This WILL BREAK due to transparency!! Properly implement transparency later!!
                        pixelCommandBuffer.writeUByte(paletteIndex.toUByte())
                    }
                    val isOdd = row.size % 2 == 1
                    if (isOdd) {
                        pixelCommandBuffer.writeByte(0x00) // Padding if odd
                    }

                    // Row Header
                    buffer.writeByte(0x04) // Pixel command
                    buffer.writeUByte((pixelCommandBuffer.bytes.size + 2).toUByte()) // Section size - We do +2 because this includes this and the command
                    buffer.writeBytes(pixelCommandBuffer.asByteArray())
                }
            }

            // This sprite is JOEVER
            buffer.writeByte(0x05)
            buffer.writeByte(0x00)
        }

        // And now we will write the offset table!
        var position = 12
        repeat(this.sprites.size) {
            val offset = offsetTable[it]!!.reverseBytes()

            // ooo, hacky!!
            val byte0 = (offset shr 24).toByte()
            val byte1 = (offset shr 16).toByte()
            val byte2 = (offset shr 8).toByte()
            val byte3 = offset.toByte()

            buffer.bytes[position++] = byte0
            buffer.bytes[position++] = byte1
            buffer.bytes[position++] = byte2
            buffer.bytes[position++] = byte3
        }

        return buffer.asByteArray()
    }
}