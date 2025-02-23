package net.sneakysims.floormaker.cli

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
import java.io.File
import javax.imageio.ImageIO

class FloorMakerCLI : CliktCommand() {
    override fun help(context: Context): String {
        return "The Sims 1 Floor Maker made by MrPowerGamerBR (https://sneakysims.net)"
    }

    val inputImage by option("--input", help = "The image that will be converted to a floor").required()
    val name by option("--name", help = "The name of the item in game")
    val description by option("--description", help = "The description of the item in game")
    val price by option("--price", help = "The price of the item in game").int()
    val stepSound by option("--step-sound", help = "The sound that the floor will make when Sims walk on top of it").enum<FloorSound> {
        it.name
    }
    val output by argument("output", help = "The output flr file")

    override fun run() {
        val inputImage = ImageIO.read(File(inputImage))
        val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
        val floorSprite = FloorMaker.createFloorSprite(slippyImage)
        val stepSound = stepSound ?: FloorSound.HARD_FLOOR

        val colors = PaletteCreator.extractColors(floorSprite)
        val palette = PaletteCreator.kMeansQuantization(colors, 256)

        val iff = FloorMaker.createFloorIFF(
            name ?: "",
            price ?: 1,
            description ?: "",
            stepSound,
            floorSprite,
            palette
        )

        File(output).writeBytes(iff.write())
    }
}

fun main(args: Array<String>) = FloorMakerCLI().main(args)