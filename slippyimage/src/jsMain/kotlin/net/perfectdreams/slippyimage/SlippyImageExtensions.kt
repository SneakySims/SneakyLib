package net.perfectdreams.slippyimage

import js.buffer.ArrayBuffer
import js.typedarrays.Uint8ClampedArray
import web.images.ImageData

fun SlippyImage.Companion.convertToSlippyImage(imageData: ImageData): SlippyImage {
    // Canvas uses RGBA, we use packed ARGB
    val pixels = IntArray(imageData.data.length / 4)
    var i = 0
    var j = 0
    while (imageData.data.length > i) {
        val red = imageData.data.asDynamic()[i++] as Int
        val green = imageData.data.asDynamic()[i++] as Int
        val blue = imageData.data.asDynamic()[i++] as Int
        val alpha = imageData.data.asDynamic()[i++] as Int

        pixels[j++] = (alpha shl 24) or (red shl 16) or (green shl 8) or blue
    }

    return SlippyImage(
        imageData.width,
        imageData.height,
        pixels
    )
}

fun SlippyImage.toImageData(): ImageData {
    val jsImagePixels = Uint8ClampedArray<ArrayBuffer>(pixels.size * 4)

    var i = 0
    var j = 0

    while (this.pixels.size > i) {
        // println("Index is $i, total ${this.pixels.size}")
        val argb = this.pixels[i++]

        val a = (argb shr 24) and 0xFF // Extract alpha
        val r = (argb shr 16) and 0xFF // Extract red
        val g = (argb shr 8) and 0xFF  // Extract green
        val b = argb and 0xFF          // Extract blue

        // println("Pixel is $r, $g, $b, $a")
        jsImagePixels.asDynamic()[j++] = r
        jsImagePixels.asDynamic()[j++] = g
        jsImagePixels.asDynamic()[j++] = b
        jsImagePixels.asDynamic()[j++] = a
    }
    // println("Done!")

    return ImageData(jsImagePixels, this.width, this.height)
}