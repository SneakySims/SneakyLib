package net.sneakysims.wallpapermaker

import net.perfectdreams.slippyimage.*
import net.sneakysims.sneakylib.iff.*
import net.sneakysims.sneakylib.sims.TheSimsLanguage

object WallpaperMaker {
    fun createWallpaperIFF(
        name: String,
        price: Int,
        description: String,
        spriteFront: SlippyImage,
        spriteLeft: SlippyImage,
        spriteRight: SlippyImage,
        spriteSide: SlippyImage,
        palette: List<Color>
    ): IFF {
        val iff = IFF.empty()

        iff.addChunk(
            IFFChunk.STR_CHUNK_CODE,
            0,
            // TODO: Why does it need to be 16?
            // IFF Pencil, and the game, only works if this is 16
            16,
            null,
            STRChunkData(
                STRChunkData.StringFormat.StringFormatFDFF(
                    mutableListOf(
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
                            name,
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
                            price.toString(),
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
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
                palette
            )
        )

        iff.addChunk(
            IFFChunk.SPR_CHUNK_CODE,
            1,
            16,
            null,
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(spriteLeft, palette, 16, 67),
                    convertToSPRSprite(spriteRight, palette, 16, 68),
                    convertToSPRSprite(spriteFront, palette, 32, 60),
                    convertToSPRSprite(spriteSide, palette, 4, 58)
                )
            )
        )

        iff.addChunk(
            IFFChunk.SPR_CHUNK_CODE,
            1793,
            16,
            null,
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(spriteLeft, palette, 32, 135),
                    convertToSPRSprite(spriteRight, palette, 32, 136),
                    convertToSPRSprite(spriteFront, palette, 64, 120),
                    convertToSPRSprite(spriteSide, palette, 8, 116)
                )
            )
        )

        iff.addChunk(
            IFFChunk.SPR_CHUNK_CODE,
            2049,
            16,
            null,
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(spriteLeft, palette, 64, 271),
                    convertToSPRSprite(spriteRight, palette, 64, 272),
                    convertToSPRSprite(spriteFront, palette, 128, 240),
                    convertToSPRSprite(spriteSide, palette, 16, 232)
                )
            )
        )

        return iff
    }

    fun createSquishedImageForLateralWallsSprite(sourceImageSquishedForLateralWalls: SlippyImage): SlippyImage {
        return sourceImageSquishedForLateralWalls.getScaledInstance(64, 232)
    }

    fun createWallLeftSprite(sourceImageSquishedForLateralWalls: SlippyImage): SlippyImage {
        val wallLeftBase = SlippyImage.createEmpty(64, 271)

        repeat(32) { // amount of strips
            wallLeftBase.drawImage(
                sourceImageSquishedForLateralWalls,
                it * 2,
                0,
                2,
                sourceImageSquishedForLateralWalls.height,
                0 + (it * 2),
                39 - it
            )
        }

        // Lighten the wall right a bit
        for ((index, argb) in wallLeftBase.pixels.withIndex()) {
            val unpackedColor = ColorUtils.unpackARGB(argb)

            val hsbVals = ColorUtils.RGBtoHSB(
                unpackedColor.red,
                unpackedColor.green,
                unpackedColor.blue,
                null
            )

            val editedRGB = ColorUtils.HSBtoRGB(
                hsbVals[0],
                hsbVals[1],
                (hsbVals[2] + 0.1f).coerceIn(0.0f..1.0f)
            )
            val editedUnpackedRGB =
                ColorUtils.unpackRGB(editedRGB) // The HSBtoRGB does not use alpha

            wallLeftBase.pixels[index] = ColorUtils.packARGB(
                editedUnpackedRGB.red,
                editedUnpackedRGB.green,
                editedUnpackedRGB.blue,
                unpackedColor.alpha
            )
        }

        return wallLeftBase
    }

    fun createWallRightSprite(sourceImageSquishedForLateralWalls: SlippyImage): SlippyImage {
        val wallRightBase = SlippyImage.createEmpty(64, 272)

        // The right wall is a bit different
        wallRightBase.drawImage(
            sourceImageSquishedForLateralWalls,
            0,
            0,
            1,
            sourceImageSquishedForLateralWalls.height,
            0,
            8
        )

        repeat(31) { // amount of strips
            wallRightBase.drawImage(
                sourceImageSquishedForLateralWalls,
                it * 2,
                0,
                2,
                sourceImageSquishedForLateralWalls.height,
                1 + (it * 2),
                9 + it
            )
        }

        wallRightBase.drawImage(
            sourceImageSquishedForLateralWalls,
            63,
            0,
            1,
            sourceImageSquishedForLateralWalls.height,
            63,
            40
        )

        // Darken the wall right a bit
        for ((index, argb) in wallRightBase.pixels.withIndex()) {
            val unpackedColor = ColorUtils.unpackARGB(argb)

            val hsbVals = ColorUtils.RGBtoHSB(
                unpackedColor.red,
                unpackedColor.green,
                unpackedColor.blue,
                null
            )

            val editedRGB = ColorUtils.HSBtoRGB(
                hsbVals[0],
                hsbVals[1],
                (hsbVals[2] - 0.1f).coerceIn(0.0f..1.0f)
            )
            val editedUnpackedRGB =
                ColorUtils.unpackRGB(editedRGB) // The HSBtoRGB does not use alpha

            wallRightBase.pixels[index] = ColorUtils.packARGB(
                editedUnpackedRGB.red,
                editedUnpackedRGB.green,
                editedUnpackedRGB.blue,
                unpackedColor.alpha
            )
        }
        return wallRightBase
    }

    fun createWallFrontSprite(sourceImage: SlippyImage): SlippyImage {
        val frontFacingWallBase = SlippyImage.createEmpty(128, 240)
        val frontFacingWallImage = sourceImage.getScaledInstance(128, 232)
        frontFacingWallBase.drawImage(frontFacingWallImage, 0, 8)
        return frontFacingWallBase
    }

    fun createWallCornerSprite(frontFacingWallImage: SlippyImage): SlippyImage {
        // This seems to be unused by the game, but if this unused side is not present, Homecrafter won't be able to open the "wll"" file!
        val sideWallUnusedBase = SlippyImage.createEmpty(16, 232)
        sideWallUnusedBase.drawImage(frontFacingWallImage, 0, 0, 16, 232, 0, 0)
        return sideWallUnusedBase
    }

    fun convertToSPRSprite(
        image: SlippyImage,
        palette: List<Color>,
        targetWidth: Int,
        targetHeight: Int
    ): SPRChunkData.SPRSprite {
        // This is REALLY *painful*
        val rows = mutableListOf<IntArray>()

        // Don't attempt to scale if the width and height are equal
        val doesNotRequireScaling =
            targetWidth == image.width && targetHeight == targetHeight
        val requiresScaling = !doesNotRequireScaling

        val scaledInstance = if (requiresScaling)
            image.getScaledInstance(targetWidth, targetHeight)
        else
            image

        repeat(targetHeight) { y ->
            val row = IntArray(targetWidth)
            repeat(targetWidth) { x ->
                val argb = scaledInstance.getRGB(x, y)
                val a = (argb shr 24) and 0xFF // Extract alpha
                val r = (argb shr 16) and 0xFF // Extract red
                val g = (argb shr 8) and 0xFF  // Extract green
                val b = argb and 0xFF          // Extract blue

                if (a == 0) {
                    row[x] = SPRChunkData.SPRImageData.TRANSPRENCY_INDEX
                } else {
                    row[x] = palette.indexOf(
                        PaletteCreator.findClosestColor(
                            Color(r, g, b),
                            palette
                        )
                    )
                }
            }

            rows.add(row)
        }

        return SPRChunkData.SPRSprite(
            0,
            0,
            0,
            SPRChunkData.SPRImageData(
                targetWidth.toShort(),
                targetHeight.toShort(),
                rows
            )
        )
    }
}