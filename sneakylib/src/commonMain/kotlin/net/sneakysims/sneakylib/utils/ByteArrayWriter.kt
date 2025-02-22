package net.sneakysims.sneakylib.utils

import net.sneakysims.sneakylib.utils.BinaryUtils.reverseBytes

class ByteArrayWriter {
    // TODO: Position?
    val bytes = mutableListOf<Byte>()

    fun writeShort(value: Short) {
        val valueAsInt = value.toInt()

        val byte0 = (valueAsInt shr 8).toByte()
        val byte1 = valueAsInt.toByte()

        bytes.add(byte0)
        bytes.add(byte1)
    }

    fun writeShortLe(value: Short) {
        writeShort(value.reverseBytes())
    }

    fun writeUShortLe(value: UShort) {
        writeShortLe(value.toShort())
    }

    fun writeInt(value: Int) {
        val byte0 = (value shr 24).toByte()
        val byte1 = (value shr 16).toByte()
        val byte2 = (value shr 8).toByte()
        val byte3 = value.toByte()

        bytes.add(byte0)
        bytes.add(byte1)
        bytes.add(byte2)
        bytes.add(byte3)
    }

    fun writeIntLe(value: Int) {
        writeInt(value.reverseBytes())
    }

    fun writeLong(value: Long) {
        val byte0 = (value shr 56).toByte()
        val byte1 = (value shr 48).toByte()
        val byte2 = (value shr 40).toByte()
        val byte3 = (value shr 32).toByte()
        val byte4 = (value shr 24).toByte()
        val byte5 = (value shr 16).toByte()
        val byte6 = (value shr 8).toByte()
        val byte7 = value.toByte()

        bytes.add(byte0)
        bytes.add(byte1)
        bytes.add(byte2)
        bytes.add(byte3)
        bytes.add(byte4)
        bytes.add(byte5)
        bytes.add(byte6)
        bytes.add(byte7)
    }

    fun writeLongLe(value: Long) {
        writeLong(value.reverseBytes())
    }

    fun writeByte(value: Byte) {
        this.bytes.add(value)
    }

    fun writeUByte(value: UByte) {
        this.bytes.add(value.toByte())
    }

    fun writeBytes(bytes: ByteArray) {
        for (byte in bytes) {
            writeByte(byte)
        }
    }

    fun asByteArray() = bytes.toByteArray()
}