import net.sneakysims.sneakylib.far.FAR3
import java.io.File

fun main() {
    val farBytes = File("C:\\Games\\SimNation\\TSOData\\TSOClient\\uigraphics\\housepage\\housepage.dat").readBytes()

    val far3 = FAR3.read(farBytes)

    var idx = 0
    for (file in far3.files) {
        println("Writing ${file.fileName}")
        File("far3test/${file.fileName}").writeBytes(file.content)
        idx++
    }
}