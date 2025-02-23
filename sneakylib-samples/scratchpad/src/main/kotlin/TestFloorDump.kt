import net.sneakysims.sneakylib.iff.IFF
import net.sneakysims.sneakylib.iff.IFFChunk
import net.sneakysims.sneakylib.iff.IFFChunkData
import net.sneakysims.sneakylib.iff.SPR2ChunkData
import net.sneakysims.sneakylib.utils.decodeToStringUsingWindows1252
import java.io.File

fun main() {
    val iff = IFF.read(
        File("C:\\Program Files (x86)\\Steam\\steamapps\\common\\The Sims Legacy Collection\\GameData\\Floors\\green_floor_high.flr")
            .readBytes()
    )

    iff.chunks.filter { it.code == IFFChunk.SPR2_CHUNK_CODE }
        .forEach {
            println("ID: ${it.id}")
            println("Flags: ${it.flags}")
            println("Name: ${it.name.decodeToStringUsingWindows1252()}")

            val data = it.data
            if (data is SPR2ChunkData) {
                println("Version: ${data.version}")

                for (spr in data.sprites) {
                    println(spr.overridenPaletteId)
                    println(spr.unknown)
                    println(spr.flags)
                    println(spr.transparentPixelPaletteIndexId)
                }
            }
        }
}