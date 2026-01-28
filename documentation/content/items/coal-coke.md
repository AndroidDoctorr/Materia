## Coal coke

<img src="../../../shared/src/main/resources/assets/materia/textures/item/coal_coke.png" alt="Coal coke" width="64" height="64">

Coal coke is a higher-grade fuel made from coal.

It burns longer than coal/charcoal and is required for some high-heat / high-tech kiln recipes (notably **steel**).

## How to get it

Use a [Coke oven (block)](../blocks/coke-oven.md):

- **Input**: `minecraft:coal`
- **Fuel**: `minecraft:coal`
- **Output**: `materia:coal_coke`

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/blockentity/CokeOvenBlockEntity.java`

## Fuel value and restrictions

In 1.18.2:

- Burn time: **3200 ticks** (double coal/charcoal)
- Allowed in: kiln / furnace-kiln / blast-furnace-kiln
- Not allowed in: fire pit, oven

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/utils/FuelHelper.java`

## Related

- [Steel ingot (Steel Age)](steel-ingot.md)
- [Heat and fuel overview](../../mechanics/heat.md)

