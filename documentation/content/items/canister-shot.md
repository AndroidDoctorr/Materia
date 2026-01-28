## Canister shot

<img src="../../../shared/src/main/resources/assets/materia/textures/item/canister_shot.png" alt="Canister shot" width="64" height="64">

Canister shot is cannon ammunition that detonates (after a short fuse or on impact) and sprays shrapnel.

## Crafting

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/canister_shot.json)

Ingredients used:

- [`#materia:soft_plates`](../../reference/tags/metalworking-and-construction.md#materiasoft_plates)
- [`#materia:shrapnel`](../../reference/tags/combat-and-explosives.md#materiashrapnel)
- `materia:dynamite`

## Using (ammo)

- Loaded into the [Cannon](../blocks/cannon.md)
- Accepted via [`#materia:cannon_ammo`](../../reference/tags/combat-and-explosives.md#materiacannon_ammo)

## Behavior (in the cannon)

- Detonates **on impact** or after a short fuse
- Creates a small explosion effect with **no block damage**
- Spawns shrapnel projectiles
