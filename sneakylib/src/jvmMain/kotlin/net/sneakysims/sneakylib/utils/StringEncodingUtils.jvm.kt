package net.sneakysims.sneakylib.utils

import java.nio.charset.Charset

private val WINDOWS_1252 = Charset.forName("Windows-1252")
private val WINDOWS_932 = Charset.forName("Windows-932")
private val WINDOWS_1250 = Charset.forName("Windows-1250")
private val WINDOWS_936 = Charset.forName("Windows-936")
private val WINDOWS_950 = Charset.forName("Windows-950")
private val WINDOWS_874 = Charset.forName("Windows-874")
private val WINDOWS_949 = Charset.forName("Windows-949")

actual fun String.encodeToByteArrayUsingWindows1252(): ByteArray {
    return this.toByteArray(WINDOWS_1252)
}

actual fun ByteArray.decodeToStringUsingWindows1252(): String {
    return this.toString(WINDOWS_1252)
}

actual fun String.encodeToByteArrayUsingWindows932(): ByteArray {
    return this.toByteArray(WINDOWS_932)
}

actual fun ByteArray.decodeToStringUsingWindows932(): String {
    return this.toString(WINDOWS_932)
}

actual fun String.encodeToByteArrayUsingWindows1250(): ByteArray {
    return this.toByteArray(WINDOWS_1250)
}

actual fun ByteArray.decodeToStringUsingWindows1250(): String {
    return this.toString(WINDOWS_1250)
}

actual fun String.encodeToByteArrayUsingWindows936(): ByteArray {
    return this.toByteArray(WINDOWS_936)
}

actual fun ByteArray.decodeToStringUsingWindows936(): String {
    return this.toString(WINDOWS_936)
}

actual fun String.encodeToByteArrayUsingWindows950(): ByteArray {
    return this.toByteArray(WINDOWS_950)
}

actual fun ByteArray.decodeToStringUsingWindows950(): String {
    return this.toString(WINDOWS_950)
}

actual fun String.encodeToByteArrayUsingWindows874(): ByteArray {
    return this.toByteArray(WINDOWS_874)
}

actual fun ByteArray.decodeToStringUsingWindows874(): String {
    return this.toString(WINDOWS_874)
}

actual fun String.encodeToByteArrayUsingWindows949(): ByteArray {
    return this.toByteArray(WINDOWS_949)
}

actual fun ByteArray.decodeToStringUsingWindows949(): String {
    return this.toString(WINDOWS_949)
}