# SneakyLib

**🚧 UNDER CONSTRUCTION 🚧**

This is VERY early, hacky and unfinished!

If you want to take a peek at some code examples, check out the `sneakylib-samples/scratchpad` folder

If you want to take a peek at real tools that were made using this lib, check out the [Wallpaper Maker](https://sneakysims.net/tools/wallpaper-maker) and the [FAR to ZIP converter](https://sneakysims.net/tools/far-to-zip)! (Don't worry, I will open source these tools later, and maybe even create some quick 'n' dirty CLI versions of them :3)

Another good example is the `:floormaker-common` and `:floormaker-cli` modules.

## Supported Formats

### FAR
| Type                       | Read | Write |
|----------------------------|------|-------|
| `FAR 1a (The Sims 1)`      | ✅    | ✅     |
| `FAR 1b (The Sims Online)` | ✅    | ✅     |
| `FAR 3 (The Sims Online)`  | ❌    | ❌     |

### IFF
| Type   | Read    | Write   | Notes |
|--------|---------|---------|-------|
| `PALT` | ✅       | ✅       |
| `STR#` | Partial | Partial | Only supports the `FD FF` format 
| `SPR#` | Partial | Partial | The decoder does not support big endian sprites. The encoder is not optimized yet, does not support transparent pixels mixed with non-transparent pixels in the same row.
| `SPR2` | Partial | Partial | The encoder is still a bit wonky

Unsupported IFF chunks are parsed as `UnknownChunk` and are written to the IFF file as-is.

### Important Things to Note

* While having byte for byte match when loading and writing would be nice, this is not possible due to some chunks having garbage or stupid things that would be annoying to support.
* `STR#` chunks actually don't care if not all languages are present, as long as you follow the correct format (example: for walls, it must be the Name -> Price -> Description)
* `STR#` chunks have garbage at the end of the chunk, it doesn't matter if you have them or not, the game doesn't care.
* Are `rsmp` maps actually used by the game? Even if we don't update it, the game still handles the IFF file correctly
* To make things easier for you, the types are mutable
* You can't use more than 256 colors on your `PALT`, it just corrupts the colors when rendering the sprite and, depending on the `PALT` size, the game may crash!
* `floors.iff` contains sprites that doesn't seem to be in `SPR#` format that suspiciously the chunk names are `.tga` files, why is that?
* The lib original name was "SimsLib", but I've decided to change to "SneakyLib" to avoid confusion with FreeSO's "SimsLib" and SimTech's "SimLib"
* The road lines floor tiles are special: They rotate WITH the camera, and this seems to be hardcoded in the game, the sprites in the world swap automatically depending on your rotation.
    * I think that they hardcoded on the engine itself, because these are the only floor types that have this behavior, even tho there are other floor types in the game (even on expansion packs!) that would be better if they had proper rotations.
* The "Catalog" BMP files in the `floors.iff` seems to be unused, you can delete them from the game and the game still renders the catalog sprites correctly.
* The sound of the floors is controlled by the chunk name of the first (far distance) floor, example:
    * Soft Sound: `SR11GFFB00`
    * Medium Sound: `MR11GFFB00`
    * Hard Sound: `HR11GFFB00`
    * These values were checked by generating multiple floors in Maxis' HomeCrafter and comparing the differences between the floors.
    * It is unknown what the other values mean.
* The game automatically replaces missing wall/floor strings with default text from `Build.iff`'s `PluginWall` and `PluginFloor`
* SimTech says that `SPR#` is used for floors, but that's not the case, all floors use `SPR2`!

### Reading SPR# Images and Converting

```kotlin
    for ((index, sprite) in spr.sprites.withIndex()) {
        val image = BufferedImage(sprite.imageData.width.toInt(), sprite.imageData.height.toInt(), BufferedImage.TYPE_INT_ARGB)

        for ((y, row) in sprite.imageData.data.withIndex()) {
            for ((x, paletteIndex) in row.withIndex()) {
                // If it is not transparent...
                if (paletteIndex != SPRChunkData.SPRImageData.TRANSPRENCY_INDEX) {
                    // Set the color!
                    val paletteColor = palt.entries[paletteIndex]
                    // println("palColor: $paletteColor")
                    image.setRGB(x, y, paletteColor.rgba)
                }
            }
        }

        ImageIO.write(image, "png", File("spr_${index}.png"))
    }
```