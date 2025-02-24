package net.sneakysims.sneakylib.utils

import net.sneakysims.sneakylib.utils.BinaryUtils.reverseBytes

/**
 * A reader that reads a ByteArray, similar to Java's ByteBuffer
 *
 * We don't use kotlinx.io's Buffer because it doesn't support rewinding
 */
class ByteArrayReader(val byteArray: ByteArray) {
    var position = 0
    val remaining
        get() = byteArray.size - this.position

    fun readInt(): Int {
        val byte0 = this.byteArray[this.position++]
        val byte1 = this.byteArray[this.position++]
        val byte2 = this.byteArray[this.position++]
        val byte3 = this.byteArray[this.position++]

        return (byte0.toInt() and 0xFF shl 24) or (byte1.toInt() and 0xFF shl 16) or (byte2.toInt() and 0xFF shl 8) or (byte3.toInt() and 0xFF)
    }

    fun readIntLe(): Int {
        return readInt().reverseBytes()
    }

    fun readUIntLe(): UInt {
        return readIntLe().toUInt()
    }

    fun readByte(): Byte {
        return this.byteArray[this.position++]
    }

    fun readUByte(): UByte {
        return readByte().toUByte()
    }

    fun readShort(): Short {
        val byte0 = this.byteArray[this.position++]
        val byte1 = this.byteArray[this.position++]

        return ((byte0.toInt() and 0xFF shl 8) or (byte1.toInt() and 0xFF)).toShort()
    }

    fun readShortLe(): Short {
        return readShort().reverseBytes()
    }

    fun readUShortLe(): UShort {
        return readShort().reverseBytes().toUShort()
    }

    fun readBytes(length: Int): ByteArray {
        if (length + this.position > this.byteArray.size)
            error("Trying to read more bytes than what's present! ${length + this.position} > ${this.byteArray.size}")
        val target = ByteArray(length)

        repeat(length) {
            target[it] = byteArray[this.position]
            this.position++
        }

        return target
    }

    fun readLong(): Long {
        val byte0 = this.byteArray[this.position++]
        val byte1 = this.byteArray[this.position++]
        val byte2 = this.byteArray[this.position++]
        val byte3 = this.byteArray[this.position++]
        val byte4 = this.byteArray[this.position++]
        val byte5 = this.byteArray[this.position++]
        val byte6 = this.byteArray[this.position++]
        val byte7 = this.byteArray[this.position++]

        return (byte0.toLong() and 0xFF shl 56) or (byte1.toLong() and 0xFF shl 48) or (byte2.toLong() and 0xFF shl 40) or (byte3.toLong() and 0xFF shl 32) or (byte4.toLong() and 0xFF shl 24) or (byte5.toLong() and 0xFF shl 16) or (byte6.toLong() and 0xFF shl 8) or (byte7.toLong() and 0xFF)
    }

    fun readLongLe(): Long {
        return readLong().reverseBytes()
    }

    fun readBytesUntilNull(): ByteArray {
        val builder = mutableListOf<Byte>()
        while (true) {
            val b = this.readByte()
            print(b.toInt().toChar().toString())
            if (b == 0x00.toByte())
                break
            builder.add(b)
        }
        return builder.toByteArray()
    }

    fun jumpTo(position: Int) {
        this.position = position
    }

    fun hasRemaining() = byteArray.size > this.position
}