## Steel ingot

In Materia (1.18.2), the item shown in-game as a **Steel ingot** is actually the vanilla item:

- `minecraft:iron_ingot`

Likewise, the “Steel nugget” is:

- `minecraft:iron_nugget`

This is intentional: Materia uses **wrought iron** (`materia:wrought_iron_*`) as the early “iron age” metal, and treats vanilla iron as **steel**.

## How to get steel ingots (1.18.2)

Vanilla iron smelting is disabled in `shared/` (iron ore and raw iron don’t smelt into `minecraft:iron_ingot` in a normal furnace).

Instead, steel ingots are produced by a special “steel” advanced kiln recipe:

- `shared/src/main/resources/data/materia/recipes/steel_ingot.json`
  - Inputs: `minecraft:raw_iron` + `#minecraft:coals`
  - Output: `minecraft:iron_ingot` (displayed as “Steel ingot”)

Important gating (implementation detail):

- This recipe requires **coal coke as fuel** (`requires_coke_fuel: true`)
- It only runs in:
  - a **blast furnace kiln**, or
  - a **furnace kiln** with a **furnace chimney** placed above it

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/blockentity/KilnBlockEntity.java`

## What steel unlocks

Once you can make steel ingots, you can forge **steel components** (steel rod/plate/wire/tool parts) on an **iron anvil**, and craft the vanilla “iron” tool/armor set (which is labeled as steel in-game).

See:

- [Progression (Steel Age)](../../mechanics/progression.md)
- [Metalworking (overview)](../../mechanics/metalworking.md)

