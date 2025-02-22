package net.perfectdreams.slippyimage

import kotlin.math.floor

object ColorUtils {
    fun packARGB(red: Int, green: Int, blue: Int, alpha: Int): Int {
        return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
    }

    fun unpackARGB(argb: Int): ARGB {
        val a = (argb shr 24) and 0xFF // Extract alpha
        val r = (argb shr 16) and 0xFF // Extract red
        val g = (argb shr 8) and 0xFF  // Extract green
        val b = argb and 0xFF          // Extract blue

        return ARGB(r, g, b, a)
    }

    fun unpackRGB(argb: Int): RGB {
        val r = (argb shr 16) and 0xFF // Extract red
        val g = (argb shr 8) and 0xFF  // Extract green
        val b = argb and 0xFF          // Extract blue

        return RGB(r, g, b)
    }

    data class RGB(
        val red: Int,
        val green: Int,
        val blue: Int
    )

    data class ARGB(
        val red: Int,
        val green: Int,
        val blue: Int,
        val alpha: Int,
    )

    /**
     * Converts the components of a color, as specified by the default RGB
     * model, to an equivalent set of values for hue, saturation, and
     * brightness that are the three components of the HSB model.
     *
     *
     * If the `hsbvals` argument is `null`, then a
     * new array is allocated to return the result. Otherwise, the method
     * returns the array `hsbvals`, with the values put into
     * that array.
     * @param     r   the red component of the color
     * @param     g   the green component of the color
     * @param     b   the blue component of the color
     * @param     hsbvals  the array used to return the
     * three HSB values, or `null`
     * @return    an array of three elements containing the hue, saturation,
     * and brightness (in that order), of the color with
     * the indicated red, green, and blue components.
     * @see java.awt.Color.getRGB
     * @see java.awt.Color.Color
     * @see java.awt.image.ColorModel.getRGBdefault
     * @since     1.0
     */
    // From JDK
    fun RGBtoHSB(r: Int, g: Int, b: Int, hsbvals: FloatArray?): FloatArray {
        var hsbvals = hsbvals
        var hue: Float
        val saturation: Float
        val brightness: Float
        if (hsbvals == null) {
            hsbvals = FloatArray(3)
        }
        var cmax = if (r > g) r else g
        if (b > cmax) cmax = b
        var cmin = if (r < g) r else g
        if (b < cmin) cmin = b
        brightness = cmax.toFloat() / 255.0f
        saturation = if (cmax != 0) (cmax - cmin).toFloat() / cmax.toFloat() else 0f
        if (saturation == 0f) hue = 0f else {
            val redc = (cmax - r).toFloat() / (cmax - cmin).toFloat()
            val greenc = (cmax - g).toFloat() / (cmax - cmin).toFloat()
            val bluec = (cmax - b).toFloat() / (cmax - cmin).toFloat()
            hue = if (r == cmax) bluec - greenc else if (g == cmax) 2.0f + redc - bluec else 4.0f + greenc - redc
            hue /= 6.0f
            if (hue < 0) hue += 1.0f
        }
        hsbvals[0] = hue
        hsbvals[1] = saturation
        hsbvals[2] = brightness
        return hsbvals
    }

    /**
     * Converts the components of a color, as specified by the HSB
     * model, to an equivalent set of values for the default RGB model.
     *
     *
     * The `saturation` and `brightness` components
     * should be floating-point values between zero and one
     * (numbers in the range 0.0-1.0).  The `hue` component
     * can be any floating-point number.  The floor of this number is
     * subtracted from it to create a fraction between 0 and 1.  This
     * fractional number is then multiplied by 360 to produce the hue
     * angle in the HSB color model.
     *
     *
     * The integer that is returned by `HSBtoRGB` encodes the
     * value of a color in bits 0-23 of an integer value that is the same
     * format used by the method [getRGB][.getRGB].
     * This integer can be supplied as an argument to the
     * `Color` constructor that takes a single integer argument.
     * @param     hue   the hue component of the color
     * @param     saturation   the saturation of the color
     * @param     brightness   the brightness of the color
     * @return    the RGB value of the color with the indicated hue,
     * saturation, and brightness.
     * @see java.awt.Color.getRGB
     * @see java.awt.Color.Color
     * @see java.awt.image.ColorModel.getRGBdefault
     * @since     1.0
     */
// From JDK
    fun HSBtoRGB(hue: Float, saturation: Float, brightness: Float): Int {
        var r = 0
        var g = 0
        var b = 0
        if (saturation == 0f) {
            b = (brightness * 255.0f + 0.5f).toInt()
            g = b
            r = g
        } else {
            val h: Float = (hue - floor(hue.toDouble()).toFloat()) * 6.0f
            val f: Float = h - floor(h.toDouble()).toFloat()
            val p = brightness * (1.0f - saturation)
            val q = brightness * (1.0f - saturation * f)
            val t = brightness * (1.0f - saturation * (1.0f - f))
            when (h.toInt()) {
                0 -> {
                    r = (brightness * 255.0f + 0.5f).toInt()
                    g = (t * 255.0f + 0.5f).toInt()
                    b = (p * 255.0f + 0.5f).toInt()
                }

                1 -> {
                    r = (q * 255.0f + 0.5f).toInt()
                    g = (brightness * 255.0f + 0.5f).toInt()
                    b = (p * 255.0f + 0.5f).toInt()
                }

                2 -> {
                    r = (p * 255.0f + 0.5f).toInt()
                    g = (brightness * 255.0f + 0.5f).toInt()
                    b = (t * 255.0f + 0.5f).toInt()
                }

                3 -> {
                    r = (p * 255.0f + 0.5f).toInt()
                    g = (q * 255.0f + 0.5f).toInt()
                    b = (brightness * 255.0f + 0.5f).toInt()
                }

                4 -> {
                    r = (t * 255.0f + 0.5f).toInt()
                    g = (p * 255.0f + 0.5f).toInt()
                    b = (brightness * 255.0f + 0.5f).toInt()
                }

                5 -> {
                    r = (brightness * 255.0f + 0.5f).toInt()
                    g = (p * 255.0f + 0.5f).toInt()
                    b = (q * 255.0f + 0.5f).toInt()
                }
            }
        }
        return -0x1000000 or (r shl 16) or (g shl 8) or (b shl 0)
    }
}