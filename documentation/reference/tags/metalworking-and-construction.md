## Metalworking and construction tags

These tags show up in “late crafting” recipes (profession blocks, machines, armor assembly, etc.). They mostly describe:

- **metal part families** (plates, rods, wires, bands)
- **sealants** (sticky materials used for waterproofing/heat sealing)
- **tables** (Torr’s Mod’s table blocks, used as work surfaces)

Source of truth: `shared/src/main/resources/data/materia/tags/items/`

Quick links:

- [Hard plates](#materiahard_plates), [Soft plates](#materiasoft_plates), [All plates](#materiaall_plates)
- [Hard rods](#materiahard_rods), [Soft rods](#materiasoft_rods)
- [Hard wires](#materiahard_wires), [Hard bands](#materiahard_bands)
- [Hot sealants](#materiahot_sealants), [Sealants](#materiasealants)
- [Tables](#materiatables)
- [Hard rings](#materiahard_rings)

## Metal parts

### `#materia:hard_plates`

Used by: many workstation recipes (smithing table, stonecutter, smoker, etc.).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hard_plates.json`
- **Includes**: bronze / iron / steel plates

### `#materia:soft_plates`

Used by: recipes that accept “any softer metal plate”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/soft_plates.json`
- **Includes**: copper / aluminum / gold / tin / brass / zinc / lead plates

### `#materia:all_plates`

Used by: recipes that accept any plate (soft or hard).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_plates.json`
- **Includes**: `#materia:hard_plates` + `#materia:soft_plates`

### `#materia:hard_rods`

Used by: some workstation recipes (ex: grindstone).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hard_rods.json`
- **Includes**: bronze / iron / steel rods

### `#materia:soft_rods`

Used by: recipes that accept “any softer metal rod”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/soft_rods.json`
- **Includes**: copper / aluminum / gold / tin / brass / zinc / lead rods

### `#materia:hard_wires`

Used by: late-game recipes that require strong wiring (ex: brewing stand).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hard_wires.json`
- **Includes**: bronze / iron / steel wires

Note:

- `materia:copper_wire` exists, but it is *not* in `#materia:hard_wires`.

### `#materia:hard_bands`

Used by: barrel and brewing stand recipes, and various “metal band” uses.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hard_bands.json`
- **Includes**: bronze / iron / steel bands

## Sealants

### `#materia:hot_sealants`

Used by: recipes that need heat-resistant sealing (ex: cauldron, smoker).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hot_sealants.json`
- **Includes**: pitch, tar, resin

### `#materia:sealants`

Used by: general waterproofing/sealing slots (ex: composter, barrel).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/sealants.json`
- **Includes**: `#materia:hot_sealants` + `minecraft:honeycomb`

## Tables / work surfaces

### `#materia:tables`

Used by: cartography table + fletching table recipes as a “work surface” requirement.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/tables.json`
- **Includes**: the mod’s various `*_table` blocks/items; some entries are optional for version compatibility.

## Fasteners / small metal parts

### `#materia:hard_rings`

Used by: chainmail and other recipes that want “metal rings”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/hard_rings.json`
- **Includes**: `materia:bronze_rings`, `materia:iron_rings`

