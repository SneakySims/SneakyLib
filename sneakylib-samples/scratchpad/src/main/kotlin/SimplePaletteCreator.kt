import java.awt.Color
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.time.measureTime

fun loadImage(path: String): BufferedImage {
    return ImageIO.read(java.io.File(path))
}

fun extractColors(image: BufferedImage): List<Color> {
    val colors = mutableSetOf<Color>()
    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            colors.add(Color(image.getRGB(x, y)))
        }
    }
    return colors.toList()
}

// Euclidean distance between colors
fun colorDistance(c1: Color, c2: Color): Double {
    return sqrt((c2.red - c1.red).toDouble().pow(2) + (c2.green - c1.green).toDouble().pow(2) + (c2.blue - c1.blue).toDouble().pow(2))
}

// Find the closest color in the palette
fun findClosestColor(color: Color, palette: List<Color>): Color {
    return palette.minByOrNull { colorDistance(it, color) } ?: palette[0]
}

// K-Means clustering for color quantization
fun kMeansQuantization(colors: List<Color>, k: Int, iterations: Int = 10): List<Color> {
    val centroids = colors.shuffled().take(k).toMutableList()

    repeat(iterations) {
        val clusters = MutableList(k) { mutableListOf<Color>() }

        for (color in colors) {
            val closestIndex = centroids.indices.minByOrNull { colorDistance(color, centroids[it]) } ?: 0
            clusters[closestIndex].add(color)
        }

        for (i in centroids.indices) {
            if (clusters[i].isNotEmpty()) {
                val avgRed = clusters[i].sumOf { it.red } / clusters[i].size
                val avgGreen = clusters[i].sumOf { it.green } / clusters[i].size
                val avgBlue = clusters[i].sumOf { it.blue } / clusters[i].size
                centroids[i] = Color(avgRed, avgGreen, avgBlue)
            }
        }
    }
    return centroids
}

// Apply the 256-color palette to the image
fun applyPalette(image: BufferedImage, palette: List<Color>): BufferedImage {
    val newImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_RGB)

    // Optimization: Cache color results
    val colorToTargetColor = mutableMapOf<Color, Color>()

    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            val oldColor = Color(image.getRGB(x, y))
            // I don't know if this is faster
            val cachedNewColor = colorToTargetColor[oldColor]

            if (cachedNewColor == null) {
                val newColor = findClosestColor(oldColor, palette)
                colorToTargetColor[oldColor] = newColor
                newImage.setRGB(x, y, newColor.rgb)
            } else {
                newImage.setRGB(x, y, cachedNewColor.rgb)
            }
        }
    }
    return newImage
}

fun main() {
    measureTime {
        val image = loadImage("yafyr11_test2.png")
        println("Extracting")
        val colors = extractColors(image)
        println("Palettizing")
        val palette = kMeansQuantization(colors, 256)

        println("Applying")
        val newImage = applyPalette(image, palette)
        ImageIO.write(newImage, "png", java.io.File("yafyr11_test2_palettized2.png"))

        println("Palette reduced to 256 colors successfully!")
    }.also { println("Took $it") }
}