package net.perfectdreams.slippyimage

import java.awt.image.BufferedImage

fun SlippyImage.Companion.convertToSlippyImage(image: BufferedImage): SlippyImage {
    return SlippyImage(image.width, image.height, image.getRGB(0, 0, image.width, image.height, null, 0, image.width))
}

fun SlippyImage.toBufferedImage(): BufferedImage {
    val bufferedImage = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    bufferedImage.setRGB(0, 0, this.width, this.height, this.getRGB(0, 0, this.width, this.height), 0, this.width)
    return bufferedImage
}