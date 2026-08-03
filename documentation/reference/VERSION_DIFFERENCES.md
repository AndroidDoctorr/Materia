## Version differences (parity tracker)

This page is a living “parity tracker” for differences between Minecraft versions (and this repo’s version folders).

Goal:

- keep gameplay docs mostly **version-agnostic**
- record **where behavior or available content differs**, so we don’t lose track

See also: [Minecraft version support & branches](VERSIONS.md)

## High-signal differences to track

### Wood set expansions

Newer Minecraft versions add wood families (examples: **mangrove**, **cherry**). Torr’s Mod often has wood-component variants for these (posts/tables/joists/trellises/etc.) and may override vanilla recipes accordingly.

Checklist:

- [ ] Wood-component variants exist for each “new” vanilla wood family in the newer branch(es)
- [ ] Any vanilla recipe overrides are documented (what’s overridden, and why)

Current snapshot (what’s where in this repo today):

- **`shared/` wood components**
  - posts/tables/trellises: oak/spruce/birch/jungle/acacia/dark oak + rubber wood + cherry
  - joists: the above + mangrove + crimson + warped
- **`1.19.2/` adds extra wood-component recipes**
  - mangrove: posts/tables/trellises (and smooth planks)
  - crimson/warped: posts/tables/trellises (and smooth planks)

This is normal “parity drift” for a multi-version repo, but it’s worth tracking so `shared/` stays coherent over time.

### “Earth” equivalence (packed mud)

Torr’s Mod uses an “earth” concept/tag in multiple recipes.

- `#materia:earth_blocks` includes `minecraft:packed_mud` as an optional entry:
  - `shared/src/main/resources/data/materia/tags/items/earth_blocks.json`

This is a compatibility hook: packed mud exists in newer vanilla versions, but older versions shouldn’t crash on missing items.

### Copper churn / vanilla changes

Minecraft’s copper content changes over time (blocks, crafting patterns, oxidation, etc.). If Torr’s Mod adds copper-related tooling/materials or relies on specific vanilla copper items/blocks, expect version-specific follow-up work.

Checklist:

- [ ] Identify “copper touch points” in recipes/tags that are version-sensitive
- [ ] Record any intentional divergence per version folder (when you start patching it)

### Worldgen wiring (how features reach biomes)

Materia uses **three patterns** across Forge ports — keep them in sync when tuning spawn rates or adding features:

| Port | Feature registration | Biome injection |
|---|---|---|
| **1.18.2** | Java `ModConfiguredFeatures` / `ModPlacedFeatures` | **`BiomeLoadingEvent`** in `ModWorldEvents.java` (tag checks mirror shared modifier rules). Shared `data/materia/forge/biome_modifier/*` is **not** shipped in the 1.18 jar. |
| **1.19.2** | Java `ModConfiguredFeatures` / `ModPlacedFeatures` | Shared **`forge:add_features`** JSON under `shared/.../data/materia/forge/biome_modifier/` |
| **1.20.1 / 1.21.1** | Java bootstrap + **generated JSON** in `{port}/src/generated/resources/data/materia/worldgen/` | Same shared biome modifier JSON **plus** generated placed/configured features (JSON wins over optional Java registration). After bootstrap edits on 1.20.1, run **`./gradlew runData`** and copy/sync to 1.21.1. |

**Loose ground blocks** (surface/cave **rocks**, **bauxite** patches): custom `materia:loose_ground_block` feature scans a column for the highest air cell with sturdy, non-aquatic ground below.

See also: [Rock](../content/blocks/rock.md), [Worldgen biome tags](tags/worldgen-biomes.md).

## Per-version notes (start simple)

Keep this section short. Add only “worth knowing” deltas that change progression or crafting outcomes.

### 1.18.2

- Baseline reference CSVs live in `1.18.2/src/reference/` (legacy; consider moving to `documentation/reference/` later)
- **Worldgen:** injected only via **`ModWorldEvents.java`** (`BiomeLoadingEvent` + `#materia:*` tags). Biome modifier JSON is excluded from the jar — do not enable both paths or features double-register.
- **Water pot:** does **not** expose Forge **`IFluidHandler`** — only item capabilities on this port. Pipes cannot push/pull water as a fluid; use buckets, bottles, cups, etc. (see [Fluid and pipe compatibility](../fluid-pipe-compat.md)).
- **Optional compat (`materia_compat_recipes`):** Create **mixing** recipes for dough / masa / batter use Create 0.5–style JSON and live under **`1.18.2/.../compat/create/mixing/`** (not `shared/`), same as 1.19.2 / 1.20.1.

### 1.19.2

- Adds newer vanilla wood families vs 1.18.2 (ex: mangrove)
- **Worldgen:** Java `ModPlacedFeatures` + shared biome modifier JSON (no generated `worldgen/` folder).
- **Water pot:** exposes **`IFluidHandler`** (water tank maps to block water level). See [Fluid and pipe compatibility](../fluid-pipe-compat.md).
- **Create mixing (compat pack):** JSON under **`1.19.2/.../compat/create/mixing/`** (Create 0.5–style).

### 1.20.1

- Adds more vanilla content; keep an eye on recipe overrides and new blocks/items
- **Worldgen:** **`src/generated/resources/data/materia/worldgen/`** overrides optional Java registration; run **`runData`** after bootstrap changes.
- **Water pot / amphora liquids:** full Forge fluid stack behavior as documented in [Fluid and pipe compatibility](../fluid-pipe-compat.md).
- **Create mixing (compat pack):** JSON under **`1.20.1/.../compat/create/mixing/`** (Create 0.5–style).

### 1.21.1 (in progress)

- Branch is actively evolving; expect drift until stabilized
- **Worldgen:** same generated-JSON model as 1.20.1 (sync from 1.20.1 `runData` when bootstrap changes).
- **Stone urn & planter:** registered as plain decorative blocks on 1.21.1 (no `UrnBlock` / `PlanterBlock` planting GUI yet). Interactive planting remains on **1.18.2**, **1.19.2**, and **1.20.1** only.
- **Create mixing (compat pack):** uses **Create 6**–style JSON (`fluid_stack`, `id` results) and lives only under **`1.21.1/.../compat/create/mixing/`** — not in `shared/`, to avoid clashing with older Create recipe codecs pulled in from `shared/`.

## Where to write version notes

Prefer one of these patterns:

- **Small footnote** on the relevant page (“Version notes” section)
- **Here**, when it’s a repo-wide parity concern (like wood-family coverage, copper churn)
