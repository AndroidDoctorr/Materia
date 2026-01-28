## Spindle

<img src="../../../shared/src/main/resources/assets/materia/textures/item/spindle.png" alt="Spindle" width="64" height="64">

The spindle is a small crafting tool used to turn **wool clumps into string**.

Important behavior:

- It is treated as a **tool ingredient** in recipes (it is **not consumed**).

Source of truth (stable behavior):

- `1.18.2/src/main/java/com/torr/materia/item/SpindleItem.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/spindle.json`

Ingredients:

- `minecraft:stick`
- `#materia:smooth_planks`
- `#materia:adhesives`

## Using the spindle (string crafting)

The basic pattern is:

- 1× `materia:clump_of_*_wool`
- 1× `materia:spindle`
→ 2× `materia:*_string`

Example recipe (white):

- `shared/src/main/resources/data/materia/recipes/white_string.json`

## Related

- [Wool clumps](wool-clumps.md)
- [Textiles (overview)](../../mechanics/textiles.md)
- Tag reference:
  - [Textiles and storage materials](../../reference/tags/textiles-and-storage.md)

