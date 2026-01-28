## Drying rack

The drying rack is a simple, in-world processing block used for:

- **Tanning leather** (into `materia:tanned_leather`)
- **Drying meat** (into jerky)

This page is the block overview. For the full leather pipeline, see:

- [Leatherworking (tanning + hardening)](../../mechanics/leatherworking.md)

## Crafting

- Recipe: `shared/src/main/resources/data/materia/recipes/drying_rack.json`
- Uses the tag `#materia:basic_bindings` (tag reference: [Bindings and adhesives](../../reference/tags/bindings-and-adhesives.md))

## How it works (high-level)

- The rack has two “modes”:
  - **Leather slot** (tanning): insert `minecraft:leather`
  - **Meat slot** (jerky): insert raw porkchop / beef / mutton (up to 2)
- It only dries when there is a **lit campfire adjacent** (one block away on a side).
- When drying conditions are met, it completes after **30 seconds** (600 ticks).

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/DryingRackBlock.java`
- `1.18.2/src/main/java/com/torr/materia/blockentity/DryingRackBlockEntity.java`

## Loot table

- `shared/src/main/resources/data/materia/loot_tables/blocks/drying_rack.json`

