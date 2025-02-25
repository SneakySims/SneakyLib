import net.sneakysims.sneakylib.iff.IFF
import net.sneakysims.sneakylib.iff.IFFChunk
import net.sneakysims.sneakylib.iff.SPR2ChunkData
import net.sneakysims.sneakylib.iff.SPR2Utils
import java.io.File

fun main() {
    // I think it's a bug in the writer because the IFF can read images just fine
    val iff = IFF.read(File("furalhacc.flr").readBytes())
}