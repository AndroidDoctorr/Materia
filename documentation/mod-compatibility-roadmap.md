## Mod compatibility roadmap (worklist)

This is a practical checklist of changes that generally improve compatibility with large modpacks and popular mods (Pam's HarvestCraft, Farmer's Delight, Create, Immersive Engineering, Mekanism, etc.).

Scope: the "core" Materia versions in this repo.

### Guiding principles

- **Prefer tags over hardcoded item IDs** for recipe inputs and tool checks
- **Avoid global overrides** of vanilla behavior when an additive approach exists
- **Make vanilla-changing behaviors configurable**
- **Return `PASS` when you didn't fully handle an interaction** so other mods can act
- **Keep optional integrations optional** (never hard-depend unless intended)

---

## A) Foundations (highest impact)

### A1) Tag interoperability (recipes + automation)

- [x] **Add/align common tags** so other mods accept Materia's items.

Status:

- [x] **Forge tag bridges added (initial set)** under `shared/src/main/resources/data/forge/tags/items/`
  - [x] `forge:strings` + `forge:string`
  - [x] `forge:seeds` (incl. optional `#pamhc2crops:seeds` and `immersiveengineering:hemp_seed`, both `required: false`)
  - [x] `forge:fibers` + `forge:fiber` (`materia:plant_fiber` + optional `immersiveengineering:hemp_fiber`, `required: false`)
  - [x] `forge:tools/knives`, `forge:tools/axes`, `forge:tools/hammers`, `forge:tools/saws`, `forge:tools/needles`
- [x] **Dye tag bridges** (`forge:dyes/*`) including Torr's extended dye set
- [x] **Core ingredients bridged** (Forge tags added + recipes/tags updated where safe)
  - [x] `forge:flour` / `forge:flours`
  - [x] `forge:dough` / `forge:doughs`
  - [x] `forge:salt` / `forge:salts`
  - [x] `forge:milk` (items)
  - [x] `forge:oil` / `forge:oils` (items)
- [x] **Expand fibers** beyond `materia:plant_fiber` (intentionally deferred — keep fibers Torr-only for now)
- [x] **Add more common Forge "ingredient" tags** where you have equivalents (salt, flour, dough, oils, milk, etc.)

Next implementation note:

- **Prefer "invert the dependency"** so existing `materia:` tags reference `forge:` tags (not the other way around). This avoids tag cycles and means we don't have to touch dozens of recipe JSONs to gain compatibility.

- [x] **Audit Materia's recipes for hardcoded inputs** and switch them to tags where safe.
- [ ] **Document tag mapping rules** (what goes in which shared tags) in:
  - [x] `documentation/reference/tags/` (add a "compat tags" page)

### A2) Config toggles for behavior changes

- [x] **Server config** added to disable / soften "world-affecting" mechanics:
  - **Cannonballs** (block transforms / breaking)
    - `cannonballs.blockEffectsEnabled` (default: `true`)
    - `cannonballs.crackVariants` (default: `true`)
    - `cannonballs.stoneToCobblestone` (default: `true`)
    - `cannonballs.cobblestoneToPebbles` (default: `true`)
    - `cannonballs.breakSand` (default: `true`)
    - `cannonballs.ironExtraStoneSmashing` (default: `true`)
  - **Vines** (grapes/wisteria/hops supports + spread tuning)
    - `vines.preventGrapeWisteriaOverlap` / `vines.preventVineOverlapOnSupports` (default: `true`)
    - `vines.selfHealSupportOverlap` (default: `true`)
    - `vines.plantSpreadChancePercent` (default: `10`)
    - `vines.supportChainSpreadChancePercent` (default: `2`)
- [x] **Client config** only for visuals/UI where needed (avoid server/client desync). (not needed currently)

### A3) "Don't break other tools" interaction rules

- [x] Audit `Block#use` / `useItemOn` / `useWithoutItem` paths:
  - [x] only consume items + return `SUCCESS` when action happened
  - [x] otherwise return `PASS`
  - [x] **Key fixes**
    - [x] `CannonBlock`: returns `PASS` when holding unrelated items; sneak-reset only with empty hand (so wrenches/tools can work)
    - [x] Wild vines (`WildGrapeVineBlock`, `WildWisteriaVineBlock`, `WildHopsVineBlock`): harvesting drops only on empty-hand click; item-use passes through
