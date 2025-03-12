package net.perfectdreams.harmony.math

import kotlin.math.sin

data class Vector3f(var x: Float, var y: Float, var z: Float) {
    constructor(vector3f: Vector3f) : this(vector3f.x, vector3f.y, vector3f.z)

    /**
     * Rotate this vector the specified radians around the Y axis.
     *
     * @param angle
     * the angle in radians
     * @return this
     */
    fun rotateY(angle: Float, dest: Vector3f = this): Vector3f {
        val sin = sin(angle.toDouble()).toFloat()
        val cos: Float = HarmonyMath.cosFromSin(sin, angle)
        val x = this.x * cos + this.z * sin
        val z = -this.x * sin + this.z * cos
        dest.x = x
        dest.y = this.y
        dest.z = z
        return dest
    }

    fun mulPositionGeneric(mat: Matrix4f, dest: Vector3f = this): Vector3f {
        val x = this.x
        val y = this.y
        val z = this.z
        dest.x = HarmonyMath.fma(mat.m00(), x, HarmonyMath.fma(mat.m10(), y, HarmonyMath.fma(mat.m20(), z, mat.m30())))
        dest.y = HarmonyMath.fma(mat.m01(), x, HarmonyMath.fma(mat.m11(), y, HarmonyMath.fma(mat.m21(), z, mat.m31())))
        dest.z = HarmonyMath.fma(mat.m02(), x, HarmonyMath.fma(mat.m12(), y, HarmonyMath.fma(mat.m22(), z, mat.m32())))
        return dest
    }
}