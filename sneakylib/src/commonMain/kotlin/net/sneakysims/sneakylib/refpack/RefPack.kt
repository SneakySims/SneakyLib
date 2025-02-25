package net.sneakysims.sneakylib.refpack

// This is a Kotlin port of FreeSO's "Decompresser" class
// Ported by ChatGPT to Java (which, surprisingly, ported it correctly?!) and then ported by me to Kotlin
// https://github.com/riperiperi/FreeSO/blob/master/Other/tools/SimsLib/SimsLib/FAR3/Decompresser.cs
// Don't ask me how it works because I have spent way too much looking at RefPack implementations and I still
// can't wrap my head around it.
/**
 * Tools related to EA Games' RefPack compression
 *
 * http://wiki.niotso.org/RefPack
 */
object RefPack {
    // Mimicks Java's System.lang.arraycopy
    private fun arraycopy(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
        src.copyInto(dest, destinationOffset = destPos, startIndex = srcPos, endIndex = srcPos + length)
    }

    private fun arrayCopy2(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Long) {
        var dest = dest
        if (dest.size < destPos + length) {
            val destExt = ByteArray((destPos + length).toInt())
            arraycopy(dest, 0, destExt, 0, dest.size)
            dest = destExt
        }
        for (i in 0..<length) {
            dest[(destPos + i).toInt()] = src[(srcPos + i).toInt()]
        }
    }

    private fun offsetCopy(array: ByteArray, srcPos: Int, destPos: Int, length: Long) {
        var array = array
        var srcPos = srcPos
        srcPos = destPos - srcPos
        if (array.size < destPos + length) {
            val newArray = ByteArray((destPos + length).toInt())
            arraycopy(array, 0, newArray, 0, array.size)
            array = newArray
        }
        for (i in 0..<length) {
            array[(destPos + i).toInt()] = array[(srcPos + i).toInt()]
        }
    }

    // TODO: *please* add some comments, I know that FreeSO's original implementation also had a lack of comments too, but maybe we can do better...?
    //  (the narrator: no they can't)
    //  but it doesn't hurt to tryyyy right???
    fun decompress(data: ByteArray, decompressedSize: Int): ByteArray {
        var compressed = false
        if (data.size > 6) {
            val decompressedData = ByteArray(decompressedSize.toInt())
            var dataPos = 0
            compressed = true
            var pos = 0
            var control1: Long = 0

            while (control1 != 0xFCL && pos < data.size) {
                control1 = (data[pos++].toInt() and 0xFF).toLong()
                if (pos == data.size) break

                if (control1 <= 127) {
                    // 0x00 - 0x7F
                    val control2 = (data[pos++].toInt() and 0xFF).toLong()
                    val numPlainText = control1 and 0x03L
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText)
                    dataPos += numPlainText.toInt()
                    pos += numPlainText.toInt()

                    if (dataPos == decompressedData.size) break

                    val offset = (((control1 and 0x60L) shl 3) + control2.toInt() + 1)
                    val numToCopy = ((control1 and 0x1CL) shr 2) + 3
                    offsetCopy(decompressedData, offset.toInt(), dataPos, numToCopy)
                    dataPos += numToCopy.toInt()

                    if (dataPos == decompressedData.size) break
                } else if (control1 <= 191) {
                    // 0x80 - 0xBF
                    val control2 = (data[pos++].toInt() and 0xFF).toLong()
                    val control3 = (data[pos++].toInt() and 0xFF).toLong()
                    val numPlainText = (control2 shr 6) and 0x03L
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText)
                    dataPos += numPlainText.toInt()
                    pos += numPlainText.toInt()

                    if (dataPos == decompressedData.size) break

                    val offset = ((control2.toInt() and 0x3F) shl 8) + control3.toInt() + 1
                    val numToCopy = (control1 and 0x3FL) + 4
                    offsetCopy(decompressedData, offset, dataPos, numToCopy)
                    dataPos += numToCopy.toInt()

                    if (dataPos == decompressedData.size) break
                } else if (control1 <= 223) {
                    // 0xC0 - 0xDF
                    val numPlainText = control1 and 0x03L
                    val control2 = (data[pos++].toInt() and 0xFF).toLong()
                    val control3 = (data[pos++].toInt() and 0xFF).toLong()
                    val control4 = (data[pos++].toInt() and 0xFF).toLong()
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText)
                    dataPos += numPlainText.toInt()
                    pos += numPlainText.toInt()

                    if (dataPos == decompressedData.size) break

                    val offset = (((control1 and 0x10L) shl 12) + (control2.toInt() shl 8) + control3.toInt() + 1)
                    val numToCopy = ((control1 and 0x0CL) shl 6) + control4 + 5
                    offsetCopy(decompressedData, offset.toInt(), dataPos, numToCopy)
                    dataPos += numToCopy.toInt()

                    if (dataPos == decompressedData.size) break
                } else if (control1 <= 251) {
                    // 0xE0 - 0xFB
                    val numPlainText = ((control1 and 0x1FL) shl 2) + 4
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText)
                    dataPos += numPlainText.toInt()
                    pos += numPlainText.toInt()

                    if (dataPos == decompressedData.size) break
                } else {
                    val numPlainText = control1 and 0x03L
                    arrayCopy2(data, pos, decompressedData, dataPos, numPlainText)
                    dataPos += numPlainText.toInt()
                    pos += numPlainText.toInt()

                    if (dataPos == decompressedData.size) break
                }
            }
            return decompressedData
        }
        return data
    }
}