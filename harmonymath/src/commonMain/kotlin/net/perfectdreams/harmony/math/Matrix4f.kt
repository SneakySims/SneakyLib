package net.perfectdreams.harmony.math

import kotlin.js.JsName
import kotlin.math.tan

class Matrix4f {
    @JsName("getM00")
    var m00: Float = 0f
    @JsName("getM01")
    var m01: Float = 0f
    @JsName("getM02")
    var m02: Float = 0f
    @JsName("getM03")
    var m03: Float = 0f

    @JsName("getM10")
    var m10: Float = 0f
    @JsName("getM11")
    var m11: Float = 0f
    @JsName("getM12")
    var m12: Float = 0f
    @JsName("getM13")
    var m13: Float = 0f

    @JsName("getM20")
    var m20: Float = 0f
    @JsName("getM21")
    var m21: Float = 0f
    @JsName("getM22")
    var m22: Float = 0f
    @JsName("getM23")
    var m23: Float = 0f

    @JsName("getM30")
    var m30: Float = 0f
    @JsName("getM31")
    var m31: Float = 0f
    @JsName("getM32")
    var m32: Float = 0f
    @JsName("getM33")
    var m33: Float = 0f

    init {
        this._m00(1.0f)
            ._m11(1.0f)
            ._m22(1.0f)
            ._m33(1.0f)
        // ._properties(PROPERTY_IDENTITY or PROPERTY_AFFINE or PROPERTY_TRANSLATION or PROPERTY_ORTHONORMAL)
    }

    constructor()

    constructor(source: Matrix4f) {
        this.m00 = source.m00
        this.m01 = source.m01
        this.m02 = source.m02
        this.m03 = source.m03
        this.m10 = source.m10
        this.m11 = source.m11
        this.m12 = source.m12
        this.m13 = source.m13
        this.m20 = source.m20
        this.m21 = source.m21
        this.m22 = source.m22
        this.m23 = source.m23
        this.m30 = source.m30
        this.m31 = source.m31
        this.m32 = source.m32
        this.m33 = source.m33
    }

    fun m00(): Float {
        return m00
    }

    fun m01(): Float {
        return m01
    }

    fun m02(): Float {
        return m02
    }

    fun m03(): Float {
        return m03
    }

    fun m10(): Float {
        return m10
    }

    fun m11(): Float {
        return m11
    }

    fun m12(): Float {
        return m12
    }

    fun m13(): Float {
        return m13
    }

    fun m20(): Float {
        return m20
    }

    fun m21(): Float {
        return m21
    }

    fun m22(): Float {
        return m22
    }

    fun m23(): Float {
        return m23
    }

    fun m30(): Float {
        return m30
    }

    fun m31(): Float {
        return m31
    }

    fun m32(): Float {
        return m32
    }

    fun m33(): Float {
        return m33
    }


    /**
     * Set the value of the matrix element at column 0 and row 0 without updating the properties of the matrix.
     *
     * @param m00
     * the new value
     * @return this
     */
    fun _m00(m00: Float): Matrix4f {
        this.m00 = m00
        return this
    }

    /**
     * Set the value of the matrix element at column 0 and row 1 without updating the properties of the matrix.
     *
     * @param m01
     * the new value
     * @return this
     */
    fun _m01(m01: Float): Matrix4f {
        this.m01 = m01
        return this
    }

    /**
     * Set the value of the matrix element at column 0 and row 2 without updating the properties of the matrix.
     *
     * @param m02
     * the new value
     * @return this
     */
    fun _m02(m02: Float): Matrix4f {
        this.m02 = m02
        return this
    }

    /**
     * Set the value of the matrix element at column 0 and row 3 without updating the properties of the matrix.
     *
     * @param m03
     * the new value
     * @return this
     */
    fun _m03(m03: Float): Matrix4f {
        this.m03 = m03
        return this
    }

    /**
     * Set the value of the matrix element at column 1 and row 0 without updating the properties of the matrix.
     *
     * @param m10
     * the new value
     * @return this
     */
    fun _m10(m10: Float): Matrix4f {
        this.m10 = m10
        return this
    }

