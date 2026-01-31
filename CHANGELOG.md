# Changelog

All notable changes to **Materia** (mod id: `materia`) will be documented in this file.

## 1.0.2 (2026-01-31)

- Fixed **rock worldgen** in **1.20.1** and **1.21.1** by ensuring the rock placed/configured features are registered and by correcting the biome modifier biome tags.
- Fixed **gravel drops** (all versions): gravel now drops **either 4 pebbles** or **2 pebbles + 1 flint** (with Silk Touch still returning gravel).
- Fixed **JEI** display for the **bone knife** (the `FlintKnifeRecipe`) in **1.18.2** and **1.19.2** by providing explicit ingredients for the custom recipe and including it in JEI’s crafting recipe list.
- Added **thatch slab** (`thatch_slab`) to complete the thatch block set (thatch block + slope + slab).
- Removed redundant `clump_of_taupe_wool` naming/registration; `clump_of_wool` is the default (taupe) wool clump and tags were updated accordingly.

## 1.0.1

- Fixed **rock spawning** for the **1.20.x** and **1.21.x** ports.

## 1.0.0

- Initial release (**deprecated**).

