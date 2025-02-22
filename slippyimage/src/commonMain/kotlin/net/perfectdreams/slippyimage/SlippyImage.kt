package net.perfectdreams.slippyimage

class SlippyImage(
    val width: Int,
    val height: Int,
    /**
     * An array of packed ARGB pixels
     */
    val pixels: IntArray
) {
    companion object {
        fun createEmpty(width: Int, height: Int): SlippyImage {
            return SlippyImage(width, height, IntArray(width * height))
        }
    }

    fun setRGB(x: Int, y: Int, red: Int, green: Int, blue: Int, alpha: Int) {
        setRGB(x, y, ColorUtils.packARGB(red, green, blue, alpha))
    }

    fun setRGB(x: Int, y: Int, argb: Int) {
        // println("Setting pixel $x, $y")
        val targetIndex = ((y * this.width) + x)

        pixels[targetIndex] = argb
    }

    fun getRGB(x: Int, y: Int): Int {
        val targetIndex = ((y * this.width) + x)

        return pixels[targetIndex]
    }

    fun getRGB(x: Int, y: Int, width: Int, height: Int): IntArray {
        val results = IntArray(width * height)

        var j = 0
        for (i in pixels.indices) {
            val sourceX = (i % this.width)
            val sourceY = (i / this.width)

            if (sourceX in x until (x + width) && sourceY in y until (y + height)) {
                // println("Copying $sourceX and $sourceY ($x, $y, $sourceX, $sourceY, ${(x + width)}, ${y + height})")
                val argb = this.pixels[i]
                results[j++] = argb
            }
        }

        // println("j is $j, results is ${results.size}")
        return results
    }

    fun drawImage(source: SlippyImage, targetX: Int, targetY: Int) {
        var currentY = 0
        for (sourceY in 0 until source.height) {
            var currentX = 0

            for (sourceX in 0 until source.width) {
                // When drawing image, we don't really care if the image is outside the bounds
                val canvasTargetX = targetX + currentX
                val canvasTargetY = currentY + targetY

                if (canvasTargetX in 0 until this.width && canvasTargetY in 0 until this.height) {
                    val rgbValue = source.getRGB(sourceX, sourceY)
                    val unpacked = ColorUtils.unpackARGB(rgbValue)

                    // TODO: Add an option to "ignore alpha"
                    // TODO: There should be a option to blend alpha values
                    if (unpacked.alpha != 0)
                        this.setRGB(canvasTargetX, canvasTargetY, source.getRGB(sourceX, sourceY))
                }

                currentX++
            }

            currentY++
        }
    }

    fun drawImage(
        source: SlippyImage,
        sourceX: Int,
        sourceY: Int,
        sourceWidth: Int,
        sourceHeight: Int,
        targetX: Int,
        targetY: Int
    ) {
        if (targetX + sourceWidth > this.width)
            error("Outside of coordinate bounds! (${targetX + sourceWidth} > ${this.width})")
        if (targetY + sourceHeight > this.height)
            error("Outside of coordinate bounds! (${targetY + sourceHeight} > ${this.height})")

        var currentY = 0

        for (loopY in sourceY until sourceY + sourceHeight) {
            var currentX = 0

            for (loopX in sourceX until sourceX + sourceWidth) {
                this.setRGB(targetX + currentX, currentY + targetY, source.getRGB(loopX, loopY))
                currentX++
            }

            currentY++
        }
    }

    // A very simple nearest neighbor implementation
    fun getScaledInstance(targetWidth: Int, targetHeight: Int): SlippyImage {
        val newImageData = createEmpty(targetWidth, targetHeight)

        val widthDifference = this.width.toDouble() / targetWidth
        val heightDifference = this.height.toDouble() / targetHeight

        // println("Width Difference: $widthDifference")
        // println("Height Difference: $heightDifference")

        for (targetY in 0 until targetHeight) {
            for (targetX in 0 until targetWidth) {
                val mappedTargetX = (targetX * widthDifference).toInt()
                val mappedTargetY = (targetY * heightDifference).toInt()

                newImageData
                    .setRGB(
                        targetX,
                        targetY,
                        this.getRGB(mappedTargetX, mappedTargetY)
                    )
            }
        }

        return newImageData
    }

    /* fun setRGB(x: Int, y: Int, width: Int, height: Int, pixels: IntArray) {
        val results = IntArray(width * height)

        var j = 0
        for (i in pixels.indices step 4) {
            val sourceX = (i % this.width)
            val sourceY = (i / this.height)

            if (sourceX in x until (x + width) && sourceY in y until (y + height)) {
                val red = results[i]
                val green = results[i + 1]
                val blue = results[i + 2]
                val alpha = results[i + 3]

                val packed = (alpha shl 24) or (red shl 16) or (green shl 8) or blue

                results[j++] = packed
            }
        }
        return results
    } */
}