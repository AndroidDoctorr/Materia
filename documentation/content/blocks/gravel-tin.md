## Tin gravel

<img src="../../../shared/src/main/resources/assets/materia/textures/block/gravel_tin.png" alt="Tin gravel (texture)" width="128" height="128">

Tin gravel is a natural “alluvial” ore deposit. It’s one of the main early sources of **tin**.

## Block ID

- `materia:gravel_tin`

## Where it generates

**1.18.2:** `1.18.2/src/main/java/com/torr/materia/world/ModWorldEvents.java` (`BiomeLoadingEvent` — river vs non-river split).

**1.19.2+:** shared biome modifiers:

- `shared/src/main/resources/data/materia/forge/biome_modifier/materia_tin_gravel_non_river.json`
- `shared/src/main/resources/data/materia/forge/biome_modifier/materia_tin_gravel_river.json`

Placed features: `{port}/.../ModPlacedFeatures.java` (`gravel_tin_ore_placed`, `gravel_tin_ore_river_placed`).

Practical tip:

- Look in **riverbeds, beaches, and shallow water** areas first.

## Drops

Tin gravel drops:

- `materia:raw_tin`
- `materia:pebble` (extra drops)

Loot table:

- `shared/src/main/resources/data/materia/loot_tables/blocks/gravel_tin.json`

## Related

- Item: [Raw tin](../items/raw-tin.md)
- Mechanics: [Kilns](../../mechanics/kilns.md) (processing)
- Progression: [Progression](../../mechanics/progression.md)

