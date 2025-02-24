package net.sneakysims.sneakylib.far

/**
 * Represents a file inside a FAR3 file
 */
class FAR3File(
    /**
     * The name of the file name inside the archive, keep in mind that this does include the folder (example: `Skins\CSkeletonFA_Maid.bmp`)
     */
    val fileName: String,

    val typeId: UInt,

    // typeId + fileId should be unique through the game according to Niotso
    val fileId: UInt,

    /**
     * The contents of the file
     */
    val content: ByteArray
)