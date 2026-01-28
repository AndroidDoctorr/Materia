## Bow (recipe override)

This page documents Materia’s **custom crafting recipe** for the vanilla `minecraft:bow`.

The bow’s mechanics are still vanilla — only the recipe is changed.

## Recipe (source of truth)

Materia overrides the vanilla recipe here:

- `shared/src/main/resources/data/minecraft/recipes/bow.json`

Key ingredients used by the override:

- `materia:bow_limb` (crafted component)
  - [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/bow_limb.json)
- `materia:handle`
  - [Handle](handle.md)
- `#materia:all_bowstrings` (see [tag reference](../../reference/tags/textiles-and-storage.md#materiaall_bowstrings))
- `#materia:adhesives` (see [tag reference](../../reference/tags/bindings-and-adhesives.md#materiaadhesives))

## Related

- [Composite bow](composite-bow.md)
- [Arrows (flint + metal)](metal-arrows.md)
- Crossbow recipe override (vanilla mechanics unchanged): `shared/src/main/resources/data/minecraft/recipes/crossbow.json`
- [Crossbow (recipe override)](crossbow.md)
