# Optional mod-compat recipes (`materia_compat_recipes`)

Materia ships a **built-in datapack** `materia_compat_recipes` (registered in `materia.java` via Forge’s `AddPackFindersEvent`). It is **off by default** — enable it per world in **Datapacks** or with `/datapack enable "builtin/materia_compat_recipes"`.

Recipes use **`forge:conditional`** + **`forge:mod_loaded`** so they only apply when the target mod is present. There is **no compile-time dependency** on Create, Farmer’s Delight, Mekanism, or Immersive Engineering.

## Core datapack: metal smelt interop (shared kiln / furnace)

Kiln, advanced kiln, vanilla smelting, and blasting routes for **tin, copper, zinc, wrought-iron bloom work, raw gold**, and **aluminum** consume **`#forge:raw_materials/…`** (and **`#forge:nuggets/tin`**, **`#forge:ingots/copper`**, **`#forge:ingots/zinc`** for alloy inputs) defined under **`shared/.../data/forge/tags/items/`**. Optional mod items stay on those Forge tags with **`required: false`**. Materia-centric outputs (`materia:tin_*`, `materia:wrought_*`, `materia:aluminum_*`, etc.) are unchanged.

Legacy **`#materia:kiln_compatible_raw_tins`** (`shared/.../tags/items/kiln_compatible_raw_tins.json`) aliases **`#forge:raw_materials/tin`** so older references keep resolving.

## Philosophy (early vs late game)

- **Early game:** Materia’s main datapack still defines pacing (stone/bronze feel, kiln substitution, etc.). Compat does not replace that.
- **Late game:** After players can run Create, Mek, or similar, compat recipes are **additive parallel routes**—so automation can handle dough, milling, and similar steps without invalidating Materia’s own chains. Anything that would trivialize **wood** or **ore** gates is avoided here or left to pack authors.
- **Vanilla overrides:** Recipe conflicts with other mods are usually addressed by disabling the optional **`materia_vanilla_overrides`** datapack; **this** datapack stays separate so packs can enable mod interop without re-enabling every override.

## Layout (source tree)

Most JSON live under:

`shared/src/main/resources/data/materia/datapacks/materia_compat_recipes/data/materia/recipes/compat/`

| Subfolder | Mod |
|-----------|-----|
| `create/milling/` | Create millstone |
| `create/mixing/` | Create mechanical mixer — **not in `shared/`**; each Minecraft port has `1.*.x/.../compat/create/mixing/` (see [VERSION_DIFFERENCES](reference/VERSION_DIFFERENCES.md)) |
| `farmersdelight/cutting/` | Farmer’s Delight cutting board |
| `farmersdelight/cooking/` | Farmer’s Delight cooking pot |
| `mekanism/crushing/` | Mekanism crusher |
| `immersiveengineering/crusher/` | Immersive Engineering crusher |

Each port still supplies `pack.mcmeta` next to its versioned resources; Gradle merges `shared` plus that port’s `src/main/resources` into the jar.

## Current recipes (1.1.0+)

**Flour / cornmeal (parallel paths)**

- **Create** — `minecraft:wheat` → `materia:flour`; `materia:corn` → `materia:cornmeal` (×2)
- **Farmer’s Delight** — same outputs on the **cutting board** with `forge:tools/knives`
- **Immersive Engineering** — same outputs in the **IE crusher** (single-item inputs; energy 800)

**Plant fiber → string**

- **Create** — mill `materia:plant_fiber`; ~34% chance of one `materia:taupe_string` per process (≈3 fiber per string on average)
- **Farmer’s Delight** — cutting board + knife; same ~34% chance on `taupe_string`
- **Mekanism** — crusher: **4×** `plant_fiber` → **1×** `taupe_string` (deterministic bulk processing)

**Dough / masa / batter (Create mixing)**

