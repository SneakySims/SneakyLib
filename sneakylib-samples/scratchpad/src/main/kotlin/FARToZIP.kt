import net.sneakysims.sneakylib.far.FAR1a
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

// Converts a FAR file to a ZIP file
fun main() {
    val input = File("C:\\Program Files (x86)\\Steam\\steamapps\\common\\The Sims Legacy Collection\\ExpansionPack7\\ExpansionPack7.far")

    FAR1a.read(input.readBytes())
        .files
        .associate {
            it.fileName to it.content
        }
        .let {
            createZipFromMap(it)
        }
        .let {
            File("ExpansionPack7.zip").writeBytes(it)
        }
}

fun createZipFromMap(files: Map<String, ByteArray>): ByteArray {
    val outputStream = ByteArrayOutputStream()
    ZipOutputStream(outputStream).use { zipStream ->
        for ((fileName, content) in files) {
            val entry = ZipEntry(fileName)
            zipStream.putNextEntry(entry)
            zipStream.write(content)
            zipStream.closeEntry()
        }
    }
    return outputStream.toByteArray()
}