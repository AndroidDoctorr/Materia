## Crops and farming

Materia adds several new crops and a “Three Sisters” polyculture system.

This page covers:

- how to find and start crops (wild plants → seeds)
- how the mod’s right-click harvesting works
- which crops are single-block vs tall crops
- how the Three Sisters system works

Stable-version source of truth:

- Crop blocks: `1.18.2/src/main/java/com/torr/materia/*CropBlock.java`
- Three Sisters: `1.18.2/src/main/java/com/torr/materia/ThreeSistersBlock.java`
- Right-click harvest system: `1.18.2/src/main/java/com/torr/materia/events/CropRightClickHarvestHandler.java`

Block pages:

- [Crops (blocks)](../content/blocks/crops.md)
- [Three Sisters crop](../content/blocks/three-sisters-crop.md)

## Getting started: seeds

Most of the new crops have a **wild plant** variant that you can find in the world.

Wild plants drop:

- **at least 1 seed** (so you can start farming)
- sometimes extra seeds and/or a small produce drop (depends on the plant)

The “seed family” tag (useful in recipes):

- [`#materia:seeds`](../reference/tags/plants-and-farming.md#materiaseeds): `shared/src/main/resources/data/materia/tags/items/seeds.json`

It includes vanilla wheat/beetroot seeds plus:

- `materia:flax_seeds`
- `materia:squash_seeds`
- `materia:pepper_seeds`
- `materia:beans`
- `materia:corn`

## Planting rules (the short version)

- Crops grow on **farmland** (vanilla rule).
- Most crops are standard 0–7 age crops.
- Corn is a **tall crop** after a certain age (it has an upper block).
- Corn/beans/squash can be planted into the **Three Sisters** crop block (one tile can host up to three crops).

## Right-click harvesting (QoL)

The mod adds “right-click harvest with replant” on the server:

- For normal crops (`CropBlock`), right-clicking a **fully-grown** crop:
  - drops its loot
  - replants it at **age 1** (so the plant stays in place)
- For the Three Sisters crop:
  - each sub-crop (corn/beans/squash) is harvested **independently**
  - each harvested sub-crop is reset to **age 1**
  - if you click the tall corn’s upper block, the handler redirects the harvest to the base block below

## Worldgen (wild plants)

Wild crops are added to “grassy” biomes via a biome modifier:

- `shared/src/main/resources/data/materia/forge/biome_modifier/materia_wild_crops.json`

Indigo is a separate “tropical” placement:

- `shared/src/main/resources/data/materia/forge/biome_modifier/materia_indigo.json`

## Food hooks (why these crops matter)

Even before you “do a whole food system write-up”, these crops immediately feed into a few basic chains:

- **Corn → cornmeal → masa dough**
  - `shared/src/main/resources/data/materia/recipes/cornmeal.json`
  - `shared/src/main/resources/data/materia/recipes/masa_dough.json`
- **Corn cob → popcorn (campfire cooking)**
  - `shared/src/main/resources/data/materia/recipes/corn_to_popcorn_campfire.json`
- **Beans + peppers + tortilla → burrito**
  - `shared/src/main/resources/data/materia/recipes/burrito.json`

