import net.sneakysims.sneakylib.Color
import net.sneakysims.sneakylib.iff.*
import net.sneakysims.sneakylib.sims.TheSimsLanguage
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val iff = IFF.empty()

    val input = File("C:\\Program Files (x86)\\Steam\\steamapps\\common\\The Sims Legacy Collection\\GameData\\Walls\\WallScreenPanel.wll")
    val originalIff = IFF.read(input.readBytes())

    if (false) {
        fun dumpSPR(iff: IFF, prefix: String) {
            for (chunk in iff.chunks.filter { it.code == net.sneakysims.sneakylib.iff.IFFChunk.SPR_CHUNK_CODE }) {
                // Also must be 16?
                println("Chunk Flags: ${chunk.flags}")
                val spr = chunk.data as SPRChunkData
                val palt = iff.chunks.first { it.id == spr.paletteId.toShort() }.data as PALTChunkData

                for ((index, sprite) in spr.sprites.withIndex()) {
                    val image = BufferedImage(sprite.imageData.width.toInt(), sprite.imageData.height.toInt(), BufferedImage.TYPE_INT_ARGB)

                    for ((y, row) in sprite.imageData.data.withIndex()) {
                        for ((x, paletteIndex) in row.withIndex()) {
                            // If it is not transparent...
                            if (paletteIndex != SPRChunkData.SPRImageData.TRANSPRENCY_INDEX) {
                                // Set the color!
                                val paletteColor = palt.entries[paletteIndex]
                                // println("palColor: $paletteColor")
                                image.setRGB(x, y, paletteColor.rgba)
                            }
                        }
                    }

                    ImageIO.write(image, "png", File("${prefix}_spr${chunk.id}_${index}.png"))
                }
            }
        }

        dumpSPR(originalIff, "hewwo.png")

        return
    }
    // iff.chunks.addAll(originalIff.chunks.filter { it.code == "STR#" })
    iff.chunks.add(
        net.sneakysims.sneakylib.iff.IFFChunk(
            net.sneakysims.sneakylib.iff.IFFChunk.STR_CHUNK_CODE,
            0,
            // Why does it need to be 16?
            // IFF Pencil, and the game, only works if this is 16
            16,
            ByteArray(64),
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
    )

    val sprite0 = ImageIO.read(File("hewwo.png_spr2049_0.png"))
    val sprite1 = ImageIO.read(File("hewwo.png_spr2049_1.png"))
    val sprite2 = ImageIO.read(File("hewwo.png_spr2049_2.png"))
    // Unused?
    val sprite3 = ImageIO.read(File("hewwo.png_spr2049_3.png"))

    val colors = extractColors(sprite2)
    val palette = kMeansQuantization(colors, 256)
    println("Palette Size is ${palette.size}")

    iff.chunks.add(
        net.sneakysims.sneakylib.iff.IFFChunk(
            net.sneakysims.sneakylib.iff.IFFChunk.PALT_CHUNK_CODE,
            1537,
            0,
            ByteArray(64),
            PALTChunkData(
                1,
                0,
                0,
                palette.map { Color(it.red, it.green, it.blue) }
            )
        )
    )

    fun convertToSPRSprite(image: BufferedImage, targetWidth: Int, targetHeight: Int): SPRChunkData.SPRSprite {
        val resizedImage = toBufferedImage(image.getScaledInstance(targetWidth, targetHeight, BufferedImage.SCALE_FAST))

        val rows = mutableListOf<IntArray>()

        repeat(targetHeight) { y ->
            val row = IntArray(targetWidth)
            repeat(targetWidth) { x ->
                row[x] = palette.indexOf(findClosestColor(java.awt.Color(resizedImage.getRGB(x, y), false), palette))
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

    // Walls requires a SPR# chunk, if not the wall will not load
    // The SPR# may be wonky or corrupt, but it MUST exist

    iff.chunks.add(
        net.sneakysims.sneakylib.iff.IFFChunk(
            net.sneakysims.sneakylib.iff.IFFChunk.SPR_CHUNK_CODE,
            1,
            16,
            ByteArray(64),
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(sprite0, 16, 67),
                    convertToSPRSprite(sprite1, 16, 68),
                    convertToSPRSprite(sprite2, 32, 60),
                    convertToSPRSprite(sprite3, 4, 58)
                )
            )
        )
    )

    iff.chunks.add(
        net.sneakysims.sneakylib.iff.IFFChunk(
            net.sneakysims.sneakylib.iff.IFFChunk.SPR_CHUNK_CODE,
            1793,
            16,
            ByteArray(64),
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(sprite0, 32, 135),
                    convertToSPRSprite(sprite1, 32, 136),
                    convertToSPRSprite(sprite2, 64, 120),
                    convertToSPRSprite(sprite3, 8, 116)
                )
            )
        )
    )

    iff.chunks.add(
        net.sneakysims.sneakylib.iff.IFFChunk(
            net.sneakysims.sneakylib.iff.IFFChunk.SPR_CHUNK_CODE,
            2049,
            16,
            ByteArray(64),
            SPRChunkData(
                505,
                1537,
                mutableListOf(
                    convertToSPRSprite(sprite0, 64, 271),
                    convertToSPRSprite(sprite1, 64, 272),
                    convertToSPRSprite(sprite2, 128, 240),
                    convertToSPRSprite(sprite3, 16, 232)
                )
            )
        )
    )

    File("CustomWall.wll").writeBytes(iff.write())

    IFF.read(File("CustomWall.wll").readBytes())
}

/**
 * Converts a given Image into a BufferedImage
 *
 * @param img The Image to be converted
 * @return The converted BufferedImage
 */
fun toBufferedImage(img: Image): BufferedImage {
    if (img is BufferedImage) {
        return img
    }

    // Create a buffered image with transparency
    val bimage = BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB)

    // Draw the image on to the buffered image
    val bGr = bimage.createGraphics()
    bGr.drawImage(img, 0, 0, null)
    bGr.dispose()

    // Return the buffered image
    return bimage
}