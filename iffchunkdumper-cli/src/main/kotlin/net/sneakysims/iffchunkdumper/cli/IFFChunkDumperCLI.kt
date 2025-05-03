package net.sneakysims.iffchunkdumper.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.parameters.types.int
import net.perfectdreams.slippyimage.PaletteCreator
import net.perfectdreams.slippyimage.SlippyImage
import net.perfectdreams.slippyimage.convertToSlippyImage
import net.sneakysims.floormaker.FloorMaker
import net.sneakysims.floormaker.FloorSound
import net.sneakysims.sneakylib.iff.IFF
import java.io.File
import javax.imageio.ImageIO

class IFFChunkDumperCLI : CliktCommand() {
    override fun help(context: Context): String {
        return "Dumps all IFF chunks from a IFF file - Made by MrPowerGamerBR (https://sneakysims.net)"
    }

    val inputIFF by option("--input", help = "The IFF file that will have its chunks dumped").required()

    override fun run() {
        val file = File(inputIFF)
        val iff = IFF.read(file.readBytes())

        for (chunk in iff.chunks) {
            println("Chunk ID: ${chunk.id}")
            println("Chunk Code: ${chunk.code}")
            println("Chunk Flag: ${chunk.flags}")
            println("Chunk Name: ${chunk.name.joinToString("") { it.toInt().toChar().toString() }}")
            println("-------------------------------------------------------")

            File("${file.nameWithoutExtension}_${chunk.code}_${chunk.id}.bin").writeBytes(iff.write())
        }
    }
}

fun main(args: Array<String>) = IFFChunkDumperCLI().main(args)