- [x] Audit event handlers:
  - [x] avoid cancelling events globally
  - [x] gate behavior narrowly by block/item/tag
  - [x] **Key fix**
    - [x] `FurnaceOverrideHandler`: runs at `LOWEST`, respects prior cancellations, ignores off-hand + shift-click (so other mods' tools get first shot)

---

## B) Recipes and progression compatibility

### B1) Recipe overrides (vanilla + other mods)

- [x] Inventory all files under `shared/src/main/resources/data/minecraft/recipes/` (vanilla override recipes):
  - [x] identify which ones are "progression gates"
  - [x] move all overrides into the optional `materia_vanilla_overrides` built-in datapack (enabled by default)
- [x] Ensure recipe outputs are predictable and don't create "dupe loops" with other mods' processing chains. (audit complete — see B1b below)

#### B1a) Current vanilla-override footprint (initial notes)

Materia currently ships **a large set of recipes in the `minecraft` namespace** under `shared/src/main/resources/data/minecraft/recipes/`. These override vanilla recipes and effectively act as progression gates (or outright replacements) in modpacks.

Quick inventory snapshot (current repo state):

- **Total override recipe JSONs**: 366 (after B1b: removed 9 redundant/broken JSONs)
- **Recipes containing `materia:` references**: 303 (strong signal these are Materia-authored overrides)
- **Result namespace breakdown**:
  - `minecraft:*` outputs: 360
  - `materia:*` outputs: 15

High-impact examples spotted so far:

- **Core materials / crafting**
  - `minecraft:oak_planks` now requires `materia:smooth_oak_planks` + `materia:all_nails`
  - `minecraft:paper` now requires `materia:fine_paper_pulp` + `materia:paper_frame`
  - `minecraft:gunpowder` now requires `materia:saltpeter` + `materia:sulfur` + `materia:carbon`
- **Automation / redstone**
  - `minecraft:hopper` now requires `materia:hard_plates`
  - `minecraft:minecart` now requires `materia:minecart_axle` + `materia:hard_plates` + `materia:all_rivets`
- **Tools / armor**
  - `minecraft:iron_pickaxe` now requires `materia:steel_pickaxe_head` + `materia:iron_handle` + `materia:strong_adhesives`
  - `minecraft:iron_sword` now requires `materia:steel_sword_blade` + `materia:steel_crossbar` + `materia:iron_handle` + `materia:advanced_bindings`
- **Nether progression**
  - `minecraft:netherite_scrap` is produced via `materia:kiln` from `minecraft:ancient_debris` with additional kiln requirements
- **Potentially most disruptive "replacement" override**
  - `data/minecraft/recipes/furnace.json` no longer crafts `minecraft:furnace`; it crafts `materia:furnace_kiln`

High-impact progression-gate shortlist (auto-inventoried from overrides):

- **Wood building blocks**: all vanilla planks are gated behind `materia:smooth_*_planks` + `materia:all_nails`
  - `oak_planks`, `spruce_planks`, `birch_planks`, `jungle_planks`, `acacia_planks`, `dark_oak_planks`, `mangrove_planks`, `cherry_planks`, `crimson_planks`, `warped_planks`
- **Storage / transport / automation**
  - `chest` → requires `materia:brass_hinge` + `materia:brass_latch` + `materia:smooth_oak_planks`
  - `minecart` → requires `materia:minecart_axle` + `materia:hard_plates` + `materia:all_rivets`
  - `hopper` → requires `materia:hard_plates`
- **Redstone**
  - `piston` → requires `materia:hard_plates` + `materia:hard_rods` + `materia:all_rivets` + `materia:smooth_planks`
  - `observer` → requires `materia:box_frame` + `materia:hard_plates` + `materia:solenoid`
  - `dispenser` → requires `materia:insulated_wires` + `materia:solenoid`
  - `dropper` → requires `materia:box_frame` + `materia:hard_plates`
  - `comparator` → requires `materia:all_plates` + `materia:insulated_wires`
  - `lever` → requires `materia:all_hammers` + `materia:all_plates` + `materia:all_rivets`
  - `rail` / `powered_rail` → use `materia:steel_rail` / `materia:gold_rail` + `materia:tar` + `materia:smooth_planks`
- **Combat / tools / armor** (example subset)
  - `iron_pickaxe` → `materia:steel_pickaxe_head` + `materia:iron_handle` + `materia:strong_adhesives`
  - `iron_sword` → `materia:steel_sword_blade` + `materia:steel_crossbar` + `materia:iron_handle` + `materia:advanced_bindings`
  - `iron_shovel` / `iron_hoe` → similar "tool head + handle/bindings" assembly
  - `iron_helmet` / `iron_chestplate` / `iron_leggings` / `iron_boots` → gated behind Materia steel/leather/buckle/rivet parts
  - `iron_door` / `iron_trapdoor` → gated behind Materia hinges/plates/rivets/tools
- **Nether progression**
  - `netherite_scrap` → produced via `materia:kiln` (not vanilla blasting/smelting)

Overrides that output `materia:*` (i.e., replacing vanilla outputs) currently include:

- **Kilns**:
  - `blast_furnace.json` → `materia:blast_furnace_kiln`
  - `furnace.json` → `materia:furnace_kiln`
- **Food cooking**:
  - `cooked_*` and `dried_kelp` recipes → `materia:ash`
- **Other**:
  - `glass_puck_smelting.json` — furnace: `minecraft:glass` → `materia:glass_puck` (B1b)
  - `lavender_dye_from_lilac.json` (and `magenta_dye_from_allium.json`) → `materia:lavender_dye`
  - `plain_cake.json` → `materia:plain_cake`

Why this matters for compatibility:

- These overrides can conflict with modpacks that expect vanilla recipes to exist (JEI/guidebooks/questlines), and can create unexpected loops when other mods add alternative conversions.
- The `furnace.json` replacement is especially high-risk because it changes what "furnace" means in every pack.

Implementation status (complete):

- [x] **Inventory + shortlist** captured above (counts + high-impact gate list).
- [x] **All vanilla override recipes moved** into the shared `materia_vanilla_overrides` built-in datapack under `shared/src/main/resources/data/materia/datapacks/materia_vanilla_overrides/data/minecraft/recipes/` (366 JSON files after B1b pruning). The old `shared/data/minecraft/recipes/` directory no longer exists.
- [x] **`AddPackFindersEvent` registered** in `materia.java` for all 4 versions (1.18.2, 1.19.2, 1.20.1, 1.21.1). The pack is **required (enabled) by default** — vanilla crafting is gated from day one. Modpack authors or players who want vanilla recipes back can run `/datapack disable "builtin/materia_vanilla_overrides"`.
  - 1.18.2: `Pack.create` + `net.minecraftforge.resource.PathResourcePack(name, Path)`; `RepositorySource` is `(consumer, constructor) ->`
  - 1.19.2: `constructor.create` + `net.minecraftforge.resource.PathPackResources(name, Path)` with `PackMetadataSection` read first; `RepositorySource` is `(consumer, constructor) ->`
  - 1.20.1: `Pack.readMetaAndCreate` + `PathPackResources(id, Path, isBuiltIn)`; `RepositorySource` is `(consumer) ->`
  - 1.21.1: `Pack.readMetaAndCreate` + `PackLocationInfo`/`PackSelectionConfig` (both in `net.minecraft.server.packs`, not `repository`) + `PathPackResources.PathResourcesSupplier(Path)`
- [x] **`materia_compat_recipes`** also registered via the same event (was previously unregistered too). This pack is **optional (disabled by default)** since it only applies when specific other mods are present.
- [x] Added non-override Materia recipes for:
  - `materia:furnace_kiln`
  - `materia:blast_furnace_kiln`
  - `materia:plain_cake`
  so disabling the override pack does not remove access to these items.

#### B1b) Dupe loop audit (full pass)

**Summary: No true dupe loops found.** The recipe set is closed — no profit cycle exists where you can turn A → B → A and gain items. All override recipes either gate the same output behind more expensive ingredients or replace a vanilla recipe type with a Materia-machine type.

---

##### B1b recipe fixes (implemented)

| Item | Change |
|------|--------|
| `glass_puck_smelting.json` | **Fixed:** furnace smelts `minecraft:glass` → `materia:glass_puck` (recycle glass blocks into pucks for anvil recipes). Replaces the previous no-op (`glass_puck` → `glass_puck`). |
| `iron_ingot_from_smelting_raw_iron.json` / `iron_ingot_from_blasting_raw_iron.json` | **Removed:** these only existed to “disable” raw iron smelting using `minecraft:barrier` as the ingredient. Deleting them restores vanilla `raw_iron` → `iron_ingot` smelting and blasting. |
| `cooked_beef`, `cooked_chicken`, `cooked_cod`, `cooked_mutton`, `cooked_porkchop`, `cooked_rabbit`, `cooked_salmon` (override pack) | **Removed:** they were `materia:oven` recipes under `minecraft:` IDs with result `materia:ash`, which **conflicted** with `data/materia/recipes/oven_*.json` (same ingredient → cooked food). Only one `materia:oven` match is used per input; ash vs food was non-deterministic. Removing these restores vanilla **smelting** recipe IDs for those meats/fish; the Materia **oven** still uses `oven_*.json` for proper cooking. |

Legacy duplicate JSONs under `1.18.2/` and `1.19.2/` `data/minecraft/recipes/` for the same files were removed so those ports do not reintroduce the old recipes.

**Still overridden (unchanged):** `baked_potato.json` and `dried_kelp.json` remain `minecraft:smelting` → `materia:ash` in the override pack. The Materia oven still has `oven_potato.json` for baked potatoes. There is no separate oven recipe for kelp.

---

##### Cooked food override design (historical note)

Previously, seven meat/fish IDs used `materia:oven` → ash and collided with `oven_*.json`. That path is **removed** as of B1b fixes above.

---

##### Gate bypass risks (not dupe loops, but compat notes)

These are cases where another mod can produce an output without going through Materia's intended progression. They are expected and acceptable but should be documented for modpack authors.

| Override gated item | Bypass mod | Bypass mechanism | Risk level |
|---------------------|-----------|-----------------|------------|
| `minecraft:oak_planks` (and all vanilla planks) | Create | Sawmill/Cutting: logs → planks | Low — no items duplicated, just bypasses the progression gate |
| `minecraft:oak_planks` | Farmer's Delight | Cutting Board: log → planks | Low — same as above |
| Cooked foods (`cooked_beef`, etc.) | Farmer's Delight | Cooking Pot doesn't use `minecraft:cooked_*` recipe IDs | Low — FD's cooking is independent |
| `minecraft:netherite_scrap` | Mekanism | Enrichment Chamber / Crusher on ancient debris | Medium — Mekanism's processing is notably cheaper and very fast |
| `minecraft:iron_ingot` from raw iron | Any mod that adds `raw_iron → iron_ingot` conversion | Vanilla smelting/blasting again (barrier “disable” recipes removed in B1b) | Low |

---

##### No-loop confirmation for key crafting chains

**Planks chain** (most complex override):
- `oak_log + axe (hewing) → rough_oak_plank`
- `rough_oak_plank + saw → 2x smooth_oak_planks`
- `4x smooth_oak_planks + 1x nail → 1x oak_planks`
- `oak_planks` has no recipe that outputs `smooth_oak_planks`, `rough_oak_plank`, or `oak_log` in Materia's recipe set. ✅ No loop.

**Iron chain**:
- `iron_ore (smelting) → iron_ingot` — still works (override not removed)
- `raw_iron (smelting/blasting) → iron_ingot` — vanilla recipes restored (barrier placeholder recipes removed in B1b)
- `iron_ingot` has no recipe back to `iron_ore` or `raw_iron`. ✅ No loop.

**Kiln/furnace substitution**:
- `furnace.json` override: crafting now produces `materia:furnace_kiln` instead of `minecraft:furnace`
- There is a non-override recipe in `data/materia/recipes/` for `materia:furnace_kiln` that uses Materia components
- `materia:furnace_kiln` has no recipe back to `minecraft:furnace` components. ✅ No loop.

---

##### Recommended follow-up actions

- [x] **Fix broken recipes** (B1b): glass puck smelting, iron barrier recipes, meat/fish oven ash conflict — done
- [x] **Modpack author note**: `documentation/mod-compat-recipes.md` + gate-bypass table above; root `README.md` links to docs

### B2) Alternative recipe sets (optional "compat packs")

Create optional datapacks (shipped disabled by default, or documented) that add:

- [x] **Create recipes** — first batch in `materia_compat_recipes`: **Milling** for wheat → `materia:flour`, corn → `materia:cornmeal` (×2), and chance-based `plant_fiber` → `materia:taupe_string` (see `documentation/mod-compat-recipes.md`)
- [x] **Farmer's Delight recipes** — **Cutting board** for the same flour/cornmeal outputs (knife via `forge:tools/knives`) and chance-based fiber → string; Cooking Pot soups deferred
- [x] **Immersive Engineering recipes** — **Crusher** for wheat → flour and corn → cornmeal (×2); no 4:1 fiber recipe (IE ingredient JSON does not carry stack counts on a single slot the way Mekanism does)
- [x] **Mekanism recipes** — **Crusher** 4× `plant_fiber` → 1× `taupe_string`

Implementation notes:

- Almost all JSON live in **`shared/.../materia_compat_recipes/`**. **Create mixing** (dough / masa / batter) is **version-split:** `1.18.2`–`1.20.1` each carry Create 0.5–style JSON under that port’s `.../compat/create/mixing/`; **`1.21.1`** carries Create 6–style JSON only under `1.21.1/.../compat/create/mixing/` (not `shared/`, so resource merging does not duplicate paths). See `documentation/mod-compat-recipes.md` and `documentation/reference/VERSION_DIFFERENCES.md`.
- Each recipe is wrapped in **`forge:conditional`** + **`forge:mod_loaded`** for the target mod id (`create`, `farmersdelight`, `mekanism`, `immersiveengineering`).
- **Phase E (done for this batch):** Create **mixing** (`forge:flour` + water → `materia:dough`; `materia:cornmeal` + water → `materia:masa_dough`; batter parallel to shapeless), Farmer’s Delight **cutting** (`materia:squash` + knife → 2× `materia:sliced_squash`) and **cooking** (`materia:corn_cob` → `materia:popcorn`), Mek/IE **crusher** (`materia:rock` → 2× `materia:pebble`). **Still open when worth doing:** Mek enrichment / IE sawmill / more FD meals — only where they do not bypass Materia wood or other hard gates.

Note: implement as datapack JSON where possible; only code when an API requires it.

---

## C) Loot tables, drops, and worldgen

### C1) Loot table completeness

- [x] Audit every custom block has a loot table in `shared/src/main/resources/data/materia/loot_tables/blocks/`
  - [x] confirm "drops itself" for standard blocks
  - [x] confirm container blocks drop contents properly (if you have inventories)
  - [x] confirm explosion survival conditions where appropriate

Notes:
- All 212 registered blocks (excluding 2 internal non-item blocks: `cannon_barrel`, `three_sisters_corn_upper`) now have loot tables.
- `cannonball_pile` / `stone_cannonball_pile`: loot tables not needed — `CannonballPileBlock` overrides `getDrops()` in Java to drop the exact pile `COUNT` (1–14 cannonballs). These blocks handle their own drops.
- `wild_grape_vine` drops grape seeds + 50% chance of plant fiber.
- `wild_wisteria_vine` drops wisteria seeds + 50% chance of plant fiber.
- Pot variants (`water_pot`, `milk_pot`, `beer_pot`, `wine_pot`, `lava_pot`) drop themselves.

### C2) Structure chest loot

- [x] **Additive Materia loot** in vanilla structure chests via Forge **`materia:inject_loot_table`** global loot modifiers (does not replace vanilla tables).
- [x] **Structures covered:** simple dungeon, abandoned mineshaft, desert pyramid, jungle temple, stronghold (corridor/crossing/library), buried treasure, shipwreck (treasure + supply), woodland mansion, igloo.
- [x] **Docs:** [`documentation/mechanics/structure-chest-loot.md`](mechanics/structure-chest-loot.md)

Notes:
- Loot tables: `shared/src/main/resources/data/materia/loot_tables/chests/`
- Injectors: `shared/src/main/resources/data/materia/loot_modifiers/chests/`
- Pack makers can override `materia:chests/*` tables or remove entries from `global_loot_modifiers.json`.

### C3) Worldgen friendliness

- [x] **Datapack-driven:** Features and placed features are generated (1.20.1+) or shipped as JSON; biome injection uses Forge **`forge:add_features`** biome modifiers under `shared/.../data/forge/biome_modifier/` (not replacing whole biomes).
- [x] **Modded overworld biomes:** `materia:overworld` now includes `#minecraft:is_overworld` so ores/surface rules apply in Biomes O’ Plenty / Terralith / etc., not only the vanilla biome list. River vs non-river tin split uses `#minecraft:is_river` plus Forge tag `remove` on `materia:overworld_non_river` (Forge tag extension) so modded rivers match the river placement path.
- [x] **Broader biome tags:** Grassy/temperate/tropical/desert/beach/rocky/warm_wet/temperate_forest tags include Mojang biome tags (`#minecraft:is_forest`, `#minecraft:is_jungle`, etc.) and optional `forge:` / `c:` entries with `"required": false` so missing tags in older packs do not fail loading.
- [ ] **Per-feature toggles:** Not wired to config yet — use datapack overrides or disable specific biome modifiers in a pack. Granular config hooks are a later improvement (see A2).

---

## D) Capabilities and automation friendliness (machines/pipes)

### D1) Fluids

- [x] **Amphora (liquid mode)** and **water pot** expose `IFluidHandler` (Forge) for pipes (Mekanism, Create, etc.).
- [x] **Amphora:** Internal “bottle” storage maps to Forge fluids: water/lava/milk use vanilla/Forge fluids; wine, grape juice, olive oil, vinegar, beer, and beer mash use registered `FluidType` + flowing pairs (`ModFluids`) — **1.20.1 and 1.21.1** only (Forge `FluidType` API). One bottle ≈ 250 mB; nine bottles max per amphora liquid stack.
- [x] **Water pot:** Water level 0–3 maps to 0–3000 mB water (`WaterPotFluidHandler`).
- [x] **Docs:** `documentation/fluid-pipe-compat.md` (summary + version note).

### D2) Energy (if applicable)

- [ ] If any block uses energy:
  - [ ] expose standard Forge Energy capability
  - [ ] avoid custom-only energy systems unless strictly necessary

**Note:** Battery blocks exist but are not wired to FE yet — intentionally deferred until you add energy gameplay.

### D3) Item IO (if applicable)

- [x] **Baskets / sacks / amphora (solid mode)** expose `IItemHandler` via `SidedInvWrapper` / `ItemStackHandler` where applicable — logistical transporters and similar can pull/push when the block allows that face.
- [x] **Sided / mode behavior** — summarized in `documentation/fluid-pipe-compat.md` (amphora: items only in solid mode, all faces; water pot: single slot + fluid, no face filter in code). No separate per-block mechanics page unless a block gains unusual restrictions later.

---

## E) Popular-mod-specific interop (nice-to-have)

Design intent (see also `documentation/mod-compat-recipes.md`):

- **Early game:** Materia’s own gates (stone/bronze pacing, kiln/furnace fantasy, etc.) stay authoritative in the main datapack.
- **Late game:** Once players have real automation (Create, Mek, etc.), compat recipes should offer **parallel routes**—not remove Materia chains—so packs are not forced through every micro-step forever. Vanilla-override friction remains **optional** via disabling `materia_vanilla_overrides`; **compat** stays in the optional `materia_compat_recipes` datapack.
- **Additive only:** e.g. sawmills making vanilla planks from logs is an accepted bypass of the *wood* gate at automation tier; Materia can still require **smooth** planks or nails where a recipe intentionally does. Compat JSON should not delete or replace Materia-only steps unless a separate pack explicitly does.

### E1) Farmer's Delight

- [x] **Cutting board** — `materia:squash` + `forge:tools/knives` → 2× `materia:sliced_squash` (alongside existing flour/cornmeal/fiber recipes)
- [ ] More cutting board entries for other meats/fish/vegetables where it clearly matches FD patterns
- [x] **Fibers → string** — already in B2 (`forge:tools/knives`, chance-based)
- [x] **Cooking pot** — `materia:corn_cob` → `materia:popcorn` (parallels campfire timing); soups/stews/meals deferred

### E2) Create

- [x] **Milling** — B2 batch (wheat → flour, corn → cornmeal, fiber → string)
- [x] **Mixing** — `forge:flour` + 250 mB water → `materia:dough`; `materia:cornmeal` + water → `materia:masa_dough`; egg + sugar + `forge:flour` + water + `forge:milk` (items) → `materia:batter`
- [ ] Crushing/other processing for additional roots/grains only where it does not trivially skip ore or wood gates

### E3) Immersive Engineering

- [x] **Crusher** — wheat/corn (B2); **`materia:rock` → 2× `materia:pebble`** (QoL only; does not shortcut smelting or wood)
- [x] **Industrial hemp ↔ plant fiber (tag bridge, core mod)** —
  `immersiveengineering:hemp_fiber` is in `#forge:fibers` (and
  `#forge:fiber`); `immersiveengineering:hemp_seed` is in `#forge:seeds`.
  Materia's `lashing`, `bundle`, and `paper_mixture` recipes plus the
  `materia:lamp_wicks` tag consume `#forge:fibers`, so hemp is a drop-in
  equivalent for Materia's plant fiber. No `compat/immersiveengineering/`
  JSONs were added for hemp on purpose: IE already converts
  `4× hemp_fiber → minecraft:string`, and `minecraft:string` is already in
  `#forge:strings` / `#materia:strings`, so Materia recipes that ask for
  string already accept the hemp-derived string. Avoids dupe loops with
  sugar-cane → plant fiber and avoids a competing
  `hemp_fiber → materia:taupe_string` recipe in the optional compat
  datapack.
- [ ] Optional sawmill / other IE routes only after verifying JSON across supported MC versions and confirming no wood-gate bypass beyond what packs already accept

### E4) Mekanism

- [x] **Crusher** — 4× `plant_fiber` → string (B2); **`materia:rock` → 2× `materia:pebble`**
- [ ] Enrichment / other routes only where they do not undermine Materia balance (keep behind this optional datapack)

### E5) Pam's HarvestCraft

- [x] **`forge:seeds`** — optional `#pamhc2crops:seeds` entry (`required: false`) so Pam packs can align without hardcoded item IDs
- [ ] Broader crop/food tag bridges if Pam’s tags stabilize across versions; prefer tags over item IDs

---

## F) Testing checklist (per change)

Release and regression checks are maintained in **`documentation/testing-1.1.md`** (baseline, datapacks, compat mods, fluids, regressions). Run that before shipping a **1.1.x** build.

Quick smoke items (same themes, shorter):

- [ ] **Baseline**: game launches + JEI loads + no log spam
- [ ] **Recipe sanity**: JEI shows recipes; no missing-tag warnings
- [ ] **Automation sanity** (if applicable): pipes can insert/extract; fluids can fill/drain
- [ ] **No-interaction conflicts**: Create wrench / FD knife / common tools still work when used on Torr's blocks

---

## Suggested next work items

- **B2 / E** — Further compat (FD meals, Create crushing, Mek enrichment, IE sawmill) only where additive and gate-safe (see **E**).
- **C2 follow-up** — Optional per-feature config or `ICondition` on biome modifiers when you want toggles without datapack surgery.
- **D1 follow-up** — Fluid tags for cross-mod filtering; optional `FluidType` backport for **1.19.2** / **1.18.2** amphora wine/oil if those versions need full parity.
- **D2** — Forge Energy on battery blocks when gameplay is ready.
