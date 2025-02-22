import net.sneakysims.sneakylib.far.FAR1a
import java.io.File

fun main() {
    val input = File("C:\\Games\\SimNation\\Content\\Walls\\wfsobrtsobr3.far")

    val far = FAR1a.read(input.readBytes())

    for (file in far.files) {
        println(file.fileName)
    }

    File("ExpansionPack7_rewritten.far")
        .writeBytes(far.write())
}