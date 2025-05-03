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
import net.sneakysims.floormaker.FloorMaker.convertToSPR2Sprite
import net.sneakysims.floormaker.FloorSound
import java.io.File
import javax.imageio.ImageIO

class FloorMakerCLI : CliktCommand() {
    override fun help(context: Context): String {
        return "The Sims 1 Floor Maker made by MrPowerGamerBR (https://sneakysims.net)"
    }

    val inputImage by option("--input", help = "The image that will be converted to a floor")
    val inputRawFarZoomImage by option("--input-raw-far-zoom", help = "The image that will be converted to a floor (far zoom), raw in this context means the image in the same position/rotation as a floor")
    val inputRawMediumZoomImage by option("--input-raw-medium-zoom", help = "The image that will be converted to a floor (medium zoom), raw in this context means the image in the same position/rotation as a floor")
    val inputRawNearZoomImage by option("--input-raw-near-zoom", help = "The image that will be converted to a floor (near zoom), raw in this context means the image in the same position/rotation as a floor")
    val name by option("--name", help = "The name of the item in game")
    val description by option("--description", help = "The description of the item in game")
    val price by option("--price", help = "The price of the item in game").int()
    val stepSound by option("--step-sound", help = "The sound that the floor will make when Sims walk on top of it").enum<FloorSound> {
        it.name
    }
    val output by argument("output", help = "The output flr file")

    override fun run() {
        if (inputImage != null) {
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
        } else if (inputRawFarZoomImage != null && inputRawMediumZoomImage != null && inputRawNearZoomImage != null) {
            val farZoomImage = SlippyImage.convertToSlippyImage(ImageIO.read(File(inputRawFarZoomImage)))
            val mediumZoomImage = SlippyImage.convertToSlippyImage(ImageIO.read(File(inputRawMediumZoomImage)))
            val nearZoomImage = SlippyImage.convertToSlippyImage(ImageIO.read(File(inputRawNearZoomImage)))

            val stepSound = stepSound ?: FloorSound.HARD_FLOOR

            val colors = PaletteCreator.extractColors(farZoomImage) + PaletteCreator.extractColors(mediumZoomImage) + PaletteCreator.extractColors(nearZoomImage)
            val palette = PaletteCreator.kMeansQuantization(colors, 256)

            val iff = FloorMaker.createFloorIFF(
                name ?: "",
                price ?: 1,
                description ?: "",
                stepSound,
                convertToSPR2Sprite(farZoomImage, 31, 16, palette),
                convertToSPR2Sprite(mediumZoomImage, 63, 32, palette),
                convertToSPR2Sprite(nearZoomImage, 127, 64, palette),
                palette
            )

            File(output).writeBytes(iff.write())
        } else error("Missing image input parameter!")
    }
}

fun main(args: Array<String>) = FloorMakerCLI().main(args)