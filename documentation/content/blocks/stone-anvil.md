## Stone anvil

The stone anvil is the **first anvil tier**. It’s used for basic hot-metal shaping and for creating your first bronze-tier stations/tools.

See also: [Anvils (mechanic)](../../mechanics/anvils.md)

## Obtaining

Crafting:

- [Stone anvil recipe JSON](../../../shared/src/main/resources/data/materia/recipes/stone_anvil.json)

## How it works

- **Slots**: 1 tool slot + 1 hot input slot
- **UI**: right-click to open the anvil GUI
- **Required input**: the metal input must be **hot**, or no recipes will appear

Tip: if you keep getting burned, use [Tongs](../items/tongs.md) and read [Hot metals](../../mechanics/hot-metals.md).

## Tongs support

If you **right-click while holding tongs** (instead of opening the UI), the tongs will try to place hot metal into the input slot:

- **Right-click**: places 1 hot item
- **Shift + right-click**: places as many hot items as will fit

Only items in `#materia:heatable_metals` can be placed this way (see [tag reference](../../reference/tags/heat-and-hot-items.md#materiaheatable_metals)).

## Durability (stone anvils break)

Stone anvils have durability and will eventually break from use.

- **Max durability**: 50
- **Damage rules (roughly)**:
  - making big things (blocks/anvils) is heavier wear
  - some recipes (like making the bronze anvil) are especially hard on stone

## Cooling

Items left in the anvil will cool over time (every ~2 seconds), so don’t leave hot inputs sitting there forever.

## The “upgrade path” recipe

The stone anvil is how you produce the bronze anvil:

- [Stone anvil: 3× bronze blocks → bronze anvil](../../../shared/src/main/resources/data/materia/recipes/stone_anvil/bronze_anvil_from_blocks.json)

## Screenshots

Add screenshots here when helpful:

- `documentation/assets/screenshots/anvils/stone/`
