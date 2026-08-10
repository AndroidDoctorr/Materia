# Release roadmap

Planned work for upcoming Materia releases after **1.2.0**. **1.4.0** shipped the hand cart and village chest loot. Patch fixes before that: **1.3.1** (hot-metal quench lag), **1.3.2** (quench UX, loot compat, torch fat), **1.3.3–1.3.5** (worldgen/decor/recipe fixes).

For mod-pack compatibility work (tags, optional datapacks, pipes, etc.), see [`mod-compatibility-roadmap.md`](mod-compatibility-roadmap.md).

**Mosaic & alphabet blocks** are planned as a **separate optional mod** (works with or without Materia). Prototype code remains in this repo but is not shipped in Materia.

---

## 1.3.0 — architecture update, decor & fixes

Multi-version layout plus the decorative-building pass that was previously split across intermediate version numbers.

### Bug fixes & roofs

- [x] **E/W vs N/S barrel rotation:** Aim yaw sign in the barrel BER was wrong on E/W (clockwise vs counterclockwise). N/S uses `-yaw`, E/W uses `+yaw`; static mesh offset (+180 on 1.18/1.19) unchanged.
- [x] **Rainbow eucalyptus sapling drops**
- [x] **Copper roof tiles** — plate on roof frame; four oxidation stages; custom corner art
- [x] **Shingle roofs** — tar/pitch + smooth planks → shingles; shapeless 4× shingle + roof frame
- [x] **Fig, cedar, eucalyptus, and rubber wood** doors and trapdoors

### Wood doors & trapdoors

- [x] **Doors and trapdoors** for remaining Materia wood types (palm, cypress, baobab, maple, etc.).

### Glazed terracotta

- [x] **Glazed terracotta** for Materia dye colors not covered by vanilla — ten colors smelted from matching terracotta.

### Decorative building

- [x] **Shutters**, **curtains**, **awnings**, **floor rugs**
- [x] **Stone planter** and **stone urn** (planting on 1.18.2, 1.19.2, and 1.20.1)
- [x] **Balustrades**, stonecutter tile/brick variants, **columns, bases & capitals**, **cornices & brackets**, **stone & metal finials**
- [x] **Wrought iron fence, gate, door, bracket & grate**
- [x] **Decor polish:** wrought iron fence connection/orientation fixes; 1.5-block fence/gate collision with vanilla-style 1-block outline; water wash-away fix for iron fences, balustrades, and metal finials
- [x] **Marble & limestone slabs/stairs**
- [ ] **Marble**-themed blocks/items beyond current stonecutter set (exact scope TBD).
- [ ] **Flower boxes** — additional variants beyond stone planter (TBD).

### Cork oak & cork

- [ ] **Cork oak** tree (worldgen + logs/leaves/sapling).
- [ ] **Cork** material from bark or dedicated processing.
- [ ] **Uses:** wine-style **bottles**, **item frames**, and other cork-appropriate substitutes where it fits progression.
- [ ] **Cork board** — pin/note board block using existing **red string** (wall-mounted notes / map-like UX TBD).

---

## 1.4.0 — cart (shipped)

Land vehicle update — mainly the **hand cart**.

- [x] **Hand cart** — draft-pulled entity, storage, wood variants, cover/lantern, sleep, phantom shelter, dismantle vs destroy loot, surface/water sounds, draft hoof sounds.
- [x] **Village chest loot** — 16 building-specific injectors at 70% (see [Structure chest loot](mechanics/structure-chest-loot.md)).
- [ ] **Cart sound polish** — instant stop when cart halts (deferred; rolling clips may be split into segments later).
- [ ] **Villager / wandering trader trades** for Materia goods (needs Java, not JSON-only on 1.20.1 Forge).

---

## Later (1.?)

Lower priority or larger-scope ideas; version number TBD.

### Exploration & whimsy

- [ ] **Other uses for bark** (beyond current hewing/tapping — crafting, fuel, mulch, insulation, etc.).
- [ ] **Willow trees** (biome placement, droopy leaves, wood set pieces as scope allows).
- [ ] **Fire-setting** or similar early mining technique (historical progression hook — design pass needed).
- [ ] **Air chariots:** boat hull + **phantom membranes** + propulsion item/block; flying boat-style vehicle. Large feature — needs movement, controls, and balance pass.

### Signage (separate mod)

- [ ] **Mosaic block**, **mosaic stylus**, and **character blocks** — optional companion mod; see [`content/blocks/mosaic-and-alphabet-blocks.md`](content/blocks/mosaic-and-alphabet-blocks.md).

---

## How to use this doc

1. When starting a release branch, copy the relevant section into a **`testing-x.y.z.md`** checklist if needed.
2. When an item ships, check it off here and add a bullet under **`CHANGELOG.md`** for that version.
3. If scope grows, split releases explicitly in this file rather than letting “maybe” items block a ship.
