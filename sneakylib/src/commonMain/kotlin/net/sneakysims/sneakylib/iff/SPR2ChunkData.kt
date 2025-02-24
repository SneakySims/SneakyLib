package net.sneakysims.sneakylib.iff

import net.sneakysims.sneakylib.iff.SPRChunkData.SPRImageData
import net.sneakysims.sneakylib.utils.BinaryUtils
import net.sneakysims.sneakylib.utils.BinaryUtils.reverseBytes
import net.sneakysims.sneakylib.utils.ByteArrayReader
import net.sneakysims.sneakylib.utils.ByteArrayWriter

class SPR2ChunkData(
    var version: Int,
    var paletteId: Int,
    val sprites: List<SPRSprite>
) : IFFChunkData() {
    // TODO: Add "UnparsedSprite" or something like that
    class SPRSprite(
        val flags: Short,
        val unknown: Short,
        val overridenPaletteId: Short,
        val transparentPixelPaletteIndexId: Short,
        val offsetY: Short,
        val offsetX: Short,
        val imageData: SPR2ImageData
    )

    class SPR2ImageData(
        val width: Short,
        val height: Short,
        /**
         * The image data, each image row is a IntArray
         *
         * The integer is packed, with the following format:
         *
         * bit 0: palette index
         * bit 1: alpha blending
         * bit 2: depth buffer
         * the rest is unused
         */
        val data: List<IntArray>
    )

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        fun read(byteArray: ByteArray): SPR2ChunkData {
            val byteBuffer = ByteArrayReader(byteArray)

            // TODO: We need to support big endian here, the game supports both
            val version = byteBuffer.readIntLe()

            if (version != 1000)
                error("Unexpected SPR2 version!")

            val spriteCount = byteBuffer.readIntLe()
            val paletteId = byteBuffer.readIntLe()

            val offsetTable = mutableMapOf<Int, Int>()

            repeat(spriteCount) {
                val offset = byteBuffer.readIntLe()

                println("offset: $offset")

                offsetTable[it] = offset
            }

            val sprites = mutableListOf<SPRSprite>()

            repeat(spriteCount) {
                // Are we where we should be?
                val startPosition = byteBuffer.position

                val width = byteBuffer.readShortLe()
                val height = byteBuffer.readShortLe()
                val flags = byteBuffer.readShortLe()
                val unknown = byteBuffer.readShortLe()

                println("Width: $width")
                println("Height: $height")
                println("Flags: $flags")
                println("Unknown: $unknown")
                if (unknown.toInt() == -23645) {
                    println("Skipping sprite $it because the unknown bit is -23645... Are you trying to parse floors.iff?")
                    sprites.add(SPRSprite(-1, -1, -1, -1, -1, -1, SPR2ImageData(-1, -1, listOf())))
                    return@repeat
                }

                if (startPosition != offsetTable[it])
                    error("Something went wrong while trying to read the SPR2 chunk! The offset table says that we should have been at ${offsetTable[it]} but we are at ${byteBuffer.position}! Something went wrong during the decoding process!")

                val overridenPaletteId = byteBuffer.readShortLe()
                val transparentPixelPaletteId = byteBuffer.readShortLe()
                val offsetY = byteBuffer.readShortLe()
                val offsetX = byteBuffer.readShortLe()
                println("overridenPaletteId: $overridenPaletteId")
                println("transparentPixel: $transparentPixelPaletteId")
                println("OffsetX: $offsetX")
                println("OffsetY: $offsetY")

                val imageDataRows = mutableListOf<IntArray>()

                while (true) {
                    println("Reading sprite...")
                    // This is a bit of a pain to parse compared to SPR#, but let's go!
                    // If you want to learn about how SPR2 works, I recommend trying to write a SPR# parser first
                    // Because SPR# is easier to understand and to reason about, and then SPR2 becomes a bit easier
                    // because SPR2 is like SPR# but "better"
                    // We read each byte manually because we need them for the end marker check
                    val markerUShort = byteBuffer.readUShortLe()
                    val marker = markerUShort.toInt()
                    println("Marker is $markerUShort")

                    val (byte0, byte1) = BinaryUtils.shortToUBytes(markerUShort)

                    val (command, count) = SPR2Utils.unpackSectionHeader(marker)

                    println("Command: $command")
                    println("Count: $count")
                    println("Byte0: ${byte0.toUByte()}")
                    println("Byte1: ${byte1.toUByte()}")

                    when (command) {
                        0x04 -> {
                            // Fill with background
                            repeat(count.toInt()) {
                                // In other words, the entirety of the next N rows are transparent.
                                // There are no row segments associated with this encoding, so the next row header occurs immediately.
                                // -SimTech
                                imageDataRows.add(IntArray(width.toInt()) { SPRImageData.TRANSPRENCY_INDEX })
                            }
                        }

                        0x00 -> {
                            val imageDataRow = IntArray(width.toInt())
                            var imageDataIndex = 0

                            val sectionBytes = byteBuffer.readBytes(count.toInt() - 2) // -2 because this includes the encoding + header
                            val section = ByteArrayReader(sectionBytes)

                            // This is a bit confusing (more confusing than SPR#...) but not impossible!
                            while (section.hasRemaining()) {
                                // We we will do it all over again!
                                val marker = section.readUShortLe().toInt()
                                val formatCode = marker shr 13
                                val pixelCount = marker and 0x1FFF

                                println("-- Format Code: $formatCode")
                                println("-- Pixel Count: $pixelCount")
                                println("-- Remaining bytes: ${section.remaining}")

                                when (formatCode) {
                                    0x01 -> {
                                        // Code one has two bytes of data per pixel, the depth and the palette color index.
                                        repeat(pixelCount) {
                                            val depth = section.readUByte()
                                            val paletteColorIndex = section.readUByte()

                                            imageDataRow[imageDataIndex++] = SPR2Utils.packPixelData(paletteColorIndex, 255u, depth)
                                        }
                                    }

                                    0x02 -> {
                                        // Code two has three bytes of data per pixel, the depth, the palette color index, and the alpha blending.
                                        // If the pixel count is odd, an alignment byte with value 0xB0 pads the length to even.
                                        repeat(pixelCount) {
                                            val depth = section.readUByte()
                                            val paletteColorIndex = section.readUByte()
                                            val alphaBlending = section.readUByte()

                                            imageDataRow[imageDataIndex++] = SPR2Utils.packPixelData(paletteColorIndex, alphaBlending, depth)
                                        }

                                        val isOdd = pixelCount % 2 == 1
                                        if (isOdd) {
                                            println("is odd!")
                                            val padding = section.readUByte()
                                            if (padding != 0xB0.toUByte())
                                                error("Padding is not 0xB0! Something went wrong during the decoding process! Padding value: $padding; Pixel Count: $pixelCount; Position: ${section.position}; Section Bytes: ${sectionBytes.toHexString(HexFormat.UpperCase)}")
                                        }
                                    }

                                    0x03 -> {
                                        // Code three has no pixel data. The pixel count is the number of pixels that are transparent (show the background).
                                        repeat(pixelCount) {
                                            imageDataRow[imageDataIndex++] = SPR2Utils.packPixelData(0u, 0u, 255u)
                                        }
                                    }

                                    0x06 -> {
                                        // Code six has one byte of data per pixel, the palette color index. If the pixel count is odd, an alignment byte with value 0xB0 pads the length to even.
                                        repeat(pixelCount) {
                                            val paletteColorIndex = section.readUByte()
                                            imageDataRow[imageDataIndex++] = SPR2Utils.packPixelData(paletteColorIndex, 255u, 255u)
                                        }

                                        val isOdd = pixelCount % 2 == 1
                                        if (isOdd) {
                                            println("is odd!")
                                            val padding = section.readUByte()
                                            if (padding != 0xB0.toUByte())
                                                error("Padding is not 0xB0! Something went wrong during the decoding process! Padding value: $padding; Pixel Count: $pixelCount; Position: ${section.position}; Section Bytes: ${sectionBytes.toHexString(HexFormat.UpperCase)}")
                                        }
                                    }

                                    else -> error("Unknown format code! $formatCode")
                                }
                            }

                            println("Adding row...")
                            imageDataRows.add(imageDataRow)
                        }

                        else -> {
                            if (byte0 == 0xA0.toUByte()) {
                                if (byte1 == 0x00.toUByte()) {
                                    println("Finished!")
                                    if (imageDataRows.size != height.toInt())
                                        error("Image data rows and height mismatch! Something went wrong during the decoding process! We have ${imageDataRows.size} rows, but the image height is $height!")

                                    sprites.add(SPRSprite(flags, unknown, overridenPaletteId, transparentPixelPaletteId, offsetY, offsetX, SPR2ImageData(width, height, imageDataRows)))
                                    // ImageIO.write(image, "png", File("spr$it.png"))
                                    return@repeat
                                } else {
                                    error("Something went wrong during the decoding process! Expected end marker, but the second byte is not 0x00!")
                                }
                            } else {
                                error("Unknown command $command!")
                            }
                        }
                    }
                }
            }

            if (sprites.size != spriteCount)
                error("Something went wrong during the decoding process! Didn't parse all sprites, expected ${spriteCount} but parsed only ${sprites.size}")

            return SPR2ChunkData(version, paletteId, sprites)
        }
    }

    fun write(): ByteArray {
        val buffer = ByteArrayWriter()

        buffer.writeIntLe(this.version) // 0..3
        buffer.writeIntLe(this.sprites.size) // 4..7
        buffer.writeIntLe(this.paletteId) // 8..11

        // I think that The Sims Online's SPR2 table also does NOT have an offset table but don't quote me on that
        repeat(this.sprites.size) {
            buffer.writeIntLe(0) // We will come back to here later to fill this up
        }

        val offsetTable = mutableMapOf<Int, Int>()

        for ((index, sprite) in this.sprites.withIndex()) {
            offsetTable[index] = buffer.bytes.size

            buffer.writeShortLe(sprite.imageData.width)
            buffer.writeShortLe(sprite.imageData.height)
            buffer.writeShortLe(sprite.flags)
            buffer.writeShortLe(sprite.unknown)
            buffer.writeShortLe(sprite.overridenPaletteId)
            buffer.writeShortLe(sprite.transparentPixelPaletteIndexId)
            buffer.writeShortLe(sprite.offsetY)
            buffer.writeShortLe(sprite.offsetX)

            // TODO: This encoder is not *that* good yet
            //  Example: The empty row write could be optimized
            //  Currently we write EVERY pixel instead of using SPR2 compression
            for (row in sprite.imageData.data) {
                // We will create a new buffer because we will need to get the section size here
                val pixelCommandBuffer = ByteArrayWriter()

                // We want to write palette color indexes
                // The header is the command + the amount of pixels in this row (so, if we are using command 0x02 with 8 pixels, that would be 8 * 3 bytes, but the amount
                // in the header would still be 8)

                // TODO: TEST THIS IN THE SIMS COMPLETE COLLECTION EVERY TIME YOU CHANGE THIS CODE
                // It looks like Complete Collection is way pickier about what you are doing compared to Legacy Collection or even HomeCrafter
                // When I was trying to use the 0x02 command (depth buffer + palette + alpha) the floor was completely transparent in Complete Collection!?!
                // It looks like floors in The Sims Complete Collection (not in HomeCrafter, heck, not even in The Sims Legacy Collection!) HATE SPR2 that uses any pixel command except 0x06 and 0x03
                // So this is very wonky, but it is what it is

                // TODO: Create "SPR2 encoder options"
                for (packedData in row) {
                    val pixelData = SPR2Utils.unpackPixelData(packedData)

                    if (pixelData.alphaBlending == 0u.toUByte()) {
                        pixelCommandBuffer.writeUShortLe(SPR2Utils.packSectionHeader(0x03, 1))
                    } else {
                        pixelCommandBuffer.writeUShortLe(SPR2Utils.packSectionHeader(0x06, 1))
                        pixelCommandBuffer.writeUByte(pixelData.paletteIndex)

                        // It is always odd
                        pixelCommandBuffer.writeUByte(176u) // Padding if odd
                    }
                }

                // Row Header
                buffer.writeUShortLe(SPR2Utils.packSectionHeader(0x00, pixelCommandBuffer.bytes.size + 2)) // Section size - We do +2 because this includes this and the command
                buffer.writeBytes(pixelCommandBuffer.asByteArray())
            }

            // This sprite is JOEVER
            buffer.writeUByte(0u)
            buffer.writeUByte(160u)
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