## Decorative plants (yucca, plantain, reeds, taro)

Small worldgen plants added in 1.2.0. Most are decorative; taro is also a farmable crop.

## Yucca

- **Block**: `materia:yucca` — short bush, may place on grass, dirt, coarse dirt, podzol, or sand
- **Worldgen**: `shared/src/main/resources/data/materia/forge/biome_modifier/materia_yucca.json`
- **Biomes**: `#materia:desert`, `#materia:temperate`
- **Placement**: clusters of five in a plus or X pattern (`TallPlantClusterFeature`)

## Plantain

- **Block**: `materia:plantain` — two-block-tall double plant (like tall grass)
- **Worldgen**: `shared/src/main/resources/data/materia/forge/biome_modifier/materia_plantain.json`
- **Biomes**: `#materia:tropical`
- **Placement**: same cluster feature as yucca

## Reeds

- **Block**: `materia:reeds` — one-block reed clump; requires water on a horizontal neighbor to survive
- **Worldgen**: `shared/src/main/resources/data/materia/forge/biome_modifier/materia_reeds.json`
- **Biomes**: `#materia:river`, `#materia:warm_wet_surface`
- **Tag**: included in `#materia:reeds` (`shared/src/main/resources/data/materia/tags/items/reeds.json`)

## Taro

- **Crop block**: `materia:taro_crop` — four growth stages (0–3)
- **Item / seed**: `materia:taro` — plant on farmland or grass/dirt/podzol
- **Wild spawn**: semi-mature patches (age 2–3) in `#materia:tropical` biomes
  - Biome modifier: `shared/src/main/resources/data/materia/forge/biome_modifier/materia_taro_crop.json`
  - Break wild plants for at least one taro (replant pool); mature age 3 also yields 3–4 extra
  - Loot: `shared/src/main/resources/data/materia/loot_tables/blocks/taro_crop.json`
- **Raw food**: edible but applies brief **Poison I** — cook before eating
- **Cooked**: `materia:cooked_taro` via water pot (boiling) or oven
- **Farming**: standard crop growth on farmland; wild plants on grass do not need full sunlight

## Fruit leather (related food)

- **Item**: `materia:fruit_leather` — 5 nutrition / 0.6 saturation
- **Recipe**: three items from `#materia:fruits` in a row (`shared/src/main/resources/data/materia/recipes/fruit_leather.json`)
- **Fruits tag**: sweet berries, glow berries, apple, `materia:fig`

See also:

- [Crops (blocks)](crops.md)
- [Crops and farming (mechanics)](../../mechanics/crops.md)
- [Plants and farming tags](../../reference/tags/plants-and-farming.md)
