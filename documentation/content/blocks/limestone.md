## Limestone

<img src="../../../shared/src/main/resources/assets/materia/textures/block/limestone.png" alt="Limestone" width="64" height="64">

Limestone is a stone material used for building and for chemistry-adjacent chains (calcite powder).

## Where it generates

Limestone generates in the overworld as large “veins” (ore-feature style replacement of stone/deepslate), and also has a surface-biome placement in rocky biomes.

If you want the exact generator knobs for 1.21.1, see:

- `1.21.1/src/generated/resources/data/materia/worldgen/configured_feature/limestone_vein.json`
- `1.21.1/src/generated/resources/data/materia/worldgen/placed_feature/limestone_surface_biome_placed.json`
- `shared/src/main/resources/data/materia/forge/biome_modifier/materia_limestone_surface_biome_rocky.json`

## Drops / harvesting

Limestone has tool-sensitive drops:

- **Pickaxes**: drop limestone
- **Hammers** (`#materia:all_hammers`): drop **2× `materia:calcite_powder`**

Source of truth:

- `shared/src/main/resources/data/materia/loot_tables/blocks/limestone.json`

