import net.sneakysims.sneakylib.iff.PALTChunkData
import java.io.File

fun main() {
    val input = File("D:\\TheSims1ReverseEngineeringScratchPad\\PALT1537.bin")

    val palt = PALTChunkData.read(input.readBytes())

    println(palt)

    val result = palt.write()
    File("D:\\TheSims1ReverseEngineeringScratchPad\\PALT1537_rewritten.bin").writeBytes(result)
}