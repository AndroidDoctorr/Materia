## Bronze saw

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_saw.png" alt="Bronze saw" width="64" height="64">

The bronze saw is a metalworking-era woodworking tool. It appears as a requirement in the “unlock vanilla crafting table” milestone recipe.

## Crafting

- `shared/src/main/resources/data/materia/recipes/bronze_saw.json`

Ingredients:

- `materia:bronze_saw_band`
- `materia:handle`
- `#materia:adhesives`

Tag reference:

- `#materia:adhesives`: [Bindings and adhesives](../../reference/tags/bindings-and-adhesives.md#materiaadhesives)

## Getting a bronze saw band

The saw band is an anvil product:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_saw_band_from_plate.json`
  - Input: `materia:bronze_plate`
  - Tools: `#materia:bronze_hammers` + `#materia:bronze_chisels`

The bronze plate is also an anvil product:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_plate_from_ingot.json`

## Related

- [Bronze hammer](bronze-hammer.md) (common tool dependency)
- [Anvils](../../mechanics/anvils.md)
- [Progression](../../mechanics/progression.md)

