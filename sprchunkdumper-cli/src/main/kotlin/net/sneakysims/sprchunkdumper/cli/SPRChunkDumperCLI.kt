package net.sneakysims.sprchunkdumper.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import net.sneakysims.sneakylib.iff.IFF
import net.sneakysims.sneakylib.iff.IFFChunk
import net.sneakysims.sneakylib.iff.SPR2Utils
import net.sneakysims.sneakylib.iff.SPRChunkData
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class SPRChunkDumperCLI : CliktCommand() {
    override fun help(context: Context): String {
        return "Dumps all SPR# and SPR2 chunks from a IFF file to PNGs - Made by MrPowerGamerBR (https://sneakysims.net)"
    }

    val inputIFF by option("--input", help = "The IFF file that will have its chunks dumped").required()

    override fun run() {
        val file = File(inputIFF)
        val iff = IFF.read(file.readBytes())

        dumpSPR(iff, file.nameWithoutExtension)
        // dumpSPR2(iff, file.nameWithoutExtension)
    }

    fun dumpSPR(iff: IFF, prefix: String) {
        for (spr in iff.chunks.filter { it.code == IFFChunk.SPR_CHUNK_CODE }) {
            try {
                val data = spr.decodeDataAsSPR()

                println("Palette ID: ${data.paletteId}")
                val palt = iff.chunks
                    .filter {
                        it.code == IFFChunk.PALT_CHUNK_CODE
                    }
                    .first { it.id.toInt() == data.paletteId }.decodeDataAsPALT()

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
                                if (paletteIndex != SPRChunkData.SPRImageData.TRANSPRENCY_INDEX) {
                                    // Set the color!
                                    val paletteColor = palt.entries[paletteIndex]
                                    image.setRGB(x, y, paletteColor.rgba)
                                }
                            }
                        }

                        ImageIO.write(image, "png", File("${prefix}_spr_${spr.id}_${index}.png"))
                    }
                }
            } catch (e: Exception) {
                println("Failed to dump ${spr.code} ${spr.id}! Skipping...")
                e.printStackTrace()
            }
        }
    }

    fun dumpSPR2(iff: IFF, prefix: String) {
        for (spr in iff.chunks.filter { it.code == IFFChunk.SPR2_CHUNK_CODE }) {
            try {
                val data = spr.decodeDataAsSPR2()

                println("Palette ID: ${data.paletteId}")
                val palt = iff.chunks
                    .filter {
                        it.code == IFFChunk.PALT_CHUNK_CODE
                    }
                    .first { it.id.toInt() == data.paletteId }.decodeDataAsPALT()

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
                                    val (paletteIndex, alphaBlending, depthBuffer) = SPR2Utils.unpackPixelData(
                                        paletteIndex
                                    )
                                    val paletteColor = palt.entries[paletteIndex.toInt()]
                                    // The depth buffer for the entire floor (where there ARE pixels) is 255u
                                    image.setRGB(x, y, paletteColor.getRGBAWithAlpha(alphaBlending.toInt()))
                                }
                            }
                        }

                        ImageIO.write(image, "png", File("${prefix}_spr2_${spr.id}_${index}.png"))
                    }
                }
            } catch (e: Exception) {
                println("Failed to dump ${spr.code} ${spr.id}! Skipping...")
                e.printStackTrace()
            }
        }
    }
}

fun main(args: Array<String>) = SPRChunkDumperCLI().main(args)