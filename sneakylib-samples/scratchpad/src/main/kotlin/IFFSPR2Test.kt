import net.sneakysims.sneakylib.Color
import net.sneakysims.sneakylib.iff.*
import net.sneakysims.sneakylib.sims.TheSimsLanguage
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val input = File("C:\\Program Files (x86)\\Maxis\\The Sims\\GameData\\floors.iff")

    val iff = IFF.read(input.readBytes())
    dumpSPR2(iff, "flrspr2")
    /* val iff = IFF.empty()

    iff.chunks.add(
        IFFChunk(
            IFFChunk.STR_CHUNK_CODE,
            0,
            // Why does it need to be 16?
            // IFF Pencil, and the game, only works if this is 16
            16,
            ByteArray(64),
            STRChunkData(
                STRChunkData.StringFormat.StringFormatFDFF(
                    mutableListOf(
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
                            "Loritta is so cute!!",
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
                            "1",
                            "!"
                        ),
                        STRChunkData.StringFormat.StringFormatFDFF.SimsString(
                            TheSimsLanguage.getLanguageById(1),
                            "hewwo",
                            "!"
                        )
                    )
                )
            )
        )
    )

    val sprite0 = ImageIO.read(File("flr_spr2_513_0.png"))

    val colors = extractColors(sprite0)
    val palette = kMeansQuantization(colors, 256)

    iff.chunks.add(
        IFFChunk(
            IFFChunk.PALT_CHUNK_CODE,
            1,
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

    fun convertToSPRSprite(image: BufferedImage, targetWidth: Int, targetHeight: Int): SPR2ChunkData.SPRSprite {
        val resizedImage = toBufferedImage(image.getScaledInstance(targetWidth, targetHeight, BufferedImage.SCALE_FAST))

        val rows = mutableListOf<IntArray>()

        repeat(targetHeight) { y ->
            val row = IntArray(targetWidth)
            repeat(targetWidth) { x ->
                row[x] = palette.indexOf(findClosestColor(java.awt.Color(resizedImage.getRGB(x, y), false), palette))
            }

            rows.add(row)
        }

        return SPR2ChunkData.SPRSprite(
            7,
            0,
            1,
            254,
            0,
            0,
            SPR2ChunkData.SPR2ImageData(
                targetWidth.toShort(),
                targetHeight.toShort(),
                rows
            )
        )
    }

    iff.chunks.add(
        IFFChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            1,
            16,
            ByteArray(64),
            SPR2ChunkData(
                1000,
                1,
                listOf(
                    convertToSPRSprite(sprite0, 31, 16)
                )
            )
        )
    )

    iff.chunks.add(
        IFFChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            257,
            16,
            ByteArray(64),
            SPR2ChunkData(
                1000,
                1,
                listOf(
                    convertToSPRSprite(sprite0, 63, 32)
                )
            )
        )
    )

    iff.chunks.add(
        IFFChunk(
            IFFChunk.SPR2_CHUNK_CODE,
            513,
            16,
            ByteArray(64),
            SPR2ChunkData(
                1000,
                1,
                listOf(
                    convertToSPRSprite(sprite0, 127, 64)
                )
            )
        )
    )

    // dumpSPR2(iff, "flr")

    File("RewriteTest.flr").writeBytes(iff.write())

    // Attempt to re-read the written file
    IFF.read(File("RewriteTest.flr").readBytes()) */

}

fun dumpSPR2(iff: IFF, prefix: String) {
    for (spr in iff.chunks.filter { it.code == net.sneakysims.sneakylib.iff.IFFChunk.SPR2_CHUNK_CODE }) {
        val data = spr.data as SPR2ChunkData

        println("Palette ID: ${data.paletteId}")
        val palt = iff.chunks
            .filter {
                it.code == net.sneakysims.sneakylib.iff.IFFChunk.PALT_CHUNK_CODE
            }
            .first { it.id.toInt() == data.paletteId }.data as PALTChunkData

        for ((index, sprite) in data.sprites.withIndex()) {
            println("Exporting image $index (${sprite.imageData.data.size} rows)")
            if (sprite.imageData.width.toInt() != -1 && sprite.imageData.height.toInt() != -1) {
                val image = BufferedImage(
                    sprite.imageData.width.toInt(),
                    sprite.imageData.height.toInt(),
                    BufferedImage.TYPE_INT_ARGB
                )

                for ((y, row) in sprite.imageData.data.withIndex()) {
                    for ((x, paletteIndex) in row.withIndex()) {
                        // If it is not transparent...
                        if (paletteIndex != SPRChunkData.SPRImageData.TRANSPRENCY_INDEX) {
                            // Set the color!
                            val (paletteIndex, alphaBlending, depthBuffer) = SPR2Utils.unpackPixelData(paletteIndex)
                            val paletteColor = palt.entries[paletteIndex.toInt()]
                            // The depth buffer for the entire floor (where there ARE pixels) is 255u
                            image.setRGB(x, y, paletteColor.getRGBAWithAlpha(alphaBlending.toInt()))
                        }
                    }
                }

                ImageIO.write(image, "png", File("${prefix}_spr2_${spr.id}_${index}.png"))
            }
        }
    }
}