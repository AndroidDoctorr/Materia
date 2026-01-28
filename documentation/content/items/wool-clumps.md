## Wool clumps

<img src="../../../shared/src/main/resources/assets/materia/textures/item/clump_of_white_wool.png" alt="Clump of white wool" width="64" height="64">

In Materia, sheep produce **wool clumps** instead of directly producing wool blocks.

This makes early textiles more “processed”:

- clumps → string (with a spindle)
- clumps → wool blocks (bulk craft)

Source of truth (stable behavior):

- Sheep drop replacement (kills / loot): `1.18.2/src/main/java/com/torr/materia/loot/SheepWoolModifier.java`
- Custom shearing to clumps (tools affect yield): `1.18.2/src/main/java/com/torr/materia/events/SheepShearingHandler.java`

## Obtaining wool clumps

### From sheep drops

When a sheep would drop wool blocks, that wool is replaced by clumps:

- 1–3 clumps, matching vanilla “wool amount” expectations
- supports vanilla colors plus your custom sheep colors (capability-driven)

### From shearing (tool-dependent yield)

Shearing is overridden to drop clumps instead of wool blocks.

Yield scales by tool tier:

- Flint knife: ~1–2 clumps
- Bronze knife: ~2–3 clumps
- Bronze shears: ~3–4 clumps
- Iron shears: ~4–5 clumps
- Vanilla shears: ~5–6 clumps

## Dyeing clumps

Colored clumps can be crafted by combining a dye with three “any-color” clumps.

Example (white):

- `shared/src/main/resources/data/materia/recipes/clump_of_wool_white.json`

This yields **3 clumps** of the dyed color.

## Processing wool clumps

### Clumps → wool blocks

Wool blocks are crafted from clumps in a 3×3:

- 9 clumps → 1 wool block

Example (white wool):

- `shared/src/main/resources/data/materia/recipes/white_wool_from_clumps.json`

### Clumps → string (with a spindle)

Clumps can be spun into string using a spindle (the spindle is not consumed):

- 1 clump + 1 spindle → 2 string

Example (white string):

- `shared/src/main/resources/data/materia/recipes/white_string.json`

## Tags

- `#materia:clumps_of_wool`: `shared/src/main/resources/data/materia/tags/items/clumps_of_wool.json`

## Related

- [Spindle](spindle.md)
- [Textiles (overview)](../../mechanics/textiles.md)

