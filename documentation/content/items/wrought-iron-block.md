## Wrought iron block

<img src="../../../shared/src/main/resources/assets/materia/textures/block/wrought_iron_block.png" alt="Wrought iron block (texture)" width="128" height="128">

Wrought iron blocks are used as the main “iron anvil material” milestone.

## Crafting (bronze anvil)

Wrought iron blocks are made on a bronze anvil:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_block_from_ingots.json`
  - 9× `materia:wrought_iron_ingot` → 1× `materia:wrought_iron_block`
  - Requires `#materia:bronze_hammers`

## Iron anvil

The iron anvil is made from wrought iron blocks:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_anvil_from_blocks.json`
  - 3× `materia:wrought_iron_block` → 1× `materia:iron_anvil`
  - Requires `#materia:iron_hammers`

See:

- [Iron hammer](iron-hammer.md)
- [Progression](../../mechanics/progression.md)

