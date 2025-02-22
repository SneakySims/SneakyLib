package net.sneakysims.sneakylib.sims

import net.sneakysims.sneakylib.utils.*

sealed class TheSimsLanguage(val code: Byte) {
    abstract fun encodeStringForIFF(input: String): ByteArray
    abstract fun decodeStringFromIFF(input: ByteArray): String

    object Unknown : net.sneakysims.sneakylib.sims.TheSimsLanguage(0) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object EnglishUS : net.sneakysims.sneakylib.sims.TheSimsLanguage(1) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object EnglishUK : net.sneakysims.sneakylib.sims.TheSimsLanguage(2) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object French : net.sneakysims.sneakylib.sims.TheSimsLanguage(3) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object German : net.sneakysims.sneakylib.sims.TheSimsLanguage(4) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Italian : net.sneakysims.sneakylib.sims.TheSimsLanguage(5) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Spanish : net.sneakysims.sneakylib.sims.TheSimsLanguage(6) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Dutch : net.sneakysims.sneakylib.sims.TheSimsLanguage(7) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Danish : net.sneakysims.sneakylib.sims.TheSimsLanguage(8) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Swedish : net.sneakysims.sneakylib.sims.TheSimsLanguage(9) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Norwegian : net.sneakysims.sneakylib.sims.TheSimsLanguage(10) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Finnish : net.sneakysims.sneakylib.sims.TheSimsLanguage(11) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Hebrew : net.sneakysims.sneakylib.sims.TheSimsLanguage(12) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Russian : net.sneakysims.sneakylib.sims.TheSimsLanguage(13) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Portuguese : net.sneakysims.sneakylib.sims.TheSimsLanguage(14) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1252()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1252()
        }
    }

    object Japanese : net.sneakysims.sneakylib.sims.TheSimsLanguage(15) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows932()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows932()
        }
    }

    object Polish : net.sneakysims.sneakylib.sims.TheSimsLanguage(16) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows1250()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows1250()
        }
    }

    object SimplifiedChinese : net.sneakysims.sneakylib.sims.TheSimsLanguage(17) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows936()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows936()
        }
    }

    object TraditionalChinese : net.sneakysims.sneakylib.sims.TheSimsLanguage(18) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows950()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows950()
        }
    }

    object Thai : net.sneakysims.sneakylib.sims.TheSimsLanguage(19) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows874()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows874()
        }
    }

    object Korean : net.sneakysims.sneakylib.sims.TheSimsLanguage(20) {
        override fun encodeStringForIFF(input: String): ByteArray {
            return input.encodeToByteArrayUsingWindows949()
        }

        override fun decodeStringFromIFF(input: ByteArray): String {
            return input.decodeToStringUsingWindows949()
        }
    }

    companion object {
        // TODO: Add tests to check if these are correct
        // TODO: This is borked
        val ALL = listOf(
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Unknown,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.EnglishUS,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.EnglishUK,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.French,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.German,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Italian,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Spanish,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Dutch,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Danish,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Swedish,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Norwegian,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Finnish,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Hebrew,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Russian,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Portuguese,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Japanese,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Polish,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.SimplifiedChinese,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.TraditionalChinese,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Thai,
            net.sneakysims.sneakylib.sims.TheSimsLanguage.Korean
        )

        private val BY_ID = net.sneakysims.sneakylib.sims.TheSimsLanguage.Companion.ALL.associateBy { println(it); it.code }

        fun getLanguageById(id: Byte) = net.sneakysims.sneakylib.sims.TheSimsLanguage.Companion.BY_ID[id] ?: error("Language with ID $id does not exist!")
    }
}