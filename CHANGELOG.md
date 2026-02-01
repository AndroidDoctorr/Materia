# Changelog

All notable changes to **Materia** (mod id: `materia`) will be documented in this file.

## 1.0.4 (2026-02-01)

- Added initial **cheese + gut processing** content (first-phase):
  - New items: `animal_gut`, `clean_gut`, `rennet`, `cheese_curds`, `soft_cheese`, `cord`
  - New placeable blocks: `fresh_cheese_wheel`, `aged_cheese_wheel`
  - New recipes:
    - `animal_gut` + vinegar -> `clean_gut`
    - `animal_gut` + cutting tool -> `rennet`
    - milk + vinegar -> `soft_cheese`
    - milk + rennet -> `cheese_curds`
    - 4x `cheese_curds` -> `fresh_cheese_wheel`
  - Updated **drying rack**: `clean_gut` can be dried near a lit campfire into `cord`
  - Cheese wheels:
    - Wheels are **slab-height** (cake-like)
    - **Fresh** wheels are **not edible** and **age in-world** into **aged** wheels (moving the block does **not** reset aging)
    - **Aged** wheels can be sliced into 8 servings (and can also be crafted into wedges with a cutting tool)
- Added **animal gut** as occasional animal loot (killed by player):
  - Cows / pigs / sheep have a **25%** chance to drop `animal_gut`
- Added **sausage**:
  - New items: `raw_sausage`, `sausage`
  - New recipes:
    - `clean_gut` + porkchop + salt + cutting tool -> `raw_sausage`
    - Cook `raw_sausage` -> `sausage` (oven or campfire)
- Improved **cannon** feel:
  - Increased max range for fully-charged shots
  - Made partial charges more meaningfully different
  - Added slight aim randomization (iron cannonballs a bit tighter; canister/TNT a bit wider)

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

