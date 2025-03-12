package net.perfectdreams.harmony.math

import kotlin.test.Test

class Matrix4fJOMLComparisons {
    @Test
    fun `check identity`() {
        val harmony = Matrix4f()
        val joml = org.joml.Matrix4f()

        assertIfEqual(harmony.getAsFloatArray(), joml.get(FloatArray(16)))
    }

    @Test
    fun `check scale`() {
        val harmony = Matrix4f()
            .scale(4f, 4f, 4f)
        val joml = org.joml.Matrix4f()
            .scale(4f, 4f, 4f)

        assertIfEqual(harmony.getAsFloatArray(), joml.get(FloatArray(16)))
    }

    @Test
    fun `check translation`() {
        val harmony = Matrix4f()
            .translate(1f, 2f, 3f)
        val joml = org.joml.Matrix4f()
            .translate(1f, 2f, 3f)

        assertIfEqual(harmony.getAsFloatArray(), joml.get(FloatArray(16)))
    }

    @Test
    fun `check perspective`() {
        val harmony = Matrix4f()
            .perspective(Math.toRadians(50.0).toFloat(), 640f / 360f, 0.1f, 100.0f)
        val joml = org.joml.Matrix4f()
            .perspective(Math.toRadians(50.0).toFloat(), 640f / 360f, 0.1f, 100.0f)

        assertIfEqual(harmony.getAsFloatArray(), joml.get(FloatArray(16)))
    }

    @Test
    fun `check quaternion rotation`() {
        val harmony = Matrix4f()
            .rotate(Quaternionf(1f, 0.333f, 0.111f, 1f))
        val joml = org.joml.Matrix4f()
            .rotate(org.joml.Quaternionf(1f, 0.333f, 0.111f, 1f))

        assertIfEqual(harmony.getAsFloatArray(), joml.get(FloatArray(16)))
    }

    fun assertIfEqual(harmonyFloatArray: FloatArray, jomlFloatArray: FloatArray) {
        // We do this because Harmony's Matrix4f has some negative floats in it
        // It doesn't matter because -0f == 0f, but contentEquals does not like it
        for (i in 0 until 16) {
            val a = harmonyFloatArray[i]
            val b = jomlFloatArray[i]

            if (a != b) {
                error("Matrix is not equal!\nHarmony: ${harmonyFloatArray.joinToString(", ")};\nJOML:    ${jomlFloatArray.joinToString(", ")}")
                return
            }
        }
    }
}