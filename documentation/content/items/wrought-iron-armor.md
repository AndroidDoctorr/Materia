## Wrought iron armor (`materia:iron_*`)

<img src="../../../shared/src/main/resources/assets/materia/textures/item/iron_helmet.png" alt="Wrought iron helmet" width="32" height="32">

Materia adds a wrought iron armor tier registered as:

- `materia:iron_helmet`
- `materia:iron_chestplate`
- `materia:iron_leggings`
- `materia:iron_boots`

This is distinct from vanilla `minecraft:iron_*` armor.

Source of truth (stable behavior):

- `1.18.2/src/main/java/com/torr/materia/item/IronArmorItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/ModArmorMaterials.java` (wrought iron stats + repair ingredient)

Rendering note:

- In 1.18.2, the worn armor model uses **vanilla iron armor textures** so it renders correctly without custom model textures.

## Crafting (recipes)

Recipe JSONs in `shared`:

- Helmet: `shared/src/main/resources/data/materia/recipes/iron_helmet.json`
- Chestplate: `shared/src/main/resources/data/materia/recipes/iron_chestplate.json`
- Leggings: `shared/src/main/resources/data/materia/recipes/iron_leggings.json`
- Boots: `shared/src/main/resources/data/materia/recipes/iron_boots.json`

Notes:

- Boots are crafted “in two steps”: make `materia:iron_boot`, then combine two boots to get `materia:iron_boots`.
- Several pieces accept `#materia:bronze_hammers` as the “hammer requirement” in the recipe JSON.

## Repairing

Wrought iron armor repairs with:

- `materia:wrought_iron_ingot`

## Vanilla iron armor recipe override (separate topic)

Vanilla iron armor can also be affected by recipe overrides under `data/minecraft/recipes/`.

Example:

- `shared/src/main/resources/data/minecraft/recipes/iron_helmet.json` crafts `minecraft:iron_helmet` using plates/rivets/hammers rather than ingots.

## Related

- [Armor (overview)](armor.md)
- [Bronze armor](bronze-armor.md)
- [Chainmail (craftable)](chainmail.md)
