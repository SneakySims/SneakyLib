import net.perfectdreams.slippyimage.*
import net.sneakysims.sneakylib.iff.*
import net.sneakysims.sneakylib.sims.FloorUtils
import net.sneakysims.sneakylib.utils.encodeToByteArrayUsingWindows1252
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val sourceImage = SlippyImage.convertToSlippyImage(ImageIO.read(File("furalha.png")))
    val floor = createFloor(sourceImage)

    val iff = IFF.empty()

    // iff.chunks.addAll(originalIff.chunks.filter { it.code == "STR#" })
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
                        "Loritta is so cute!!",
                        "!"
                    ),
                    STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                        net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(1),
                        "1",
                        "!"
                    ),
                    STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                        net.sneakysims.sneakylib.sims.TheSimsLanguage.getLanguageById(1),
                        "hewwo",
                        "!"
                    )
                )
            )
        )
    )

    val colors = PaletteCreator.extractColors(sourceImage)
    val palette = PaletteCreator.kMeansQuantization(colors, 256)
    println("Palette Size is ${palette.size}")

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

    fun convertToSPR2Sprite(image: SlippyImage, targetWidth: Int, targetHeight: Int): SPR2ChunkData.SPRSprite {
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

    iff.addChunk(
        IFFChunk.SPR2_CHUNK_CODE,
        1,
        16,
        "${FloorUtils.SOFT_FLOOR_SOUND_CHUNK_NAME}R3AG3AB4E",
        SPR2ChunkData(
            1000,
            1537,
            mutableListOf(
                convertToSPR2Sprite(floor, 31, 16)
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
                convertToSPR2Sprite(floor, 63, 32)
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
                convertToSPR2Sprite(floor, 127, 64)
            )
        )
    )

    ImageIO.write(floor.toBufferedImage(), "png", File("testfloor_final.png"))

    File("myfloor.flr").writeBytes(iff.write())
}

fun createFloor(sourceImage: SlippyImage): SlippyImage {
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