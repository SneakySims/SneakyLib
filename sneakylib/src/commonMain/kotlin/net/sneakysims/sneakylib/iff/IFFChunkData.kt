package net.sneakysims.sneakylib.iff

sealed class IFFChunkData {
    abstract fun write(): ByteArray
}