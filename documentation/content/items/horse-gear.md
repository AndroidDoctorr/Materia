## Saddle and horse armor (craftable)

Materia makes saddle and horse armor craftable using the leatherworking pipeline (tanned leather, hardened leather, straps, rivets, etc.).

Related mechanics:

- [Leatherworking (tanning + hardening)](../../mechanics/leatherworking.md)
- [Animal drops](../../mechanics/animal-drops.md) (animal fat is part of leather finishing)

## Saddle

Materia provides the saddle recipe under the vanilla namespace:

- `shared/src/main/resources/data/minecraft/recipes/saddle.json`

Ingredients:

- `materia:hardened_leather`
- `materia:tanned_leather`
- `materia:leather_strap`
- `minecraft:stick`

## Leather horse armor

This is also an override under the vanilla namespace:

- `shared/src/main/resources/data/minecraft/recipes/leather_horse_armor.json`

Notable ingredients:

- `minecraft:saddle`
- `materia:tanned_leather`
- `materia:hardened_leather`
- `materia:leather_strap`
- `#materia:all_rivets` (tag JSON: `shared/src/main/resources/data/materia/tags/items/all_rivets.json`)

## Gold horse armor

Gold horse armor is craftable (vanilla namespace override):

- `shared/src/main/resources/data/minecraft/recipes/gold_horse_armor.json`

This upgrades leather horse armor using:

- `materia:gold_plate`
- `#materia:all_rivets`

## Iron horse armor

Iron horse armor is craftable (Materia namespace recipe that outputs the vanilla item):

- `shared/src/main/resources/data/materia/recipes/iron_horse_armor.json`

This upgrades leather horse armor using:

- `materia:iron_plate`
- `#materia:all_rivets`

## Diamond horse armor

Diamond horse armor is craftable (Materia namespace recipe that outputs the vanilla item):

- `shared/src/main/resources/data/materia/recipes/diamond_horse_armor.json`

This upgrades iron horse armor using:

- `minecraft:diamond`
- `#materia:strong_adhesives` (tag JSON: `shared/src/main/resources/data/materia/tags/items/strong_adhesives.json`)

## Related

- [Tanned leather](tanned-leather.md)
- [Hardened leather](hardened-leather.md)
- [Leather strap](leather-strap.md)
- Tag reference: [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md) (`#materia:all_rivets`)

