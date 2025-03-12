package net.perfectdreams.harmony.math

import kotlin.math.PI
import kotlin.math.sqrt

object HarmonyMath {
    const val PI_f = PI.toFloat()
    const val PI_OVER_2_f = (PI * 0.5).toFloat()
    const val PI_TIMES_2_f = PI_f * 2.0f
    private const val DEGREES_TO_RADIANS: Double = 0.017453292519943295

    fun fma(a: Float, b: Float, c: Float): Float {
        return a * b + c
    }

    fun cosFromSin(sin: Float, angle: Float): Float {
        // sin(x)^2 + cos(x)^2 = 1
        val cos: Float = sqrt(1.0f - sin * sin)
        val a: Float = angle + PI_OVER_2_f
        var b: Float = a - (a / PI_TIMES_2_f).toInt() * PI_TIMES_2_f
        if (b < 0.0) b = PI_TIMES_2_f + b
        if (b >= PI_f) return -cos
        return cos
    }

    fun invsqrt(r: Float): Float {
        return 1.0f / sqrt(r)
    }

    fun toRadians(angdeg: Double): Double {
        return angdeg * DEGREES_TO_RADIANS
    }
}