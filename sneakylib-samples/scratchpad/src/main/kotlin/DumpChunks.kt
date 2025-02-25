import net.sneakysims.sneakylib.iff.IFF
import net.sneakysims.sneakylib.iff.SPR2ChunkData
import java.io.File

fun main() {
    val iff = IFF.read(File("C:\\Program Files (x86)\\Maxis\\The Sims\\GameData\\Floors\\FloorZenGravel.flr").readBytes())

    println("Chunk Dumper:")
    iff.chunks.forEach {
        println("Chunk ID: ${it.id}")
        println("Chunk Code: ${it.code}")
        println("Chunk Flag: ${it.flags}")
        println("Chunk Name: ${it.name.joinToString("") { it.toInt().toChar().toString() }}")
        val data = it.data

        if (data is SPR2ChunkData) {
            println("Version: ${data.version}")
            println("Palette ID: ${data.paletteId}")
            for (sprite in data.sprites) {
                println("Flags: ${sprite.flags}")
                println("Unknown: ${sprite.unknown}")
                println("Overriden: ${sprite.overridenPaletteId}")
                println("Transparent Pixel Palette Index ID: ${sprite.transparentPixelPaletteIndexId}")
                println("OffsetX: ${sprite.offsetX}")
                println("OffsetY: ${sprite.offsetY}")
            }
        }
    }
}