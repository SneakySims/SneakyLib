package net.sneakysims.sneakylib.skn

// https://web.archive.org/web/20050502112039/http://simtech.sourceforge.net/tech/file_formats_skn.htm
data class SKN(
    val sknFileName: String, // unused
    val bitmapFileName: String,
    val bones: List<String>,
    val faces: List<Face>,
    val boneBindings: List<BoneBindings>,
    val textureCoordinates: List<TextureCoordinates>,
    val vertices: List<Vertex>
) {
    companion object {
        fun read(content: String): SKN {
            val lines = content.lines().iterator()
            val sknFileName = lines.next()
            val bitmapFileName = lines.next()
            val boneCount = lines.next().toInt()
            val bones = mutableListOf<String>()

            repeat(boneCount) {
                bones.add(lines.next())
            }

            val faceCount = lines.next().toInt()
            val faces = mutableListOf<Face>()

            repeat(faceCount) {
                val (v0, v1, v2) = lines.next().split(" ").map { it.toInt() }
                faces.add(Face(v0, v1, v2))
            }

            val boneBindingsCount = lines.next().toInt()
            val boneBindings = mutableListOf<BoneBindings>()

            repeat(boneBindingsCount) {
                val (v0, v1, v2, v3, v4) = lines.next().split(" ").map { it.toInt() }
                boneBindings.add(BoneBindings(v0, v1, v2, v3, v4))
            }

            val textureCoordinatesCount = lines.next().toInt()
            val textureCoordinates = mutableListOf<TextureCoordinates>()

            repeat(textureCoordinatesCount) {
                val (u, v) = lines.next().split(" ").map { it.toFloat() }
                textureCoordinates.add(TextureCoordinates(u, v))
            }

            val blendDataCount = lines.next().toInt()

            repeat(blendDataCount) {
                lines.next()
            }

            val verticesCount = lines.next().toInt()
            val vertices = mutableListOf<Vertex>()

            repeat(verticesCount) {
                val result = lines.next().split(" ").map { it.toFloat() }

                vertices.add(
                    Vertex(
                        result[0],
                        result[1],
                        result[2],

                        result[3],
                        result[4],
                        result[5]
                    )
                )
            }

            return SKN(
                sknFileName,
                bitmapFileName,
                bones,
                faces,
                boneBindings,
                textureCoordinates,
                vertices
            )
        }
    }

    data class Face(
        val vertex0Index: Int,
        val vertex1Index: Int,
        val vertex2Index: Int
    )

    data class BoneBindings(
        val boneIndex: Int,
        val firstVert: Int,
        val vertCount: Int,
        val firstBlendedVert: Int,
        val blendedVertCount: Int
    )

    data class TextureCoordinates(
        val u: Float,
        val v: Float
    )

    data class Vertex(
        val x: Float,
        val y: Float,
        val z: Float,
        val normalX: Float,
        val normalY: Float,
        val normalZ: Float
    )
}