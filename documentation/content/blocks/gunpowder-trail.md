## Gunpowder trail

<img src="../../../shared/src/main/resources/assets/materia/textures/block/gunpowder_line.png" alt="Gunpowder trail (texture)" width="64" height="64">

Gunpowder trails are placed on the ground like redstone. They connect into lines and ignite in sequence when lit.

## Obtaining

You “craft” trails by having `minecraft:gunpowder` and placing it on the ground.

Gunpowder itself has a recipe override:

- `shared/src/main/resources/data/minecraft/recipes/gunpowder.json`

## Placing

- **Right-click** a block face with gunpowder in hand to place a trail segment.
- Trails can’t be placed into fluids, and need a solid surface below.

## Igniting

You can ignite a trail with:

- flint and steel (or the bow drill)
- adjacent fire/lava

When the trail burns:

- it propagates along the line (“dominoes”)
- endpoints can ignite what’s ahead (commonly: **TNT**)
- it throws sparks/embers and can rarely start nearby fires

## Drops

- Breaking a trail yields gunpowder (see `shared/src/main/resources/data/materia/loot_tables/blocks/gunpowder_trail.json`)
- If the trail is washed away by water, it also drops 1× gunpowder

## Related

- [Gunpowder trails (mechanics)](../../mechanics/gunpowder-trails.md)
