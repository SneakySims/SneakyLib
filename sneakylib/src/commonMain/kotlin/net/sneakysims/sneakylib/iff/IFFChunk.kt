package net.sneakysims.sneakylib.iff

class IFFChunk(
    val code: String,
    val id: Short,
    val flags: Short,
    val name: ByteArray,
    val data: net.sneakysims.sneakylib.iff.IFFChunkData
) {
    companion object {
        const val PALT_CHUNK_CODE = "PALT"
        const val STR_CHUNK_CODE = "STR#"
        const val SPR_CHUNK_CODE = "SPR#"
        const val SPR2_CHUNK_CODE = "SPR2"
    }
}