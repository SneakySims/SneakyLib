package net.sneakysims.floormaker

import net.perfectdreams.slippyimage.*
import net.sneakysims.sneakylib.iff.*

object FloorMaker {
    fun createFloorIFF(
        name: String,
        price: Int,
        description: String,
        stepSound: FloorSound,
        sprite: SlippyImage,
        palette: List<Color>
    ): IFF {
        val iff = IFF.empty()

        iff.addChunk(
            IFFChunk.STR_CHUNK_CODE,
            0,
            // Why does it need to be 16?
            // IFF Pencil, and the game, only works if this is 16
            16,
            null,
            STRChunkData(
                STRChunkData.StringFormat.StringFormatFDFF(
                    mutableListOf(
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(1),
                            name,
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(1),
                            price.toString(),
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(1),
                            description,
                            "!"
                        )
                    )
                )
            )
        )

        iff.addChunk(
            IFFChunk.PALT_CHUNK_CODE,
            1537,
            0,
            null,
            PALTChunkData(
                1,
                0,
                0,
                palette.map { Color(it.red, it.green, it.blue) }
            )
        )

        iff.addChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            1,
            16,
            "${stepSound.chunkPrefix}R3AG3AB4E",
            SPR2ChunkData(
                1000,
                1537,
                mutableListOf(
                    convertToSPR2Sprite(sprite, 31, 16, palette)
                )
            )
        )

        iff.addChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            257,
            16,
            null,
            SPR2ChunkData(
                1000,
                1537,
                mutableListOf(
                    convertToSPR2Sprite(sprite, 63, 32, palette)
                )
            )
        )

        iff.addChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            513,
            16,
            null,
            SPR2ChunkData(
                1000,
                1537,
                mutableListOf(
                    convertToSPR2Sprite(sprite, 127, 64, palette)
                )
            )
        )

        return iff
    }

    fun createFloorSprite(sourceImage: SlippyImage): SlippyImage {
        val floor = SlippyImage.createEmpty(127, 64)

        val skew = Skew(sourceImage)

        val pixelOffset = 0
        // We fudge the values a bit to get the perfect pixel perfect floor like if the floor was created via HomeCrafter
        // I couldn't get it 100% correct, but this is close enough
        // Maybe in the future it would be cool to reverse engineer HomeCrafter's source code to figure out how the rotation is made
        // But alas, that's outside of my knowledge rn
        // I did try using the "obvious thing" of rotate 45 degrees -> squash, but then the pixels aren't 100% matching and that's a bit sad
        val newImage = skew.setCorners(
            floor,
            pixelOffset + -0.99999f, pixelOffset + 32f,
            pixelOffset + 63f, pixelOffset + -0.5f,
            pixelOffset + 127f, pixelOffset + 32f,
            pixelOffset + 63f, pixelOffset + 63.9999f // anything bigger than this and it will trip over the pixels
        )

        // Now we will manually patch up the bottom 3 pixels because I don't know how I could fix it otherwise
        newImage.drawImage(newImage, 62, 62, 3, 1, 62, 63)

        return newImage
    }

    fun convertToSPR2Sprite(
        image: SlippyImage,
        targetWidth: Int,
        targetHeight: Int,
        palette: List<Color>
    ): SPR2ChunkData.SPRSprite {
        val resizedImage = image.getScaledInstance(targetWidth, targetHeight)
        val rows = mutableListOf<IntArray>()

        repeat(targetHeight) { y ->
            val row = IntArray(targetWidth)
            repeat(targetWidth) { x ->
                val unpacked = ColorUtils.unpackARGB(resizedImage.getRGB(x, y))

                val paletteIndex = palette.indexOf(PaletteCreator.findClosestColor(Color(unpacked.red, unpacked.green, unpacked.blue), palette))

                if (unpacked.alpha != 0) {
                    row[x] = SPR2Utils.packPixelData(
                        paletteIndex.toUByte(),
                        255u,
                        255u,
                    )
                } else {
                    row[x] = SPR2Utils.packPixelData(
                        paletteIndex.toUByte(),
                        0u,
                        0u,
                    )
                }
            }

            rows.add(row)
        }

        return SPR2ChunkData.SPRSprite(
            1,
            0,
            -23645,
            0,
            0,
            0,
            SPR2ChunkData.SPR2ImageData(
                targetWidth.toShort(),
                targetHeight.toShort(),
                rows
            )
        )
    }
}