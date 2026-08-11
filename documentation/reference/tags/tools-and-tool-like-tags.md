## Tools and tool-like tags

This page is for “this recipe accepts a tool from a tag” style requirements.

## Cutting tools and knives

See: [Early crafting and woodworking](early-crafting-and-woodworking.md)

## `#materia:all_knives`

Used by: recipes that accept any knife tier.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_knives.json`
- **Includes**: flint, bronze, iron, steel knives

## Saws

## `#materia:all_saws`

Used by: saw-required recipes and components.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_saws.json`
- **Includes**: `#forge:tools/saws` → `materia:bronze_saw`, `materia:iron_saw`

## Construction fasteners and joinery

These aren’t “tools” in-hand, but they behave like tool requirements in many recipes (tables/joists/etc.).

## `#materia:all_nails`

Used by: tables, joists, and other “fastened wood” recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_nails.json`
- **Includes**: `materia:bronze_nails`, `materia:iron_nails`

## `#materia:all_wood_joiners`

Used by: joists and other joinery-heavy woodworking recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_wood_joiners.json`
- **Includes (currently)**:
  - `#materia:all_nails`
  - `#materia:strong_adhesives`
  - `materia:leather_strap`
  - `materia:rope`

## `#materia:crushers`

Used by: recipes that accept “any crushing tool” (mortar-and-pestle or hammers).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/crushers.json`
- **Includes**: `materia:mortar_and_pestle` and `#materia:all_hammers`

## `#materia:all_sawblades`

Used by: saw crafting and saw maintenance chains.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_sawblades.json`
- **Includes**: bronze/iron/steel sawblades

## Armor-related crafting tags

These show up heavily in armor, horse armor, and other “plate + fastener” recipes.

## `#materia:all_rivets`

Used by: armor components, armor assembly recipes, horse armor, and other metalwork-with-fasteners.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_rivets.json`
- **Includes**: `materia:brass_rivets`, `materia:iron_rivets`

## `#materia:bronze_hammers` / `#materia:iron_hammers`

Used by: recipes that require “a hammer of at least this tier”.

- **Bronze hammer tag JSON**: `shared/src/main/resources/data/materia/tags/items/bronze_hammers.json`
  - Includes: `materia:bronze_hammer`, plus higher tiers via `#materia:iron_hammers`
- **Iron hammer tag JSON**: `shared/src/main/resources/data/materia/tags/items/iron_hammers.json`

Related:

- [Armor (overview)](../../content/items/armor.md)

## Tongs

Used by: hot-metal handling and forging systems.

See also: [Tongs](../../content/items/tongs.md), [Hot metals](../../mechanics/hot-metals.md)

## `#materia:wood_tongs`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/wood_tongs.json`
- **Includes**: `materia:wood_tongs` plus higher tiers via `#materia:bronze_tongs`

## `#materia:all_tongs`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_tongs.json`
- **Includes**: `#materia:wood_tongs`

## Anvil tool tags

The anvil system leans heavily on tool tags for slot requirements. That’s documented separately:

- [Anvil tool tags cheat sheet](../anvil-tool-tags.md)
