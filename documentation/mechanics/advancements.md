# Advancements (Materia tab)

Materia adds a full **Advancements** chapter (in-game it is still named “Advancements”). It is a **set of optional milestones** that roughly follow the mod’s tech tree: they help you notice what exists next, but they do **not** replace recipe unlocks or hard progression gates.

For the walkthrough style guide, see [Progression (high-level guide)](progression.md).

## How the tab works

- **Hidden root** [`materia:root`](../../shared/src/main/resources/data/materia/advancements/root.json): grants on first world tick (invisible toast). It only exists so the game has a parent for the tree and a **tab background**.
- **Tab wallpaper** is set on that root’s `display.background`, currently [`materia:textures/gui/advancements/bg.png`](../../shared/src/main/resources/assets/materia/textures/gui/advancements/bg.png) (tiling texture under `assets/materia/textures/...`).
- **First visible beat** is usually **The Secret** (hammer stone): once you pick that up, the branch structure starts to make sense.

## Milestones (player-facing)

Each row is the in-game **title** (advancement id in parentheses).

Most triggers are **`minecraft:inventory_changed`** (“have this in inventory at least once”). Exceptions: **Cooked Carbon** adds **`minecraft:placed_block`** for `materia:coke_oven` (**coke item OR placed oven** satisfies the advancement); **Roundshot Ringer** uses **`minecraft:player_killed_entity`** with a **`killing_blow`** predicate on `materia:cannonball_projectile`.

| Title | Id | What counts |
|--------|-----|-------------|
| The Secret | `materia:the_secret` | Have `materia:hammer_stone` |
| Next: Circuses | `materia:next_circuses` | Have `minecraft:bread` |
| Pointed Stick | `materia:pointed_stick` | Have any item in **`#materia:spears`** |
| Sticky Business | `materia:sticky_business` | Have `materia:latex` |
| The Neolithic Age | `materia:the_neolithic` | Have / craft `materia:primitive_crafting_table` |
| Ring of Truth | `materia:ring_of_truth` | Have `materia:stone_anvil` |
| Controlled Burn | `materia:controlled_burn` | Have `materia:fire_pit` |
| Trial by Fire | `materia:trial_by_fire` | Have `materia:kiln` |
| Handle With Care | `materia:handle_with_care` | Have `materia:wood_tongs` |
| Speedy Citrus | `materia:speedy_citrus` | Have `materia:quicklime` |
| The Bronze Age | `materia:the_bronze_age` | Have `materia:bronze_ingot` (no separate “copper + tin” OR in datapack) |
| Depth of Boiling | `materia:depth_of_boiling` | Have `materia:water_pot` |
| Jar Theology | `materia:jar_theology` | Have `materia:amphora` |
| Between the Laths | `materia:between_the_laths` | Have `materia:wattle_and_daub` |
| Now You Know Why It Was So Expensive | `materia:exclusive_purples` | Have any item in **`#materia:story_purple_dyewares`** (rare purple-line dyes) — see [Dyes](../chemistry/dyes.md) |
| Under the Warp | `materia:under_the_warp` | Have `materia:frame_loom` |
| Pack Rat Royalty | `materia:pack_rat_royalty` | Have any item in **`#materia:sacks_and_bindles`** |
| Lattice Logistics | `materia:lattice_logistics` | Have `materia:basket` |
| Sister Act | `materia:sister_act` | Have `materia:corn_cob`, `materia:squash`, and `materia:beans` at least once (three separate criteria; not one harvest tick) |
| Cotton Pickings | `materia:cotton_pickings` | Have `materia:cotton` (child of Sister Act) |
| Arts & Crafts | `materia:arts_and_crafts` | **Both:** have `materia:water_pot` **and** `minecraft:crafting_table` |
| The Iron Age | `materia:the_iron_age` | **Either:** `materia:wrought_iron_ingot` **or** `materia:iron_anvil` |
| Wings of Empire | `materia:wings_of_empire` | Have `materia:aquila_aurea` |
| Cooked Carbon | `materia:cooked_carbon` | **Either:** `materia:coal_coke` **or** place `materia:coke_oven` |
| Stacked Heat | `materia:stacked_heat` | **Any of:** `materia:blast_furnace_kiln`, `materia:furnace_kiln`, `materia:furnace_chimney` (datapack does not verify chimney-on-kiln formation) |
| Carbon Temper | `materia:carbon_temper` | Have `minecraft:iron_ingot` (refined iron tier); see [Iron ingot](../content/items/iron-ingot.md). |
| Beer Is Good | `materia:beer_is_good` | Have `materia:beer_bottle` |
| In Vino Veritas | `materia:in_vino_veritas` | Have `materia:wine_bottle` |
| Breach Loader | `materia:breach_loader` | Have `materia:cannon` |
| Roundshot Ringer | `materia:roundshot_ringer` | Kill where the **killing blow** is from entity type **`materia:cannonball_projectile`** (stone or iron roundshot). Other cannon payloads (e.g. TNT) do **not** count. See [Cannons](cannons.md). |
| Stored Energy | `materia:stored_energy` | Have `materia:composite_bow` |

## Text & data layout (mod authors)

- **Advancement JSON:** [`shared/src/main/resources/data/materia/advancements/`](../../shared/src/main/resources/data/materia/advancements/) — one file per advancement plus `root.json`; picked up by every supported Forge port via the shared resources merge in each version’s `build.gradle`.
- **English UI strings:** [`shared/.../assets/materia/lang/en_us.json`](../../shared/src/main/resources/assets/materia/lang/en_us.json) keys `advancements.materia.<advancement_id>.title` and `.description` (id matches the JSON filename stem).

### Item tags used only for advancements

| Tag | Used by |
|-----|---------|
| [`#materia:spears`](../../shared/src/main/resources/data/materia/tags/items/spears.json) | Pointed Stick |
| [`#materia:sacks_and_bindles`](../../shared/src/main/resources/data/materia/tags/items/sacks_and_bindles.json) | Pack Rat Royalty |
| [`#materia:story_purple_dyewares`](../../shared/src/main/resources/data/materia/tags/items/story_purple_dyewares.json) | Exclusive purples |

### Adding a new milestone

1. Copy an existing sibling JSON (same `parent` / `display` shape as a nearby node).
2. Rename the file to the new advancement id (that becomes `materia:<filename>`).
3. Add `advancements.materia.<filename>.title` / `.description` to `en_us.json`.
4. If the criterion needs “any of several items”, prefer a **`#materia:...` item tag** over duplicating giant OR lists in JSON.

## Flavor notes (optional reading)

Several titles riff on SF comedy (for example **The Secret** and the Hitchhiker’s **“bang the rocks together”** line; **Beer Is Good** echoes a common Psychostick hook). Players do not need the references to understand the goals.
