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
| `#materia:overworld_land_non_rocky` | Dry land surface rocks (excludes ocean/river/beach) | plains, forests, savannas, badlands, windswept hills, … |
| `#materia:overworld_non_rocky` | Cave rocks + some ores | most overworld except `#materia:rocky` mountains |
| `#materia:rocky` | High surface/cave rock rates, magnetite boost | windswept hills, badlands, stony peaks, … |

**Note:** `#materia:temperate` deliberately excludes `#minecraft:is_taiga` and `#minecraft:is_forest`, which previously pulled eucalyptus and other warm-climate plants into snowy spruce taiga.

See also:

- [Decorative plants](../content/blocks/decorative-plants.md)
- [Decorative flowers](../content/blocks/decorative-flowers.md)
- [Rock](../content/blocks/rock.md)
- [Version differences — worldgen wiring](../VERSION_DIFFERENCES.md#worldgen-wiring-how-features-reach-biomes)
- [Crops and farming (mechanics)](../mechanics/crops.md)
