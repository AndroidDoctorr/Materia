# Release roadmap

Planned work for upcoming Materia releases after **1.2.0**. This is a living checklist — move items to `CHANGELOG.md` when they ship.

For mod-pack compatibility work (tags, optional datapacks, pipes, etc.), see [`mod-compatibility-roadmap.md`](mod-compatibility-roadmap.md).

---

## 1.2.1 — bug fixes & copper roofs

Patch release focused on regressions from 1.2.0 and one small building feature.

### Cannons (again)

- [x] **E/W vs N/S barrel rotation:** Aim yaw sign in the barrel BER was wrong on E/W (clockwise vs counterclockwise). N/S uses `-yaw`, E/W uses `+yaw`; static mesh offset (+180 on 1.18/1.19) unchanged.

### Rainbow eucalyptus

- [x] **Sapling drops:** Rainbow eucalyptus logs/leaves should drop **`rainbow_eucalyptus_sapling`** (or equivalent) like normal eucalyptus; currently missing or broken.

### Copper roofs

- [x] **Copper roof tiles:** Right-click a placed **`roof_frame`** with a **copper plate** to sheet the slope (anvil hammer sound), or craft **`roof_frame` + `copper_plate`** → **`roof_copper`** item.
- [x] **Oxidation stages:** Copper roofs weather through four stages (vanilla copper block textures on the slope; custom corner triangle art per stage) via random tick, matching vanilla copper pacing.
- [x] **Sounds:** copper placement uses **`SoundEvents.ANVIL_USE`**.

### Shingle roofs

- [x] **Shingles:** **`shingle.json`** — 2× **`materia:tar`** (or **`shingle_from_pitch.json`** with 2× pitch) + any **`#materia:smooth_planks`** → **4× shingle**.
- [x] **Shingle roof block:** shapeless **4× shingle + roof frame** → **`shingle_roof`** item; or tile a frame one shingle at a time (four stages). Placement uses **`block.wood.scrape`** (`wood_scrape.ogg`).

---

## 1.2.2 — wood doors, glazed terracotta, decor, cork

Minor content release: finish wood sets, expand dye-colored building blocks, decorative building parts, and a cork oak line.

### Wood doors & trapdoors

- [ ] **Doors and trapdoors** for Materia wood types skipped in 1.2.0 (fig, cedar, eucalyptus/rainbow eucalyptus, palm, cypress, baobab, maple, etc. — scope TBD per wood already in the mod).

### Glazed terracotta

- [ ] **Glazed terracotta** for Materia dye colors not covered by vanilla (burgundy, tan, teal, verdigris, extended Torr palette where applicable).

### Decorative building

- [ ] **Marble**-themed blocks/items (exact set TBD).
- [ ] **Finials** and similar roof/detail pieces.
- [ ] **Flower boxes** (planters / window boxes).

### Cork oak & cork

- [ ] **Cork oak** tree (worldgen + logs/leaves/sapling).
- [ ] **Cork** material from bark or dedicated processing.
- [ ] **Uses:** wine-style **bottles**, **item frames**, and other cork-appropriate substitutes where it fits progression.
- [ ] **Cork board** — pin/note board block using existing **red string** (wall-mounted notes / map-like UX TBD).

---

## 1.2.2 or 1.2.3 — exploration & whimsy

Lower priority or larger-scope ideas; may slip to a later patch.

### Bark

- [ ] **Other uses for bark** (beyond current hewing/tapping — crafting, fuel, mulch, insulation, etc.).

### Willow

- [ ] **Willow trees** (biome placement, droopy leaves, wood set pieces as scope allows).

### Early mining / fire

- [ ] **Fire-setting** or similar early mining technique (historical progression hook — design pass needed).

### Airiots (air chariots)

- [ ] **Air chariots:** boat hull + **phantom membranes** + some propulsion item/block; flying boat-style vehicle. Large feature — needs movement, controls, and balance pass before committing to a version number.

---

## How to use this doc

1. When starting a release branch, copy the relevant section into a **`testing-x.y.z.md`** checklist if needed.
2. When an item ships, check it off here and add a bullet under **`CHANGELOG.md`** for that version.
3. If scope grows, split **1.2.2** vs **1.2.3** explicitly in this file rather than letting “maybe” items block a release.
