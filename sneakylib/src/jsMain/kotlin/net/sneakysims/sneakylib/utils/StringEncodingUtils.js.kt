package net.sneakysims.sneakylib.utils

import js.typedarrays.toUint8Array

private val WINDOWS_1252 = "Windows-1252"
private val WINDOWS_932 = "Windows-932"
private val WINDOWS_1250 = "Windows-1250"
private val WINDOWS_936 = "Windows-936"
private val WINDOWS_950 = "Windows-950"
private val WINDOWS_874 = "Windows-874"
private val WINDOWS_949 = "Windows-949"

actual fun String.encodeToByteArrayUsingWindows1252(): ByteArray {
    return encode(this, WINDOWS_1252.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows1252(): String {
    return decode(this.toUint8Array(), WINDOWS_1252.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows932(): ByteArray {
    return encode(this, WINDOWS_932.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows932(): String {
    return decode(this.toUint8Array(), WINDOWS_932.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows1250(): ByteArray {
    return encode(this, WINDOWS_1250.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows1250(): String {
    return decode(this.toUint8Array(), WINDOWS_1250.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows936(): ByteArray {
    return encode(this, WINDOWS_936.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows936(): String {
    return decode(this.toUint8Array(), WINDOWS_936.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows950(): ByteArray {
    return encode(this, WINDOWS_950.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows950(): String {
    return decode(this.toUint8Array(), WINDOWS_950.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows874(): ByteArray {
    return encode(this, WINDOWS_874.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows874(): String {
    return decode(this.toUint8Array(), WINDOWS_874.lowercase())
}

actual fun String.encodeToByteArrayUsingWindows949(): ByteArray {
    return encode(this, WINDOWS_949.lowercase()).toByteArray()
}

actual fun ByteArray.decodeToStringUsingWindows949(): String {
    return decode(this.toUint8Array(), WINDOWS_949.lowercase())
}