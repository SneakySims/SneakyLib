package net.sneakysims.sneakylib.refpack

// This is a Kotlin port of FreeSO's "Decompresser" class
// Ported by ChatGPT to Java (which, surprisingly, ported it correctly?!) and then ported by me to Kotlin
// https://github.com/riperiperi/FreeSO/blob/master/Other/tools/SimsLib/SimsLib/FAR3/Decompresser.cs
class Decompresser {
    private var compressedSize: Long = 0
    private var decompressedSize: Long = 0
    private var compressed = false

    fun getDecompressedSize(): Long {
        return decompressedSize
    }

    fun setDecompressedSize(decompressedSize: Long) {
        println("Setting setDecompressedSize to " + decompressedSize)
        this.decompressedSize = decompressedSize
    }

    // Mimicks Java's System.lang.arraycopy
    fun arraycopy(src: ByteArray, srcPos: Int, dest: ByteArray, destPos: Int, length: Int) {
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

    fun decompress(data: ByteArray): ByteArray {
        compressed = false
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
        println("Returning as is")
        return data
    }
}