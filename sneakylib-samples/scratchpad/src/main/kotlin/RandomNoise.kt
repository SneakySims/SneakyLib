import net.perfectdreams.slippyimage.ColorUtils
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

fun main() {
    // This sucks but what you can do right?
    var colorIdx = 0
    val img = BufferedImage(128, 128, BufferedImage.TYPE_INT_RGB)
    val random = Random()

    for (x in 0 until img.width) {
        for (y in 0 until img.height) {
            val (r, g, b) = ColorUtils.unpackRGB(colorIdx++)

            img.setRGB(x, y, ColorUtils.packARGB(r, g, b, 255))
        }
    }

    ImageIO.write(img, "bmp", File("noise.bmp"))
}