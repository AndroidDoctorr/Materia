# Changelog

All notable changes to **Materia** (mod id: `materia`) will be documented in this file.

## 1.0.7 (2026-04-05)

- Fixed crafting sticks and smooth planks from rough cherry, mangrove, and nether-based wood planks
- Fixed orientation of upside-down thatch stairs corners
- Added missing loot tables for all custom blocks
- **Cannonball piles** now correctly drop the exact number of cannonballs stored in the pile (1–14), not a fixed 3–5 range (this was always handled in Java; the incorrect loot table that was briefly added has been removed)
- **Vanilla recipe overrides** are now bundled as built-in datapacks, registered in Forge via `AddPackFindersEvent` on all supported versions (1.18.2, 1.19.2, 1.20.1, 1.21.1):
  - All 375 `minecraft`-namespace recipe overrides live in the shared `materia_vanilla_overrides` datapack (`data/materia/datapacks/materia_vanilla_overrides/`); each port supplies a matching `pack.mcmeta` (pack format per MC version)
  - **`materia_vanilla_overrides` is required (on by default)** so progression-gated vanilla crafting applies unless you explicitly disable it (e.g. `/datapack disable "builtin/materia_vanilla_overrides"`)
  - **`materia_compat_recipes`** (Create / Farmer's Delight compat) is also registered and **optional (off by default)** — enable when those mods are present
  - Disabling either pack does not strand key Materia items: non-override recipes exist for outputs such as `materia:furnace_kiln`, `materia:blast_furnace_kiln`, and `materia:plain_cake`
- **Documentation**: `documentation/mod-compatibility-roadmap.md` updated for B1 (override pack layout + registration) and a **dupe loop audit** (B1b: no infinite loops found; a few broken or awkward recipes called out for follow-up)


## 1.0.6 (2026-03-03)

- Fixed vine crop placement/survival edge cases across versions so seed-planted **wisteria/grape/hops** crop blocks no longer remain as free-floating stage-0 plants near support structures.
- Fixed **gravel loot regression**: restored **flint** drops while keeping Materia pebble behavior, and added occasional `materia:rock` bonus drops.
- Fixed **1.18.2 startup crash** related to worldgen registry initialization by correcting configured/placed feature registry keys for that version.
- Added clearer **metalworking guidance** in docs and JEI:
  - Anvil inputs must be **hot** (including `/heatmetal` note for testing/creative).
  - Some recipes consume **multiple units** (for example, 9 ingots -> 1 block).
  - Added explicit alloy ratio guidance for **bronze** and **brass**.
- Added missing item translations in `en_us` and `nl_be` for:
  - `batter`, `bronze_boot`, `gold_boot`, `gold_backpiece`, `gold_chestpiece`, `gold_shoulder`, `unfired_amphora`.

## 1.0.5 (2026-02-17)

- Fixed multiple **dedicated server** issues in the **1.20.1** port:
  - Prevented client-only classes from being loaded on the server during cannon aiming.
  - Improved cannon interaction so aiming UI can be opened reliably via server->client packet.
- Fixed **1.21.1** menu opening crashes for several blocks (kiln/oven/etc.) by updating to the newer `openMenu(..., BlockPos)` flow.
- Fixed several **recipe parsing** problems and improved **cross-version recipe compatibility** (schema/IDs) so servers start cleanly.
- Moved disruptive **vanilla recipe overrides** into an optional built-in datapack (`materia_vanilla_overrides`) and added safer **compat recipe** helpers in an optional built-in datapack (`materia_compat_recipes`).
- Added **JEI** support for **oven** recipes (so cooking recipes like `raw_sausage -> sausage` show up).
- Added throwable **Bomb** item (grenade-like), similar to dynamite, with ignition requirement and a larger explosion than dynamite.
- Fixed an exploit: **wild vines** (wild wisteria/grapes/hops) no longer drop seeds/fiber on **right-click**; loot is obtained by **breaking** only.

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

