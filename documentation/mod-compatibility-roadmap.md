## Mod compatibility roadmap (worklist)

This is a practical checklist of changes that generally improve compatibility with large modpacks and popular mods (Pam’s HarvestCraft, Farmer’s Delight, Create, Immersive Engineering, Mekanism, etc.).

Scope: the “core” Materia versions in this repo.

### Guiding principles

- **Prefer tags over hardcoded item IDs** for recipe inputs and tool checks
- **Avoid global overrides** of vanilla behavior when an additive approach exists
- **Make vanilla-changing behaviors configurable**
- **Return `PASS` when you didn’t fully handle an interaction** so other mods can act
- **Keep optional integrations optional** (never hard-depend unless intended)

---

## A) Foundations (highest impact)

### A1) Tag interoperability (recipes + automation)

- [x] **Add/align common tags** so other mods accept Materia’s items.

Status:

- [x] **Forge tag bridges added (initial set)** under `shared/src/main/resources/data/forge/tags/items/`
  - [x] `forge:strings` + `forge:string`
  - [x] `forge:seeds`
  - [x] `forge:fibers` + `forge:fiber` (currently includes `materia:plant_fiber`)
  - [x] `forge:tools/knives`, `forge:tools/axes`, `forge:tools/hammers`, `forge:tools/saws`, `forge:tools/needles`
- [x] **Dye tag bridges** (`forge:dyes/*`) including Torr’s extended dye set
- [x] **Core ingredients bridged** (Forge tags added + recipes/tags updated where safe)
  - [x] `forge:flour` / `forge:flours`
  - [x] `forge:dough` / `forge:doughs`
  - [x] `forge:salt` / `forge:salts`
  - [x] `forge:milk` (items)
  - [x] `forge:oil` / `forge:oils` (items)
- [x] **Expand fibers** beyond `materia:plant_fiber` (intentionally deferred — keep fibers Torr-only for now)
- [x] **Add more common Forge “ingredient” tags** where you have equivalents (salt, flour, dough, oils, milk, etc.)

Next implementation note:

- **Prefer “invert the dependency”** so existing `materia:` tags reference `forge:` tags (not the other way around). This avoids tag cycles and means we don’t have to touch dozens of recipe JSONs to gain compatibility.

- [x] **Audit Materia’s recipes for hardcoded inputs** and switch them to tags where safe.
- [ ] **Document tag mapping rules** (what goes in which shared tags) in:
  - [x] `documentation/reference/tags/` (add a “compat tags” page)

### A2) Config toggles for behavior changes

- [x] **Server config** added to disable / soften “world-affecting” mechanics:
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

### A3) “Don’t break other tools” interaction rules

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
    - [x] `FurnaceOverrideHandler`: runs at `LOWEST`, respects prior cancellations, ignores off-hand + shift-click (so other mods’ tools get first shot)

---

## B) Recipes and progression compatibility

### B1) Recipe overrides (vanilla + other mods)

- [ ] Inventory all files under `shared/src/main/resources/data/minecraft/recipes/` (vanilla override recipes):
  - [ ] identify which ones are “progression gates”
  - [ ] add config toggles or alternate (non-override) recipes where feasible
- [ ] Ensure recipe outputs are predictable and don’t create “dupe loops” with other mods’ processing chains.

#### B1a) Current vanilla-override footprint (initial notes)

Materia currently ships **a large set of recipes in the `minecraft` namespace** under `shared/src/main/resources/data/minecraft/recipes/`. These override vanilla recipes and effectively act as progression gates (or outright replacements) in modpacks.

Quick inventory snapshot (current repo state):

- **Total override recipe JSONs**: 375
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
- **Potentially most disruptive “replacement” override**
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
  - `iron_shovel` / `iron_hoe` → similar “tool head + handle/bindings” assembly
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
  - `glass_puck_smelting.json` → `materia:glass_puck`
  - `lavender_dye_from_lilac.json` (and `magenta_dye_from_allium.json`) → `materia:lavender_dye`
  - `plain_cake.json` → `materia:plain_cake`

Why this matters for compatibility:

- These overrides can conflict with modpacks that expect vanilla recipes to exist (JEI/guidebooks/questlines), and can create unexpected loops when other mods add alternative conversions.
- The `furnace.json` replacement is especially high-risk because it changes what “furnace” means in every pack.

Proposed direction (what we’ll implement next):

