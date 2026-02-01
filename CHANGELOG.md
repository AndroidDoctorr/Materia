# Changelog

All notable changes to **Materia** (mod id: `materia`) will be documented in this file.

## 1.0.3 (2026-01-31)

- Fixed **tool harvesting** in **1.20.1** and **1.21.1** (e.g. `materia:malachite`): added missing vanilla block tags for pickaxe mining and tool tiers so blocks that require the correct tool drop properly again.
- Added missing **wood button** and **wood pressure plate** recipes, consistent with the mod’s `smooth_*_planks` + nails/tool-based progression.
- Fixed **marble harvesting**: marble now drops **calcite powder** when broken with a **hammer**, or **marble** when broken with a **pickaxe**, and it now requires **bronze-tier** tools.
- Made **obsidian** (and `materia:obsidian_slab`) mine at a reasonable speed with a **wrought iron pickaxe** or **steel hammer**.
- Fixed **1.21.1 mod-block drops**: mirrored `loot_tables/` -> `loot_table/` during the build so loot tables load on 1.21+.
- Made **vanilla pickaxe tiers** mine obsidian faster than **wrought iron** in **1.21.1** (iron/diamond/netherite now progressively faster).
- Fixed missing texture/model for **`materia:taupe_string`** and made it the default “String” (taupe), with `materia:white_string` remaining the dyed variant.

## 1.0.2 (2026-01-31)

- Fixed **rock worldgen** in **1.20.1** and **1.21.1** by ensuring the rock placed/configured features are registered and by correcting the biome modifier biome tags.
- Fixed **gravel drops** (all versions): gravel now drops **4 pebbles** (with Silk Touch still returning gravel).
- Fixed **JEI** display for the **bone knife** (the `FlintKnifeRecipe`) in **1.18.2** and **1.19.2** by providing explicit ingredients for the custom recipe and including it in JEI’s crafting recipe list.
- Added **thatch slab** (`thatch_slab`) to complete the thatch block set (thatch block + slope + slab).
- Removed redundant `clump_of_taupe_wool` naming/registration; `clump_of_wool` is the default (taupe) wool clump and tags were updated accordingly.

## 1.0.1

- Fixed **rock spawning** for the **1.20.x** and **1.21.x** ports.

## 1.0.0

- Initial release (**deprecated**).

