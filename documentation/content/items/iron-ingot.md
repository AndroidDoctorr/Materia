## Iron ingot (refined iron)

Materia’s **refined iron** tier uses the vanilla item:

- `minecraft:iron_ingot`

Likewise, the vanilla nugget:

- `minecraft:iron_nugget`

This is intentional: **wrought iron** (`materia:wrought_iron_*`, `materia:iron_*` tools/components) is the early iron-age metal. **Refined iron** is vanilla `minecraft:iron_ingot`, smelted through Materia’s advanced kiln gate — not renamed “steel” in-game anymore.

## How to get iron ingots

Vanilla iron smelting is disabled in `shared/` (iron ore and raw iron don’t smelt into `minecraft:iron_ingot` in a normal furnace).

Instead, iron ingots come from an advanced kiln recipe:

- `shared/src/main/resources/data/materia/recipes/steel_ingot.json` (registry id unchanged)
  - Inputs: `minecraft:raw_iron` + `#minecraft:coals`
  - Output: `minecraft:iron_ingot`

Important gating:

- Requires **coal coke as fuel** (`requires_coke_fuel: true`)
- Runs only in a **blast furnace kiln**, or a **furnace kiln** with a **furnace chimney** above it

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/blockentity/KilnBlockEntity.java`

## What refined iron unlocks

Once you have iron ingots, forge **iron components** (`materia:steel_*` registry ids — displayed as **Iron** plate/rod/wire/parts) on the **wrought iron anvil**, then craft the vanilla iron tool and armor set from Materia override recipes.

See:

- [Progression (Iron Age)](../../mechanics/progression.md)
- [Refined iron hammer](refined-iron-hammer.md)
- [Metalworking (overview)](../../mechanics/metalworking.md)
