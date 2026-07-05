## Worldgen biome tags

Materia groups biomes for plant and tree worldgen. Tags live under `shared/src/main/resources/data/materia/tags/worldgen/biome/`.

| Tag | Intended climate / region | Example vanilla biomes |
|---|---|---|
| `#materia:temperate` | Mild temperate (no boreal snow forest) | plains, meadow, birch/ oak forest, cherry grove, swamp |
| `#materia:temperate_boreal` | Cool conifer forest | taiga, old-growth pine/spruce taiga, grove |
| `#materia:temperate_forest` | Deciduous / mixed forest (no taiga tag bleed) | forest, flower forest, birch forest, dark forest |
| `#materia:subtropical` | Warm temperate + dry grassland | temperate tag + savanna, sparse jungle |
| `#materia:prairie` | Great Plains–style grassland | plains, sunflower plains, meadow, savanna |
| `#materia:tropical` | Wet/dry tropics | jungle, bamboo jungle, savanna |
| `#materia:desert` | Arid | desert, badlands |
| `#materia:river` | Freshwater margins | river, frozen river |
| `#materia:warm_wet_surface` | Humid lowland tropics/subtropics | jungle, mangrove swamp, savanna |
| `#materia:beach` | Coast | beach, stony shore |
| `#materia:grassy` | Broad farmland/grass starter crops | plains, forests, savannas, jungles |

**Note:** `#materia:temperate` deliberately excludes `#minecraft:is_taiga` and `#minecraft:is_forest`, which previously pulled eucalyptus and other warm-climate plants into snowy spruce taiga.

See also:

- [Decorative plants](../content/blocks/decorative-plants.md)
- [Decorative flowers](../content/blocks/decorative-flowers.md)
- [Crops and farming (mechanics)](../mechanics/crops.md)
