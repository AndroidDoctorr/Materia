## Earth (and packed mud)

Materia uses **Earth** as a “mud / raw clay soil” building material.

In newer Minecraft versions, **packed mud** is treated as interchangeable with Earth in many recipes and mechanics.

## What is Earth?

- Block: `materia:earth`
- It behaves like a dirt-like block (mining, sounds, etc.).

Worldgen note:

- In `shared/`, Earth is added to river biomes via:
  - `shared/src/main/resources/data/materia/forge/biome_modifier/materia_surface_earth_river.json`

## Earth vs packed mud (interchangeability)

Materia defines an item tag used by recipes and mechanics:

- `#materia:earth_blocks`
  - Tag JSON: `shared/src/main/resources/data/materia/tags/items/earth_blocks.json`
  - Includes:
    - `materia:earth`
    - `minecraft:packed_mud` (optional / `required: false` for older versions)

Use this tag in docs when you mean “Earth-like blocks” (Earth in 1.18.2, packed mud in newer versions).

## Clay + dirt separation (“clay pot” trick)

You can turn Earth-like blocks into clay and dirt using a **water pot** (a clay pot filled with water):

- Put **2×** `#materia:earth_blocks` into a water pot.
- It outputs:
  - 4× `minecraft:clay_ball`
  - 1× `minecraft:dirt`
- The water pot is consumed and becomes an **empty pot** block.

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/blockentity/WaterPotBlockEntity.java`

Related:

- [Water pot (mechanic)](water-pot.md)
- [Earth (block)](../content/blocks/earth.md)

