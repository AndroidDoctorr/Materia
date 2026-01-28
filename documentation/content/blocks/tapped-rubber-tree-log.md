## Tapped rubber tree log

<img src="../../../shared/src/main/resources/assets/materia/textures/block/tapped_rubber_tree_log.png" alt="Tapped rubber tree log (texture)" width="128" height="128">

`materia:tapped_rubber_tree_log` is the “spent” form of a rubber log after it has been tapped for latex.

## How you get it

Right-click `materia:rubber_tree_log` with one of the tapping-capable tools:

- `materia:knapped_flint`
- `materia:flint_knife`
- `materia:iron_knife`
- `materia:steel_knife`

This converts the block (preserving log axis) and drops **1–2** `materia:latex`.

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/item/KnappedFlintItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/FlintKnifeItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/IronKnifeItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/SteelKnifeItem.java`

## Fire pit output

Tapped rubber logs can be processed in a fire pit:

- `shared/src/main/resources/data/materia/recipes/tapped_rubber_tree_log_to_charcoal.json`

This yields:

- `minecraft:charcoal`
- `materia:ash`

## Related

- [Rubber tree](rubber-tree.md)
- [Tapping (sap and latex)](../../mechanics/tapping.md)
- [Latex](../items/latex.md)

