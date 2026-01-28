## Tapping (sap and latex)

Some trees can be harvested for resources by right-clicking their logs with early cutting tools.

## Tools

In 1.18.2, “tapping” is implemented directly on specific items’ `useOn(...)` behavior.

Tapping-capable tools are:

- `materia:knapped_flint` (knapped flint)
- `materia:flint_knife` (flint knife)
- `materia:iron_knife` (iron knife)
- `materia:steel_knife` (steel knife)

Notes:

- The tool takes **1 durability** damage per tap and plays a sound.
- The log’s **axis is preserved** when it changes state (horizontal stays horizontal, etc.).

Source of truth:

- `1.18.2/src/main/java/com/torr/materia/item/KnappedFlintItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/FlintKnifeItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/IronKnifeItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/SteelKnifeItem.java`

## Spruce sap

- Right-click a spruce log → becomes a sapped spruce log
- Drops 1–2 sap

Related blocks:

- `minecraft:spruce_log` → `materia:sapped_spruce_log`

## Rubber latex

- Right-click a rubber tree log → becomes a tapped rubber tree log
- Drops 1–2 latex

Related blocks:

- `materia:rubber_tree_log` → `materia:tapped_rubber_tree_log`

See also:

- [Rubber tree](../content/blocks/rubber-tree.md)
- [Tapped rubber tree log](../content/blocks/tapped-rubber-tree-log.md)

Related:

- [Sap](../content/items/sap.md)
- [Latex](../content/items/latex.md)
- [Flint knife](../content/items/flint-knife.md)
