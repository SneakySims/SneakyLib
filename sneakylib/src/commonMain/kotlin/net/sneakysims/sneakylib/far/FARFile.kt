package net.sneakysims.sneakylib.far

/**
 * Represents a file inside a FAR file
 */
class FARFile(
    /**
     * The name of the file name inside the archive, keep in mind that this does include the folder (example: `Skins\CSkeletonFA_Maid.bmp`)
     */
    val fileName: String,

    /**
     * The contents of the file
     */
    val content: ByteArray
)