## Bronze chisel

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_chisel.png" alt="Bronze chisel" width="64" height="64">

The bronze chisel is a metalworking tool used in anvil recipes that cut/shape metal stock (especially plates) into more specialized parts.

## How to get one

Made on a bronze anvil:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_chisel_from_rod.json`

High-level:

- Input: `materia:bronze_rod`
- Tool requirement: `#materia:bronze_hammers`

## What it’s used for

Common pattern:

- Plate + (hammer + chisel) → shaped cutting parts

Example:

- `materia:bronze_plate` → `materia:bronze_saw_band`
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_saw_band_from_plate.json`

## Tags and tiering

Many recipes require `#materia:bronze_chisels`, which includes iron chisels too:

- Tag JSON: `shared/src/main/resources/data/materia/tags/items/bronze_chisels.json`

## Related

- [Bronze drawplate](bronze-drawplate.md)
- [Metalworking (mechanics)](../../mechanics/metalworking.md)

