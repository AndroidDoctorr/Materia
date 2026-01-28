## Tin nugget

<img src="../../../shared/src/main/resources/assets/materia/textures/item/tin_nugget.png" alt="Tin nugget" width="64" height="64">

Tin nuggets are the common “kiln output” form of tin. They’re used directly for alloying bronze, or can be consolidated into tin ingots.

## Getting tin nuggets

- Kiln recipe:
  - `shared/src/main/resources/data/materia/recipes/raw_tin_to_tin_ingot.json`
  - `materia:raw_tin` → 9× `materia:tin_nugget`

## Tin ingots (stone anvil)

You can consolidate nuggets into an ingot on a **stone anvil**:

- `shared/src/main/resources/data/materia/recipes/stone_anvil/tin_ingot_from_nuggets.json`
  - 9× `materia:tin_nugget` + `#materia:stone_hammers` → `materia:tin_ingot`

## Bronze alloying

Tin nuggets are used as the “tin side” of bronze alloying in an **advanced kiln** (requires a chimney upgrade):

- `shared/src/main/resources/data/materia/recipes/bronze_ingot.json`
- `shared/src/main/resources/data/materia/recipes/bronze_alloy.json`
- `shared/src/main/resources/data/materia/recipes/bronze_alloy_with_ingot.json`

See: [Kilns](../../mechanics/kilns.md)

