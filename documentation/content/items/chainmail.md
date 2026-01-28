## Chainmail (craftable)

Materia makes vanilla chainmail armor craftable by introducing **chainmail components**.

Vanilla pieces:

- `minecraft:chainmail_helmet`
- `minecraft:chainmail_chestplate`
- `minecraft:chainmail_leggings`
- `minecraft:chainmail_boots`

Component items used by recipes:

<img src="../../../shared/src/main/resources/assets/materia/textures/item/chainmail_chestpiece.png" alt="Chainmail chestpiece" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/chainmail_backpiece.png" alt="Chainmail backpiece" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/chainmail_shoulder.png" alt="Chainmail shoulder" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/chainmail_boot.png" alt="Chainmail boot" width="32" height="32">

## Recipes (source of truth)

### Components (Materia recipes)

- `shared/src/main/resources/data/materia/recipes/chainmail_chestpiece.json`
- `shared/src/main/resources/data/materia/recipes/chainmail_backpiece.json`
- `shared/src/main/resources/data/materia/recipes/chainmail_shoulder.json`
- `shared/src/main/resources/data/materia/recipes/chainmail_boot.json`

These are primarily built from:

- `materia:iron_rings`
- `materia:leather_strap`
- (some pieces) `materia:tanned_leather`

### Vanilla chainmail armor (recipe overrides)

Materia provides chainmail recipes under the vanilla namespace:

- Helmet: `shared/src/main/resources/data/minecraft/recipes/chainmail_helmet.json`
- Chestplate: `shared/src/main/resources/data/minecraft/recipes/chainmail_chestplate.json`
- Leggings: `shared/src/main/resources/data/minecraft/recipes/chainmail_leggings.json`
- Boots: `shared/src/main/resources/data/minecraft/recipes/chainmail_boots.json`

Notable behavior:

- Boots are assembled from two `materia:chainmail_boot`.
- Chestplate is assembled from chest/back/shoulders plus rings + strap.

## Ring tags

Chestplate assembly uses a tag:

- [`#materia:hard_rings`](../../reference/tags/metalworking-and-construction.md#materiahard_rings): `shared/src/main/resources/data/materia/tags/items/hard_rings.json`

In `shared`, this currently includes:

- `materia:bronze_rings`
- `materia:iron_rings`

## Related

- [Armor (overview)](armor.md)
- [Bronze armor](bronze-armor.md)
- [Wrought iron armor (`materia:iron_*`)](wrought-iron-armor.md)
