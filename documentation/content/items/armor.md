## Armor

Materia changes armor progression by:

- adding a full **bronze armor** set
- adding a “wrought iron” set (registered as `materia:iron_*`)
- making some “rare” vanilla armors **craftable** (notably chainmail)
- pushing vanilla-style armor crafting toward **components** (plates, rivets, straps, buckles)

Stable-version source of truth:

- `1.18.2/src/main/java/com/torr/materia/item/ModArmorMaterials.java`

Pages:

- [Bronze armor](bronze-armor.md)
- [Wrought iron armor (`materia:iron_*`)](wrought-iron-armor.md)
- [Chainmail (craftable)](chainmail.md)

## Repair materials (1.18.2)

From `ModArmorMaterials`:

- **Bronze** repairs with `materia:bronze_ingot`
- **Wrought iron** repairs with `materia:wrought_iron_ingot`

## Common crafting ingredients

Many armor recipes use:

- plates (example: `materia:bronze_plate`, `materia:iron_plate`)
- `materia:leather_strap`
- `materia:tanned_leather`
- `materia:brass_buckle`
- `#materia:all_rivets`
- a hammer tag (commonly `#materia:bronze_hammers` or `#materia:iron_hammers`)

Tag JSONs (source of truth):

- `shared/src/main/resources/data/materia/tags/items/all_rivets.json`
- `shared/src/main/resources/data/materia/tags/items/bronze_hammers.json`
- `shared/src/main/resources/data/materia/tags/items/iron_hammers.json`

## Vanilla recipe overrides to know about

Some vanilla armor pieces are overridden in `shared/src/main/resources/data/minecraft/recipes/`.

Example:

- `shared/src/main/resources/data/minecraft/recipes/iron_helmet.json`

That recipe crafts `minecraft:iron_helmet`, but uses Materia components (plates, rivets, straps, etc.) rather than vanilla ingots.