- **Move vanilla overrides into an optional built-in datapack** (disabled by default) so modpacks can opt-in to Materia’s progression-gated vanilla crafting.
- For any Materia-only outputs currently living in the `minecraft` namespace (like `materia:furnace_kiln`), provide a **non-override recipe** under `data/materia/recipes/` so enabling/disabling the override pack doesn’t strand items behind missing recipes.
- Then do a second pass to flag + fix any likely “dupe loop” candidates (recipes where another mod can cheaply reverse or reprocess back into the same input).

Implementation status (started):

- [x] **Inventory + shortlist** captured above (counts + high-impact gate list).
- [x] **Carve-out for the most disruptive replacements**: moved the 15 `minecraft:*` recipes that output `materia:*` into an **optional built-in datapack** named `materia_vanilla_overrides` (added per-version pack metadata).
- [x] Added non-override Materia recipes for:
  - `materia:furnace_kiln`
  - `materia:blast_furnace_kiln`
  - `materia:plain_cake`
  so disabling the override pack does not remove access to these items.

### B2) Alternative recipe sets (optional “compat packs”)

Create optional datapacks (shipped disabled by default, or documented) that add:

- [ ] **Create recipes** (Mixing/Milling/Crushing/Pressing) for your crops/materials (tag-based)
- [ ] **Farmer’s Delight recipes** (Cutting Board / Cooking Pot) for your foods/materials (tag-based)
- [ ] **Immersive Engineering recipes** (crusher/sawmill style equivalents) where sensible
- [ ] **Mekanism recipes** (crusher/enrichment style equivalents) where sensible

Note: implement as datapack JSON where possible; only code when an API requires it.

---

## C) Loot tables, drops, and worldgen

### C1) Loot table completeness

- [ ] Audit every custom block has a loot table in `shared/src/main/resources/data/materia/loot_tables/blocks/`
  - [ ] confirm “drops itself” for standard blocks
  - [ ] confirm container blocks drop contents properly (if you have inventories)
  - [ ] confirm explosion survival conditions where appropriate

### C2) Worldgen friendliness

- [ ] If you add ores/rocks/plants via worldgen:
  - [ ] ensure it’s datapack-driven (features/placed features) where possible
  - [ ] avoid overwriting other mods’ placed features/biomes
  - [ ] make it configurable (enable/disable per feature)

---

## D) Capabilities and automation friendliness (machines/pipes)

### D1) Fluids

- [ ] If a block holds fluids (pots, amphora, etc.):
  - [ ] expose standard fluid handling capabilities
  - [ ] support fill/drain with common containers where appropriate
  - [ ] document which fluids are valid via tags

### D2) Energy (if applicable)

- [ ] If any block uses energy:
  - [ ] expose standard Forge Energy capability
  - [ ] avoid custom-only energy systems unless strictly necessary

### D3) Item IO (if applicable)

- [ ] For inventories:
  - [ ] expose item handler capability consistently
  - [ ] ensure sided rules don’t block automation unexpectedly (document any restrictions)

---

## E) Popular-mod-specific interop (nice-to-have)

### E1) Farmer’s Delight

- [ ] Add Cutting Board recipes for:
  - [ ] your meats/fish/vegetables where it makes sense
  - [ ] fibers → strings / cloth steps if appropriate (tag-based knives)
- [ ] Cooking Pot recipes for soups/stews/preserved foods if appropriate.

### E2) Create

- [ ] Add Milling/Crushing for grains/roots/ores where it makes sense (tag-based)
- [ ] Add Mixing for dough/batters/brines where it makes sense (tag-based fluids/items)

### E3) Immersive Engineering

- [ ] Add optional IE machine recipes for:
  - [ ] plant processing (fiber, oil, etc.)
  - [ ] metal processing steps that map cleanly

### E4) Mekanism

- [ ] Add optional Mek machine recipes for:
  - [ ] ore/material processing steps that map cleanly
  - [ ] avoid stepping on Mek’s balance by making these optional

### E5) Pam’s HarvestCraft

- [ ] Tag alignment so Pam’s crops/foods can be used in Torr’s recipes (and vice versa)
- [ ] Avoid hardcoding Pam item IDs; rely on tags.

---

## F) Testing checklist (per change)

- [ ] **Baseline**: game launches + JEI loads + no log spam
- [ ] **Recipe sanity**: JEI shows recipes; no missing-tag warnings
- [ ] **Automation sanity** (if applicable): pipes can insert/extract; fluids can fill/drain
- [ ] **No-interaction conflicts**: Create wrench / FD knife / common tools still work when used on Torr’s blocks

---

## Next step (pick one)

Suggested first work item: **A1 Tag interoperability** (it helps *every* mod).

