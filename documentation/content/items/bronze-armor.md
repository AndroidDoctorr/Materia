## Bronze armor

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_helmet.png" alt="Bronze helmet" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_chestplate.png" alt="Bronze chestplate" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_leggings.png" alt="Bronze leggings" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/bronze_boots.png" alt="Bronze boots" width="32" height="32">

Bronze armor is Materia’s first “real” armor set.

Mechanically it behaves like normal armor, but it’s crafted via plates + leatherwork components (not “just ingots”).

Source of truth (stable behavior):

- `1.18.2/src/main/java/com/torr/materia/item/BronzeArmorItem.java`
- `1.18.2/src/main/java/com/torr/materia/item/ModArmorMaterials.java` (bronze stats + repair ingredient)

## Crafting (recipes)

Each piece has a recipe JSON in `shared`:

- Helmet: `shared/src/main/resources/data/materia/recipes/bronze_helmet.json`
- Chestplate: `shared/src/main/resources/data/materia/recipes/bronze_chestplate.json`
- Leggings: `shared/src/main/resources/data/materia/recipes/bronze_leggings.json`
- Boots: `shared/src/main/resources/data/materia/recipes/bronze_boots.json`

Notes:

- Boots are crafted “in two steps”: make `materia:bronze_boot`, then combine two boots to get `materia:bronze_boots`.
- Several recipes require a hammer via `#materia:bronze_hammers`.

## Common ingredients

Across the set you’ll see:

- `materia:bronze_plate`
- `materia:tanned_leather`
- `materia:leather_strap`
- `materia:brass_buckle`
- `#materia:all_rivets`
- `#materia:bronze_hammers`

## Repairing

Bronze armor repairs with:

- `materia:bronze_ingot`

## Related

- [Armor (overview)](armor.md)
- [Wrought iron armor (`materia:iron_*`)](wrought-iron-armor.md)
- [Chainmail (craftable)](chainmail.md)
