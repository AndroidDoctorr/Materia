## Bronze drawplate

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_drawplate.png" alt="Bronze drawplate" width="64" height="64">

A drawplate is a metalworking tool used to turn **rods into wire** on an anvil. It’s one of the key “Bronze Age starter kit” tools for making fasteners and logic materials.

## How to get one

Made on a bronze anvil:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_drawplate_from_plate.json`

Inputs and tools (high level):

- Input: `materia:bronze_plate`
- Tools: `#materia:bronze_tongs` + `#materia:bronze_bores`

## What it’s used for

- Bronze wire (bronze anvil):
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_wire_from_rod.json`

## Tags and tiering

Recipes typically require `#materia:bronze_drawplates`, which includes iron drawplates as well:

- Tag JSON: `shared/src/main/resources/data/materia/tags/items/bronze_drawplates.json`

## Related

- [Bronze chisel](bronze-chisel.md)
- [Tongs](tongs.md)
- [Metalworking (mechanics)](../../mechanics/metalworking.md)

