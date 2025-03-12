package net.sneakysims.sneakylib.cmx

import net.perfectdreams.harmony.math.Quaternionf
import net.perfectdreams.harmony.math.Vector3f

data class CMX(
    val version: String,
    val skeletons: List<Skeleton>,
    val suits: List<Suit>,
    val skills: List<Skill>
) {
    companion object {
        fun read(content: String): CMX {
            val lines = content.lines().iterator()
            val comment = lines.next()
            val version = lines.next()
            val skeletonCount = lines.next().toInt()
            val skeletons = mutableListOf<Skeleton>()

            repeat(skeletonCount) {
                val type = lines.next()
                val suitCount = lines.next().toInt()

                val skeletonBones = mutableListOf<SkeletonBone>()

                repeat(suitCount) {
                    println("Parsing...")
                    val boneName = lines.next()
                    println(boneName)
                    val parentBone = lines.next()
                    val propCount = lines.next().toInt()
                    repeat(propCount) {
                        val arrayElements = lines.next().toInt()
                        repeat(arrayElements) {
                            val name = lines.next()
                            val type = lines.next()
                        }
                    }
                    val xyz = lines.next().removePrefix("| ").removeSuffix(" |").trim().split(" ").map { it.toFloat() }
                    val rotationQuaternion =
                        lines.next().removePrefix("| ").removeSuffix(" |").trim().split(" ").map { it.toFloat() }
                    val canTranslate = lines.next().toInt() == 1
                    val canRotate = lines.next().toInt() == 1
                    val canBend = lines.next().toInt() == 1
                    val wiggleValue = lines.next()
                    val wigglePower = lines.next()

                    println(boneName)
                    println(parentBone)

                    skeletonBones.add(
                        SkeletonBone(
                            boneName,
                            parentBone,
                            Vector3f(xyz[0], xyz[1], xyz[2]),
                            Quaternionf(
                                rotationQuaternion[0],
                                rotationQuaternion[1],
                                rotationQuaternion[2],
                                rotationQuaternion[3]
                            )
                        )
                    )
                }

                skeletons.add(Skeleton(skeletonBones))
            }

            val suits = mutableListOf<Suit>()

            val suitsCount = lines.next().toInt()
            repeat(suitsCount) {
                val suitName = lines.next()
                println("Suit Name: $suitName")
                val suitType = lines.next().toInt()
                // The suit type almost always has the value zero.
                // The exceptions are the two censor files (adult-censor and child-censor) where it has the value one.
                // At a guess, it is a flag that sets off the blur effect.
                // (The blur effect is more complex than this simple supposition, but this flag does seem to be related somehow.)
                // (Don Hopkins says zero is a "normal" suit and one is a censorship bounding box.
                // There may be other types as well, such as clipping optimizations.)
                // -SimTech
                println("Suit Type: $suitType")
                val unknown = lines.next().toInt() // SimTech thinks that this is Props
                println("Unknown: $unknown")
                val skinCount = lines.next().toInt()
                println("Skin Count: $skinCount")

                val skins = mutableListOf<Skin>()

                repeat(skinCount) {
                    val boneName = lines.next()
                    println("Bone Name: $boneName")
                    val skinName = lines.next()
                    println("Skin Name: $skinName")
                    val censorFlagBits = lines.next().toInt()
                    println("Censor Flag Bits: $censorFlagBits")
                    val unknown = lines.next().toInt() // SimTech thinks that this is Props
                    println("Unknown: $unknown")

                    skins.add(Skin(boneName, skinName, censorFlagBits, unknown))
                }

                suits.add(Suit(suitName, suitType, unknown, skins))
            }

            // TODO: Skills
            val skillsCount = lines.next().toInt()
            if (skillsCount != 0)
                error("Skills are not supported yet! Sorry!!")

            return CMX(version, skeletons, suits, emptyList())
        }
    }

    data class Skeleton(
        val skeletonBones: List<SkeletonBone>
    )

    data class SkeletonBone(
        val boneName: String,
        val parentBone: String,
        val position: Vector3f,
        val rotation: Quaternionf
    )

    data class Suit(
        val suitName: String,
        val suitType: Int,
        val unknown: Int,
        val skins: List<Skin>
    )

    data class Skin(
        val boneName: String,
        val skinName: String,
        val censorFlagBits: Int,
        val unknown: Int
    )

    data class Skill(
        val skillName: String,
        val animationName: String,
        val duration: Float,
        val distance: Float,
        val movingFlag: Int,
        val positionCount: UInt,
        val rotationCount: UInt,
        val motions: List<Motion>
    )

    data class Motion(
        val boneName: String,
        val frameCount: Int,
        val duration: Float,
        val positionsUsedFlag: Boolean,
        val rotationsUsedFlag: Boolean,
        val positionOffset: Int,
        val rotationOffset: Int
    )
}