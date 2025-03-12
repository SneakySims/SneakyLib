package net.perfectdreams.harmony.math

import kotlin.js.JsName

data class Quaternionf(var x: Float, var y: Float, var z: Float, var w: Float) {
    @JsName("getX")
    fun x() = this.x
    @JsName("getY")
    fun y() = this.y
    @JsName("getZ")
    fun z() = this.z
    @JsName("getW")
    fun w() = this.w

    constructor(source: Quaternionf) : this(source.x, source.y, source.z, source.w)

    fun invert(dest: Quaternionf = this): Quaternionf {
        val invNorm: Float = 1.0f / HarmonyMath.fma(x, x, HarmonyMath.fma(y, y, HarmonyMath.fma(z, z, w * w)))
        dest.x = -x * invNorm
        dest.y = -y * invNorm
        dest.z = -z * invNorm
        dest.w = w * invNorm
        return dest
    }
}