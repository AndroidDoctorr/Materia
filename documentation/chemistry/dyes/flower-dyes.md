## Flower → dye policy

Materia narrows which flowers can be crushed directly into vanilla dye. This keeps early dye progression tied to specific world finds and Materia plants rather than every vanilla flower.

Requires the built-in **`materia_vanilla_overrides`** datapack (on by default).

## Allowed vanilla flower → dye recipes

These still work when overrides are enabled (re-declared in the override pack):

| Flower | Dye output | Recipe |
|--------|------------|--------|
| `minecraft:poppy` | pink dye | `data/minecraft/recipes/red_dye_from_poppy.json` |
| `minecraft:dandelion` | yellow dye | `data/minecraft/recipes/yellow_dye_from_dandelion.json` |
| `minecraft:cornflower` | light blue dye | `data/minecraft/recipes/blue_dye_from_cornflower.json` |
| `minecraft:rose_bush` | pink dye ×2 | `data/minecraft/recipes/red_dye_from_rose_bush.json` |

## Materia flower / plant → dye recipes

| Source | Dye output | Recipe |
|--------|------------|--------|
| `materia:indigo` | indigo dye | [indigo_dye.json](../../../shared/src/main/resources/data/materia/recipes/indigo_dye.json) |
| `materia:marigold` | yellow dye | [yellow_dye_from_marigold.json](../../../shared/src/main/resources/data/materia/recipes/yellow_dye_from_marigold.json) |
| `materia:hibiscus` | pink dye | [pink_dye_from_hibiscus.json](../../../shared/src/main/resources/data/materia/recipes/pink_dye_from_hibiscus.json) |

## Disabled vanilla flower dyes

Other vanilla **flower → dye** recipes in the override pack use `minecraft:barrier` as the ingredient so they no longer craft dye. Examples include tulips, allium, lilac, peony, blue orchid, sunflower, lily of the valley, wither rose, and similar.

Non-flower dye routes (combining dyes, kiln/oven chains, Materia custom dyes, etc.) are unchanged.

See also:

- [Dye cheat sheet](cheat-sheet.md)
- [Decorative flowers](../content/blocks/decorative-flowers.md)
