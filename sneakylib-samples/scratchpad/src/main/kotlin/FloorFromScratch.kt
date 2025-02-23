import net.perfectdreams.slippyimage.ColorUtils
import net.perfectdreams.slippyimage.SlippyImage
import net.perfectdreams.slippyimage.convertToSlippyImage
import net.perfectdreams.slippyimage.toBufferedImage
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.*

fun main() {
    val furalhaImage = ImageIO.read(File("furalha.png"))
    val f2 = BufferedImage(furalhaImage.width * 2, furalhaImage.height * 2, BufferedImage.TYPE_INT_ARGB)

    val furalhaSlippyImage = SlippyImage.convertToSlippyImage(furalhaImage)
        .getScaledInstance(90, 90)

    val aaa = furalhaSlippyImage.getRotatedInstanceCentered(Math.toRadians(45.0))
        .getScaledInstance(127, 64)

    val originalGraphics = f2.createGraphics()
    originalGraphics.translate(64, 64)
    originalGraphics.rotate(Math.toRadians(45.0), 64.0, 64.0)
    originalGraphics.drawImage(furalhaImage, 0, 0, null)

    ImageIO.write(aaa.toBufferedImage(), "png", File("furalha_rot.png"))
    ImageIO.write(f2, "png", File("furalha_rot_bufimg.png"))
}

/**
 * Gets the current image but rotated by [radians] radians
 *
 * Keep in mind that this won't create a bigger image!
 */
fun SlippyImage.getRotatedInstanceCentered(
    radians: Double
): SlippyImage {
    // Cached results, yay!
    val sinTheta = sin(radians)
    val cosTheta = cos(radians)
    // Yes, it is + instead of -
    // https://stackoverflow.com/a/57778745/7271796
    val newWidth = (this.width * cosTheta) + (this.height * sinTheta)
    val newHeight = (this.height * cosTheta) + (this.width * sinTheta)

    println("New width: $newWidth")
    println("New height: $newHeight")

    // Rotate around the original image center
    val pivotX = this.width / 2f
    val pivotY = this.height / 2f

    val outputCenterX = newWidth / 2f
    val outputCenterY = newHeight / 2f

    val offsetX = -outputCenterX
    val offsetY = -outputCenterY

    // First we calculate the new width and height based on the current rotation
    // TODO: I think we should round this up?
    val dst = SlippyImage.createEmpty(newWidth.toInt(), newHeight.toInt())
    val newImage = dst

    for (newY in 0 until newImage.height) {
        for (newX in 0 until newImage.width) {
            // For each pixel, we will attempt to find the original pixel in the input
            // Similar to how OpenGL fragment shaders work!
            //
            // The algorithm is...
            // x′ = x cos(θ) − y sin(θ)
            // y′ = y cos(θ) + x sin(θ)
            // Well, that's confusing because that's in math terms and I'm stupid, so here it is in programmer terms...
            // newX = (x * cos(radians)) - (y * sin(radians))
            // newY = (y * cos(radians)) + (x * sin(radians))
            // Ahhh, much better!

            // https://math.stackexchange.com/a/1964911
            // x′=5+(x−5)cos(φ)−(y−10)sin(φ)
            // y′=10+(x−5)sin(φ)+(y−10)cos(φ)
            val newXWithOffset = newX + offsetX
            val newYWithOffset = newY + offsetY

            val originalXAsDouble = pivotX + (newXWithOffset) * cosTheta - (newYWithOffset) * sinTheta
            val originalYAsDouble = pivotY + (newXWithOffset) * sinTheta + (newYWithOffset) * cosTheta

            val originalX = originalXAsDouble.toInt()
            val originalY = originalYAsDouble.toInt()

            if (originalX in 0 until this.width && originalY in 0 until this.height) {
                dst.setRGB(newX, newY, this.getRGB(originalX, originalY))
            }
        }
    }

    return newImage
}

/**
 * Gets the current image but rotated by [radians] radians
 *
 * Keep in mind that this won't create a bigger image!
 */
