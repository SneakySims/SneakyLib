package net.sneakysims.sneakylib.iff

object IFFChunkUtils {
    const val IFF_CHUNK_NAME_SIZE = 64

    /**
     * An empty chunk name
     */
    val EMPTY_CHUNK_NAME = ByteArray(64)

    /**
     * Creates a chunk name for an IFF chunk
     *
     * While most chunks do not use the chunk name, some kinds of content (like floors) use the chunk name
     * for things like step sounds
     *
     * @param input the input string, should be max [IFF_CHUNK_NAME_SIZE]
     * @return the chunk name
     */
    fun createChunkName(input: String): ByteArray {
        if (input.length > IFF_CHUNK_NAME_SIZE)
            error("Input length is bigger than the max allowed length! ($IFF_CHUNK_NAME_SIZE)")

        val name = ByteArray(IFF_CHUNK_NAME_SIZE)
        for ((index, ch) in input.withIndex()) {
            name[index] = ch.code.toByte()
        }

        return name
    }
}