- **`forge:flour` + 250 mB water** → `materia:dough` (parallels shapeless crafting + bottles)
- **`materia:cornmeal` + 250 mB water** → `materia:masa_dough`
- **Egg + sugar + `forge:flour` + 250 mB water + `forge:milk` (items)** → `materia:batter` (milk uses the Forge item tag—bucket, Materia cup/bottle, etc.)

**Create 0.5 vs Create 6:** **1.18.2–1.20.1** ship Create **mixing** JSON under each **`1.*.x/.../compat/create/mixing/`** tree (Create 0.5 style: `fluid` + `nbt` + `amount`, `item` in results). **1.21.1** ships the same three files only under **`1.21.1/.../compat/create/mixing/`** with Create 6’s shape (`fluid_stack`, `id` outputs). Those mixing files are **not** in `shared/` so Gradle does not merge two copies at the same path. If a future Create build renames fluid ingredients again, edit the version folder that matches your Minecraft port.

**Farmer’s Delight cutting (produce)**

- **`materia:squash` + knife** → **2×** `materia:sliced_squash` (manual recipe yields 1×; board is slightly more efficient)

**Farmer’s Delight cooking**

- **`materia:corn_cob`** → **`materia:popcorn`** (same cook time as Materia’s campfire recipe)

**Rock → pebbles (Mek / IE crushers)**

- **`materia:rock` → 2× `materia:pebble`** in Mekanism crusher and IE crusher (QoL; does not affect smelting or wood progression)

IE’s crusher only accepts a **single** `Ingredient` without stack counts on a single slot the way Mekanism does, so a 4:1 fiber recipe is not added there; wheat/corn use simple one-item inputs instead.

## Balance & modpack notes

- These recipes **add** options; they do not remove Materia’s own mortar, oven, or anvil chains.
- Multiple mods may offer overlapping routes (e.g. wheat → flour in Create, FD, and IE). That is intentional so pack authors can gate or disable packs by mod.
- For **1.21.x**, the Gradle build mirrors `recipes/` → `recipe/` for data generation; compat JSON follows the same pipeline as the rest of the mod.

## Pam’s HarvestCraft (tags)

- **`forge:seeds`** includes an optional **`#pamhc2crops:seeds`** entry (`required: false`) so Pam packs can fold Pam seeds into the same tag space without hardcoding Pam item IDs.

## Industrial hemp (Immersive Engineering)

Hemp is handled entirely by **tag bridges** in the **core mod**, not by this
optional compat datapack. There are no `compat/immersiveengineering/...` JSONs
for hemp — the integration is purely tag-driven so it is on by default,
matches IE's own progression, and does not duplicate IE recipes.

- **`forge:fibers`** + **`forge:fiber`** include
  `immersiveengineering:hemp_fiber` (`required: false`).
- **`forge:seeds`** includes `immersiveengineering:hemp_seed`
  (`required: false`).
- Materia's core fiber recipes (`lashing`, `bundle`, `paper_mixture`) and the
  `materia:lamp_wicks` tag consume `#forge:fibers`, so hemp fiber is a
  drop-in substitute for Materia's plant fiber everywhere it is used.

What we **do not** add (and why):

- **Hemp → string** in any crusher / mill / cutting board: IE already ships a
  `4× hemp_fiber → 1× minecraft:string` shaped recipe, and `minecraft:string`
  is already in `#forge:strings` (which `#materia:strings` extends). Adding a
  Materia path would either duplicate IE's recipe or pull hemp toward
  `materia:taupe_string` and create a JEI conflict. Players already have a
  clean route: hemp → string (IE) → use string in Materia like any other
  string.
- **Hemp → plant fiber** (or vice versa): a direct conversion would create
  an obvious dupe loop with `sugar_cane → 9× plant_fiber` and IE's hemp
  growth, so it is intentionally absent.

## Future (roadmap)

More FD meals, Create crushing for specific crops, Mek enrichment, IE sawmill—only where additive and progression-safe — see `documentation/mod-compatibility-roadmap.md` sections **B2** and **E**.
