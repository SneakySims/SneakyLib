@file:JsModule("iconv-lite")
@file:JsNonModule
package net.sneakysims.sneakylib.utils

import js.typedarrays.Uint8Array

external fun encode(input: String, encoding: String): Uint8Array<*>
external fun decode(input: Uint8Array<*>, encoding: String): String