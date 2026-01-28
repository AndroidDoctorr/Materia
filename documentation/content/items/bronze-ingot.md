## Bronze ingot

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_ingot.png" alt="Bronze ingot" width="64" height="64">

Bronze ingots are the first major alloy milestone. Many bronze-age tools and components start from bronze ingots.

## Getting bronze ingots

Bronze is produced in an **advanced kiln** recipe, which requires a **chimney** upgrade (two-input kiln).

Recipes (source of truth):

- `shared/src/main/resources/data/materia/recipes/bronze_alloy.json`
  - `materia:tin_nugget` + `minecraft:raw_copper` → `materia:bronze_ingot` (requires chimney)
- `shared/src/main/resources/data/materia/recipes/bronze_ingot.json`
  - `minecraft:raw_copper` + `materia:tin_nugget` → `materia:bronze_ingot` (requires chimney)
- `shared/src/main/resources/data/materia/recipes/bronze_alloy_with_ingot.json`
  - `materia:tin_nugget` + `minecraft:copper_ingot` → `materia:bronze_ingot` (requires chimney)

## Bronze nuggets (stone anvil)

If you have bronze nuggets, you can consolidate them:

- `shared/src/main/resources/data/materia/recipes/stone_anvil/bronze_ingot_from_nuggets.json`
  - 9× `materia:bronze_nugget` + `#materia:stone_hammers` → 1× `materia:bronze_ingot`

## Related

- [Kilns](../../mechanics/kilns.md)
- [Progression](../../mechanics/progression.md)

