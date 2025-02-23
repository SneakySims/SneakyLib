package net.perfectdreams.slippyimage

data class Color(val red: UByte, val green: UByte, val blue: UByte) {
    constructor(red: Int, green: Int, blue: Int) : this(red.toUByte(), green.toUByte(), blue.toUByte())

    val rgba
        get() = (255 shl 24) or (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt()

    fun getRGBAWithAlpha(alpha: Int): Int {
        return (alpha shl 24) or (red.toInt() shl 16) or (green.toInt() shl 8) or blue.toInt()
    }
}