fun SlippyImage.getRotatedInstance(
    radians: Double,
    pivotX: Float,
    pivotY: Float,
    offsetX: Float,
    offsetY: Float
): SlippyImage {
    val dst = SlippyImage.createEmpty(this.width + 64, this.height + 64)
    val newImage = dst

    // Cached results, yay!
    val sinTheta = sin(radians)
    val cosTheta = cos(radians)

    // TODO: We can calculate the new width and height by calculating the rotation of both points of the image, we can do that later tho :3
    for (newY in 0 until newImage.height) {
        for (newX in 0 until newImage.width) {
            // For each pixel, we will attempt to find the original pixel in the input
            // Similar to how OpenGL fragment shaders work!
            //
            // The algorithm is...
            // x′ = x cos(θ) − y sin(θ)
            // y′ = y cos(θ) + x sin(θ)
            // Well, that's confusing because that's in math terms and I'm stupid, so here it is in programmer terms...
            // newX = (x * cos(radians)) - (y * sin(radians))
            // newY = (y * cos(radians)) + (x * sin(radians))
            // Ahhh, much better!

            // https://math.stackexchange.com/a/1964911
            // x′=5+(x−5)cos(φ)−(y−10)sin(φ)
            // y′=10+(x−5)sin(φ)+(y−10)cos(φ)
            val newXWithOffset = newX + offsetX
            val newYWithOffset = newY + offsetY

            val originalXAsDouble = pivotX + (newXWithOffset) * cosTheta - (newYWithOffset) * sinTheta
            val originalYAsDouble = pivotY + (newXWithOffset) * sinTheta + (newYWithOffset) * cosTheta

            val originalX = originalXAsDouble.toInt()
            val originalY = originalYAsDouble.toInt()

            if (originalX in 0 until this.width && originalY in 0 until this.height) {
                dst.setRGB(newX, newY, this.getRGB(originalX, originalY))
            }
        }
    }

    return newImage
}


// Thanks ChatGPT
/* fun SlippyImage.getRotatedInstance(radians: Double): SlippyImage {
    // Calculate the new image dimensions
    val cosTheta = cos(radians)
    val sinTheta = sin(radians)

    val newWidth = ceil(abs(this.width * cosTheta) + abs(this.height * sinTheta)).toInt()
    val newHeight = ceil(abs(this.width * sinTheta) + abs(this.height * cosTheta)).toInt()

    val output = SlippyImage.createEmpty(newWidth, newHeight)

    val centerX = width / 2.0
    val centerY = height / 2.0
    val outputCenterX = newWidth / 2.0
    val outputCenterY = newHeight / 2.0

    for (yOut in 0 until newWidth) {
        for (xOut in 0 until newHeight) {
            // Apply inverse rotation
            val xIn = (xOut - outputCenterX) * cosTheta + (yOut - outputCenterY) * sinTheta + centerX
            val yIn = -(xOut - outputCenterX) * sinTheta + (yOut - outputCenterY) * cosTheta + centerY

            // Bilinear interpolation
            val x0 = xIn.toInt()
            val y0 = yIn.toInt()

            if (x0 in 0 until width && y0 in 0 until height) {
                val color = pixels[y0 * width + x0]
                output.pixels[yOut * newWidth + xOut] = color
            }
        }
    }

    return output
} */

fun rotatePoint(x: Double, y: Double, pivotX: Double, pivotY: Double, angleDegrees: Double): Pair<Double, Double> {
    val angleRadians = Math.toRadians(angleDegrees) // Convert degrees to radians
    val cosTheta = cos(angleRadians)
    val sinTheta = sin(angleRadians)

    // Translate point to origin (relative to pivot)
    val translatedX = x - pivotX
    val translatedY = y - pivotY

    // Apply rotation transformation
    val rotatedX = translatedX * cosTheta - translatedY * sinTheta
    val rotatedY = translatedX * sinTheta + translatedY * cosTheta

    // Translate point back
    val finalX = rotatedX + pivotX
    val finalY = rotatedY + pivotY

    return Pair(finalX, finalY)
}

fun rotateImage45Degrees(source: IntArray, width: Int, height: Int): SlippyImage {
    val radians = Math.toRadians(45.0)

    // Calculate new image dimensions
    val diagonal = ceil(sqrt(((width * width) + (height * height)).toDouble())).toInt()
    val newWidth = diagonal
    val newHeight = diagonal

    val rotatedImage = IntArray(newWidth * newHeight) { 0 } // Default black

    val cx = width / 2.0
    val cy = height / 2.0
    val ncx = newWidth / 2.0
    val ncy = newHeight / 2.0

    for (y in 0 until height) {
        for (x in 0 until width) {
            val srcIndex = y * width + x
            val color = source[srcIndex]

            // Apply rotation formula
            val newX = ((x - cx) * cos(radians) - (y - cy) * sin(radians) + ncx).toInt()
            val newY = ((x - cx) * sin(radians) + (y - cy) * cos(radians) + ncy).toInt()

            if (newX in 0 until newWidth && newY in 0 until newHeight) {
                val destIndex = newY * newWidth + newX
                rotatedImage[destIndex] = color
            }
        }
    }

    return SlippyImage(newWidth, newHeight, rotatedImage)
}