package net.sneakysims.sneakylib.iff

class IFFChunk(
    val code: String,
    val id: Short,
    val flags: Short,
    val name: ByteArray,
    var data: ByteArray
) {
    companion object {
        const val PALT_CHUNK_CODE = "PALT"
        const val STR_CHUNK_CODE = "STR#"
        const val SPR_CHUNK_CODE = "SPR#"
        const val SPR2_CHUNK_CODE = "SPR2"
    }

    fun decodeDataAsPALT(): PALTChunkData {
        return PALTChunkData.read(data)
    }

    fun decodeDataAsSPR(): SPRChunkData {
        return SPRChunkData.read(data)
    }

    fun decodeDataAsSPR2(): SPR2ChunkData {
        return SPR2ChunkData.read(data)
    }

    fun decodeDataAsSTR(): STRChunkData {
        return STRChunkData.read(data)
    }

    fun setChunkData(chunkData: IFFChunkData) {
        data = chunkData.write()
    }
}