    /**
     * Set the value of the matrix element at column 1 and row 1 without updating the properties of the matrix.
     *
     * @param m11
     * the new value
     * @return this
     */
    fun _m11(m11: Float): Matrix4f {
        this.m11 = m11
        return this
    }

    /**
     * Set the value of the matrix element at column 1 and row 2 without updating the properties of the matrix.
     *
     * @param m12
     * the new value
     * @return this
     */
    fun _m12(m12: Float): Matrix4f {
        this.m12 = m12
        return this
    }

    /**
     * Set the value of the matrix element at column 1 and row 3 without updating the properties of the matrix.
     *
     * @param m13
     * the new value
     * @return this
     */
    fun _m13(m13: Float): Matrix4f {
        this.m13 = m13
        return this
    }

    /**
     * Set the value of the matrix element at column 2 and row 0 without updating the properties of the matrix.
     *
     * @param m20
     * the new value
     * @return this
     */
    fun _m20(m20: Float): Matrix4f {
        this.m20 = m20
        return this
    }

    /**
     * Set the value of the matrix element at column 2 and row 1 without updating the properties of the matrix.
     *
     * @param m21
     * the new value
     * @return this
     */
    fun _m21(m21: Float): Matrix4f {
        this.m21 = m21
        return this
    }

    /**
     * Set the value of the matrix element at column 2 and row 2 without updating the properties of the matrix.
     *
     * @param m22
     * the new value
     * @return this
     */
    fun _m22(m22: Float): Matrix4f {
        this.m22 = m22
        return this
    }

    /**
     * Set the value of the matrix element at column 2 and row 3 without updating the properties of the matrix.
     *
     * @param m23
     * the new value
     * @return this
     */
    fun _m23(m23: Float): Matrix4f {
        this.m23 = m23
        return this
    }

    /**
     * Set the value of the matrix element at column 3 and row 0 without updating the properties of the matrix.
     *
     * @param m30
     * the new value
     * @return this
     */
    fun _m30(m30: Float): Matrix4f {
        this.m30 = m30
        return this
    }

    /**
     * Set the value of the matrix element at column 3 and row 1 without updating the properties of the matrix.
     *
     * @param m31
     * the new value
     * @return this
     */
    fun _m31(m31: Float): Matrix4f {
        this.m31 = m31
        return this
    }

    /**
     * Set the value of the matrix element at column 3 and row 2 without updating the properties of the matrix.
     *
     * @param m32
     * the new value
     * @return this
     */
    fun _m32(m32: Float): Matrix4f {
        this.m32 = m32
        return this
    }

    /**
     * Set the value of the matrix element at column 3 and row 3 without updating the properties of the matrix.
     *
     * @param m33
     * the new value
     * @return this
     */
    fun _m33(m33: Float): Matrix4f {
        this.m33 = m33
        return this
    }

    fun scale(x: Float, y: Float, z: Float): Matrix4f {
        return scaleGeneric(x, y, z, this)
    }

    private fun scaleGeneric(x: Float, y: Float, z: Float, dest: Matrix4f): Matrix4f {
        // val one = HarmonyMath.absEqualsOne(x) && HarmonyMath.absEqualsOne(y) && HarmonyMath.absEqualsOne(z)
        return dest
            ._m00(m00() * x)
            ._m01(m01() * x)
            ._m02(m02() * x)
            ._m03(m03() * x)
            ._m10(m10() * y)
            ._m11(m11() * y)
            ._m12(m12() * y)
            ._m13(m13() * y)
            ._m20(m20() * z)
            ._m21(m21() * z)
            ._m22(m22() * z)
            ._m23(m23() * z)
            ._m30(m30())
            ._m31(m31())
            ._m32(m32())
            ._m33(m33())
            /* ._properties(
                properties and ((PROPERTY_PERSPECTIVE or PROPERTY_IDENTITY or PROPERTY_TRANSLATION
                        or (if (one) 0 else PROPERTY_ORTHONORMAL))).inv()
            ) */
    }

