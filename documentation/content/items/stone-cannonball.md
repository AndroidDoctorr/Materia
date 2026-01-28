## Stone cannonball

<img src="../../../shared/src/main/resources/assets/materia/textures/item/stone_cannonball.png" alt="Stone cannonball" width="64" height="64">

Stone cannonballs are basic cannon ammunition.

They deal heavy **direct-hit** damage, but do not explode and (currently) do not damage blocks.

## Crafting

There are multiple ways to make stone cannonballs.

### Stonecutting (from rock)

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/stone_cannonball.json)

### Stonecutting (from vanilla stone, bulk)

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/stone_cannonball_from_stone.json)

This yields **4** cannonballs per `minecraft:stone`.

### Shapeless (chisel + hammer)

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/stone_cannonball_from_chisel.json)

This uses:

- `materia:rock`
- any `#materia:bronze_hammers`
- any `#materia:all_chisels`

## Using (ammo)

- Loaded into the [Cannon](../blocks/cannon.md)
- Accepted via `#materia:cannon_ammo`

## Planned / future note

You mentioned a future feature: cannonballs damaging stone/stone bricks (to cobble/cracked stone). That behavior is **not implemented yet**.
