## Copper nugget

<img src="../../../shared/src/main/resources/assets/materia/textures/item/copper_nugget.png" alt="Copper nugget" width="64" height="64">

Copper nuggets are an early metal output used for “small copper” crafting and for consolidating into copper ingots.

## Getting copper nuggets

From raw copper in a kiln:

- `shared/src/main/resources/data/materia/recipes/raw_copper_to_copper_ingot.json`
  - `minecraft:raw_copper` → 9× `materia:copper_nugget`

Raw copper sources:

- [Malachite](../blocks/malachite.md) drops `minecraft:raw_copper`

## Copper ingots (stone anvil)

- `shared/src/main/resources/data/materia/recipes/stone_anvil/copper_ingot_from_nuggets.json`
  - 9× `materia:copper_nugget` + `#materia:stone_hammers` → `minecraft:copper_ingot`

## Related

- [Kilns](../../mechanics/kilns.md)
- [Anvils](../../mechanics/anvils.md) (stone anvil is an early consolidation tool)

