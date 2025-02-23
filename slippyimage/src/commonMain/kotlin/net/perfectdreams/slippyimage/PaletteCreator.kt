package net.perfectdreams.slippyimage

import kotlin.math.pow
import kotlin.math.sqrt

// This was originally made in Kotlin for the JVM, but I ported it to Kotlin Multiplatform
object PaletteCreator {
    fun extractColors(imageData: SlippyImage): List<Color> {
        val colors = mutableSetOf<Color>()

        // imageData is a packed ARGB order
        for (argb in imageData.pixels) {
            val color = ColorUtils.unpackARGB(argb)

            // We don't care about alpha
            colors.add(Color(color.red.toUByte(), color.green.toUByte(), color.blue.toUByte()))
        }

        return colors.toList()
    }

    // Euclidean distance between colors
    fun colorDistance(c1: Color, c2: Color): Double {
        return sqrt((c2.red.toInt() - c1.red.toInt()).toDouble().pow(2) + (c2.green.toInt() - c1.green.toInt()).toDouble().pow(2) + (c2.blue.toInt() - c1.blue.toInt()).toDouble().pow(2))
    }

    // Find the closest color in the palette
    fun findClosestColor(color: Color, palette: List<Color>): Color {
        return palette.minByOrNull { colorDistance(it, color) } ?: palette[0]
    }

    // K-Means clustering for color quantization
    fun kMeansQuantization(colors: List<Color>, k: Int, iterations: Int = 10): List<Color> {
        if (k >= colors.size)
            return colors // Return the palette as is, the current colors already match what we want

        val centroids = colors.shuffled().take(k).toMutableList()

        repeat(iterations) {
            val clusters = MutableList(k) { mutableListOf<Color>() }

            for (color in colors) {
                val closestIndex = centroids.indices.minByOrNull { colorDistance(color, centroids[it]) } ?: 0
                clusters[closestIndex].add(color)
            }

            for (i in centroids.indices) {
                if (clusters[i].isNotEmpty()) {
                    val avgRed = clusters[i].sumOf { it.red.toInt() } / clusters[i].size
                    val avgGreen = clusters[i].sumOf { it.green.toInt() } / clusters[i].size
                    val avgBlue = clusters[i].sumOf { it.blue.toInt() } / clusters[i].size
                    centroids[i] = Color(avgRed, avgGreen, avgBlue)
                }
            }
        }
        return centroids
    }

    // Apply the 256-color palette to the image
    fun applyPalette(imageData: SlippyImage, palette: List<Color>) {
        // Optimization: Cache color results
        val colorToTargetColor = mutableMapOf<Color, Color>()

        for (i in imageData.pixels.indices) {
            // imageData is in RGBA order
            val argb = imageData.pixels[i]

            val a = (argb shr 24) and 0xFF // Extract alpha
            val r = (argb shr 16) and 0xFF // Extract red
            val g = (argb shr 8) and 0xFF  // Extract green
            val b = argb and 0xFF          // Extract blue

            val oldColor = Color(r.toUByte(), g.toUByte(), b.toUByte())

            // I don't know if this is faster
            val cachedNewColor = colorToTargetColor[oldColor]

            if (cachedNewColor == null) {
                val newColor = findClosestColor(oldColor, palette)
                colorToTargetColor[oldColor] = newColor
                val newARGB = (a shl 24) or (newColor.red.toInt() shl 16) or (newColor.green.toInt() shl 8) or newColor.blue.toInt()
                imageData.pixels[i] = newARGB
            } else {
                val newARGB = (a shl 24) or (cachedNewColor.red.toInt() shl 16) or (cachedNewColor.green.toInt() shl 8) or cachedNewColor.blue.toInt()
                imageData.pixels[i] = newARGB
            }
        }
    }
}