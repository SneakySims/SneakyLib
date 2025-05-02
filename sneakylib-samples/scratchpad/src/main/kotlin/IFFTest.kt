import net.sneakysims.sneakylib.iff.*
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    val input = File("C:\\Program Files (x86)\\Steam\\steamapps\\common\\The Sims Legacy Collection\\GameData\\walls.iff")
    
    val iff = IFF.read(input.readBytes())

    dumpSPR(iff, "originalgamesprites")

    return

    val text = iff.chunks.first { it.code == "STR#" }.decodeDataAsSTR()

    when (text.format) {
        is STRChunkData.StringFormat.StringFormatFDFF -> {
            // val onlyEnglish = text.format.strings.filter { it.language == TheSimsLanguage.EnglishUS }
            // text.format.strings.clear()

            (text.format as STRChunkData.StringFormat.StringFormatFDFF).strings.forEach {
                it.text = it.text.replace("to", "UwU")

                if (it.text == "14")
                    it.text = "1"
            }

            // text.format.strings.addAll(onlyEnglish)
        }

        is STRChunkData.StringFormat.StringFormatFCFF -> TODO()
    }

    dumpSPR(iff, "original")

    iff.chunks.filter { it.code == IFFChunk.PALT_CHUNK_CODE }
        .forEach {
            it.decodeDataAsPALT().entries.forEachIndexed { index, color ->
                println("Index $index is $color")
            }
        }

    val rewrittenFile = File(".\\WallScreenPanel_rewritten.wll")
    rewrittenFile.writeBytes(iff.write())

    val iff2 = IFF.read(rewrittenFile.readBytes())

    dumpSPR(iff2, "rewritten")
}

fun dumpSPR(iff: IFF, prefix: String) {
    val sprIff = iff.chunks.filter { it.code == IFFChunk.SPR_CHUNK_CODE }

    for (sprChunk in sprIff) {
        val spr = sprChunk.decodeDataAsSPR()

        for ((index, sprite) in spr.sprites.withIndex()) {
            val palt = iff.chunks.firstOrNull { it.id == spr.paletteId.toShort() }?.decodeDataAsPALT()
            if (palt == null) {
                println("Could not find palette for SPR# ${sprChunk.id}! ${spr.paletteId.toShort()}")
                continue
            }

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

            ImageIO.write(image, "png", File("${prefix}_spr_${sprChunk.id}_${index}.png"))
        }
    }
}