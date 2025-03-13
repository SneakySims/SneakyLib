package net.sneakysims.wallpapermaker.cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.int
import net.perfectdreams.slippyimage.PaletteCreator
import net.perfectdreams.slippyimage.SlippyImage
import net.perfectdreams.slippyimage.convertToSlippyImage
import net.sneakysims.wallpapermaker.WallpaperMaker
import java.io.File
import javax.imageio.ImageIO

class WallpaperMakerCLI : CliktCommand() {
    override fun help(context: Context): String {
        return "The Sims 1 Wallpaper Maker made by MrPowerGamerBR (https://sneakysims.net)"
    }

    val inputImage by option("--input-front", help = "The image that will be converted to a wall (front facing)").required()
    val inputImageLeft by option("--input-left", help = "The image that will be converted to a wall (side left). If not present, the front facing wall image will be used")
    val inputImageRight by option("--input-right", help = "The image that will be converted to a wall (side right). If not present, the front facing wall image will be used")
    val inputImageCorner by option("--input-corner", help = "The image that will be converted to a wall (corner), the game does not seem to use this sprite, but tools (like HomeCrafter) require the image to be present. If not present, the front facing wall image will be used")
    val name by option("--name", help = "The name of the item in game")
    val description by option("--description", help = "The description of the item in game")
    val price by option("--price", help = "The price of the item in game").int()
    val output by argument("output", help = "The output wll file")

    override fun run() {
        val inputImage = ImageIO.read(File(inputImage))
        val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
        val frontFacingSprite = WallpaperMaker.createWallFrontSprite(slippyImage)

        val sideLeftSprite: SlippyImage
        val sideRightSprite: SlippyImage
        val sideCornerSprite: SlippyImage

        if (inputImageLeft != null) {
            val inputImage = ImageIO.read(File(inputImageLeft))
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideLeftSprite = WallpaperMaker.createWallLeftSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        } else {
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideLeftSprite = WallpaperMaker.createWallLeftSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        }

        if (inputImageRight != null) {
            val inputImage = ImageIO.read(File(inputImageRight))
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideRightSprite = WallpaperMaker.createWallRightSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        } else {
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideRightSprite = WallpaperMaker.createWallRightSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        }

        if (inputImageCorner != null) {
            val inputImage = ImageIO.read(File(inputImageCorner))
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideCornerSprite = WallpaperMaker.createWallCornerSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        } else {
            val slippyImage = SlippyImage.convertToSlippyImage(inputImage)
            sideCornerSprite = WallpaperMaker.createWallCornerSprite(WallpaperMaker.createSquishedImageForLateralWallsSprite(slippyImage))
        }

        val colors = PaletteCreator.extractColors(sideLeftSprite) + PaletteCreator.extractColors(sideRightSprite) + PaletteCreator.extractColors(frontFacingSprite)
        val palette = PaletteCreator.kMeansQuantization(colors, 256)

        val iff = WallpaperMaker.createWallpaperIFF(
            name ?: "",
            price ?: 1,
            description ?: "",
            frontFacingSprite,
            sideLeftSprite,
            sideRightSprite,
            sideCornerSprite,
            palette
        )

        File(output).writeBytes(iff.write())
    }
}

fun main(args: Array<String>) = WallpaperMakerCLI().main(args)