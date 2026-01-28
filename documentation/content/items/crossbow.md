## Crossbow (recipe override)

This page documents Materia’s **custom crafting recipe** for the vanilla `minecraft:crossbow`.

The crossbow’s mechanics are still vanilla — only the recipe is changed.

## Recipe (source of truth)

Materia overrides the vanilla recipe here:

- `shared/src/main/resources/data/minecraft/recipes/crossbow.json`

Key ingredients used by the override:

- `materia:bow_limb` (crafted component)
  - [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/bow_limb.json)
- `materia:handle` ([Handle](handle.md))
- `materia:brass_latch`
- `#materia:strong_bowstrings` (see [tag reference](../../reference/tags/textiles-and-storage.md#materiastrong_bowstrings))
- `#materia:strong_adhesives` (see [tag reference](../../reference/tags/bindings-and-adhesives.md#materiastrong_adhesives))
- `#materia:smooth_planks`

## What ammo does it use?

Crossbows use vanilla projectile rules (arrows, spectral/tipped arrows, etc.).

For Materia arrows:

- [Arrows (flint + metal)](metal-arrows.md)