    fun translate(vector3f: Vector3f): Matrix4f {
        return translate(vector3f.x, vector3f.y, vector3f.z)
    }

    fun translate(x: Float, y: Float, z: Float): Matrix4f {
        return translateGeneric(x, y, z)
    }
    
    private fun translateGeneric(x: Float, y: Float, z: Float): Matrix4f {
        return this
            ._m30(HarmonyMath.fma(m00(), x, HarmonyMath.fma(m10(), y, HarmonyMath.fma(m20(), z, m30()))))
            ._m31(HarmonyMath.fma(m01(), x, HarmonyMath.fma(m11(), y, HarmonyMath.fma(m21(), z, m31()))))
            ._m32(HarmonyMath.fma(m02(), x, HarmonyMath.fma(m12(), y, HarmonyMath.fma(m22(), z, m32()))))
            ._m33(HarmonyMath.fma(m03(), x, HarmonyMath.fma(m13(), y, HarmonyMath.fma(m23(), z, m33()))))
            // ._properties(properties and (PROPERTY_PERSPECTIVE or PROPERTY_IDENTITY).inv())
    }

    fun perspective(fovy: Float, aspect: Float, zNear: Float, zFar: Float, zZeroToOne: Boolean = false, dest: Matrix4f = this) = perspectiveGeneric(fovy, aspect, zNear, zFar, false, this)

    private fun perspectiveGeneric(
        fovy: Float,
        aspect: Float,
        zNear: Float,
        zFar: Float,
        zZeroToOne: Boolean,
        dest: Matrix4f
    ): Matrix4f {
        val h = tan((fovy * 0.5f).toDouble()).toFloat()
        // calculate right matrix elements
        val rm00 = 1.0f / (h * aspect)
        val rm11 = 1.0f / h
        val rm22: Float
        val rm32: Float
        val farInf = zFar > 0 && zFar.isInfinite()
        val nearInf = zNear > 0 && zNear.isInfinite()
        if (farInf) {
            // See: "Infinite Projection Matrix" (http://www.terathon.com/gdc07_lengyel.pdf)
            val e = 1E-6f
            rm22 = e - 1.0f
            rm32 = (e - (if (zZeroToOne) 1.0f else 2.0f)) * zNear
        } else if (nearInf) {
            val e = 1E-6f
            rm22 = (if (zZeroToOne) 0.0f else 1.0f) - e
            rm32 = ((if (zZeroToOne) 1.0f else 2.0f) - e) * zFar
        } else {
            rm22 = (if (zZeroToOne) zFar else zFar + zNear) / (zNear - zFar)
            rm32 = (if (zZeroToOne) zFar else zFar + zFar) * zNear / (zNear - zFar)
        }
        // perform optimized matrix multiplication
        val nm20 = m20() * rm22 - m30()
        val nm21 = m21() * rm22 - m31()
        val nm22 = m22() * rm22 - m32()
        val nm23 = m23() * rm22 - m33()
        dest._m00(m00() * rm00)
            ._m01(m01() * rm00)
            ._m02(m02() * rm00)
            ._m03(m03() * rm00)
            ._m10(m10() * rm11)
            ._m11(m11() * rm11)
            ._m12(m12() * rm11)
            ._m13(m13() * rm11)
            ._m30(m20() * rm32)
            ._m31(m21() * rm32)
            ._m32(m22() * rm32)
            ._m33(m23() * rm32)
            ._m20(nm20)
            ._m21(nm21)
            ._m22(nm22)
            ._m23(nm23)
            // ._properties(properties and (PROPERTY_AFFINE or PROPERTY_IDENTITY or PROPERTY_TRANSLATION or PROPERTY_ORTHONORMAL).inv())
        return dest
    }

    fun lookAt(
        eye: Vector3f,
        center: Vector3f,
        up: Vector3f,
        dest: Matrix4f = this
    ) = lookAtGeneric(eye.x, eye.y, eye.z, center.x, center.y, center.z, up.x, up.y, up.z, dest)

    fun lookAt(
        eyeX: Float, eyeY: Float, eyeZ: Float,
        centerX: Float, centerY: Float, centerZ: Float,
        upX: Float, upY: Float, upZ: Float, dest: Matrix4f = this
    ) = lookAtGeneric(eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ, dest)

    private fun lookAtGeneric(
        eyeX: Float, eyeY: Float, eyeZ: Float,
        centerX: Float, centerY: Float, centerZ: Float,
        upX: Float, upY: Float, upZ: Float, dest: Matrix4f
    ): Matrix4f {
        // Compute direction from position to lookAt
        var dirX: Float
        var dirY: Float
        var dirZ: Float
        dirX = eyeX - centerX
        dirY = eyeY - centerY
        dirZ = eyeZ - centerZ
        // Normalize direction
        val invDirLength: Float = HarmonyMath.invsqrt(dirX * dirX + dirY * dirY + dirZ * dirZ)
        dirX *= invDirLength
        dirY *= invDirLength
        dirZ *= invDirLength
        // left = up x direction
        var leftX: Float
        var leftY: Float
        var leftZ: Float
        leftX = upY * dirZ - upZ * dirY
        leftY = upZ * dirX - upX * dirZ
        leftZ = upX * dirY - upY * dirX
        // normalize left
        val invLeftLength: Float = HarmonyMath.invsqrt(leftX * leftX + leftY * leftY + leftZ * leftZ)
        leftX *= invLeftLength
        leftY *= invLeftLength
        leftZ *= invLeftLength
        // up = direction x left
        val upnX = dirY * leftZ - dirZ * leftY
        val upnY = dirZ * leftX - dirX * leftZ
        val upnZ = dirX * leftY - dirY * leftX

        // calculate right matrix elements
        val rm30 = -(leftX * eyeX + leftY * eyeY + leftZ * eyeZ)
        val rm31 = -(upnX * eyeX + upnY * eyeY + upnZ * eyeZ)
        val rm32 = -(dirX * eyeX + dirY * eyeY + dirZ * eyeZ)
        // introduce temporaries for dependent results
        val nm00 = m00() * leftX + m10() * upnX + m20() * dirX
        val nm01 = m01() * leftX + m11() * upnX + m21() * dirX
        val nm02 = m02() * leftX + m12() * upnX + m22() * dirX
        val nm03 = m03() * leftX + m13() * upnX + m23() * dirX
        val nm10 = m00() * leftY + m10() * upnY + m20() * dirY
        val nm11 = m01() * leftY + m11() * upnY + m21() * dirY
        val nm12 = m02() * leftY + m12() * upnY + m22() * dirY
        val nm13 = m03() * leftY + m13() * upnY + m23() * dirY

        // perform optimized matrix multiplication
        // compute last column first, because others do not depend on it
        return dest
            ._m30(m00() * rm30 + m10() * rm31 + m20() * rm32 + m30())
            ._m31(m01() * rm30 + m11() * rm31 + m21() * rm32 + m31())
            ._m32(m02() * rm30 + m12() * rm31 + m22() * rm32 + m32())
            ._m33(m03() * rm30 + m13() * rm31 + m23() * rm32 + m33())
            ._m20(m00() * leftZ + m10() * upnZ + m20() * dirZ)
            ._m21(m01() * leftZ + m11() * upnZ + m21() * dirZ)
            ._m22(m02() * leftZ + m12() * upnZ + m22() * dirZ)
            ._m23(m03() * leftZ + m13() * upnZ + m23() * dirZ)
            ._m00(nm00)
            ._m01(nm01)
            ._m02(nm02)
            ._m03(nm03)
            ._m10(nm10)
            ._m11(nm11)
            ._m12(nm12)
            ._m13(nm13)
            // ._properties(properties and (PROPERTY_PERSPECTIVE or PROPERTY_IDENTITY or PROPERTY_TRANSLATION).inv())
    }

    fun rotate(quat: Quaternionf, dest: Matrix4f = this): Matrix4f = rotateGeneric(quat, dest)

    private fun rotateGeneric(quat: Quaternionf, dest: Matrix4f): Matrix4f {
        val w2: Float = quat.w() * quat.w()
        val x2: Float = quat.x() * quat.x()
        val y2: Float = quat.y() * quat.y()
        val z2: Float = quat.z() * quat.z()
        val zw: Float = quat.z() * quat.w()
        val dzw = zw + zw
        val xy: Float = quat.x() * quat.y()
        val dxy = xy + xy
        val xz: Float = quat.x() * quat.z()
        val dxz = xz + xz
        val yw: Float = quat.y() * quat.w()
        val dyw = yw + yw
        val yz: Float = quat.y() * quat.z()
        val dyz = yz + yz
        val xw: Float = quat.x() * quat.w()
        val dxw = xw + xw
        val rm00 = w2 + x2 - z2 - y2
        val rm01 = dxy + dzw
        val rm02 = dxz - dyw
        val rm10 = -dzw + dxy
        val rm11 = y2 - z2 + w2 - x2
        val rm12 = dyz + dxw
        val rm20 = dyw + dxz
        val rm21 = dyz - dxw
        val rm22 = z2 - y2 - x2 + w2
        val nm00: Float = HarmonyMath.fma(m00(), rm00, HarmonyMath.fma(m10(), rm01, m20() * rm02))
        val nm01: Float = HarmonyMath.fma(m01(), rm00, HarmonyMath.fma(m11(), rm01, m21() * rm02))
        val nm02: Float = HarmonyMath.fma(m02(), rm00, HarmonyMath.fma(m12(), rm01, m22() * rm02))
        val nm03: Float = HarmonyMath.fma(m03(), rm00, HarmonyMath.fma(m13(), rm01, m23() * rm02))
        val nm10: Float = HarmonyMath.fma(m00(), rm10, HarmonyMath.fma(m10(), rm11, m20() * rm12))
        val nm11: Float = HarmonyMath.fma(m01(), rm10, HarmonyMath.fma(m11(), rm11, m21() * rm12))
        val nm12: Float = HarmonyMath.fma(m02(), rm10, HarmonyMath.fma(m12(), rm11, m22() * rm12))
        val nm13: Float = HarmonyMath.fma(m03(), rm10, HarmonyMath.fma(m13(), rm11, m23() * rm12))
        return dest
            ._m20(HarmonyMath.fma(m00(), rm20, HarmonyMath.fma(m10(), rm21, m20() * rm22)))
            ._m21(HarmonyMath.fma(m01(), rm20, HarmonyMath.fma(m11(), rm21, m21() * rm22)))
            ._m22(HarmonyMath.fma(m02(), rm20, HarmonyMath.fma(m12(), rm21, m22() * rm22)))
            ._m23(HarmonyMath.fma(m03(), rm20, HarmonyMath.fma(m13(), rm21, m23() * rm22)))
            ._m00(nm00)
            ._m01(nm01)
            ._m02(nm02)
            ._m03(nm03)
            ._m10(nm10)
            ._m11(nm11)
            ._m12(nm12)
            ._m13(nm13)
            ._m30(m30())
            ._m31(m31())
            ._m32(m32())
            ._m33(m33())
            // ._properties(properties and (PROPERTY_PERSPECTIVE or PROPERTY_IDENTITY or PROPERTY_TRANSLATION).inv())
    }

    fun getAsFloatArray() = get(FloatArray(16), 0)

    fun get(arr: FloatArray, offset: Int): FloatArray {
        arr[offset + 0] = m00
        arr[offset + 1] = m01
        arr[offset + 2] = m02
        arr[offset + 3] = m03

        arr[offset + 4] = m10
        arr[offset + 5] = m11
        arr[offset + 6] = m12
        arr[offset + 7] = m13

        arr[offset + 8] = m20
        arr[offset + 9] = m21
        arr[offset + 10] = m22
        arr[offset + 11] = m23

        arr[offset + 12] = m30
        arr[offset + 13] = m31
        arr[offset + 14] = m32
        arr[offset + 15] = m33

        return arr
    }
}