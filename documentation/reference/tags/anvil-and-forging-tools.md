## Anvil and forging tool tags

These tags are used by anvil recipes to say “any tool of at least this tier”.

In practice, the tags are *nested upward* (higher tiers include lower tiers), so once you upgrade your tools you don’t have to keep old ones around just to satisfy recipes.

For the full anvil tool-tag breakdown, see:

- [Anvil tool tags cheat sheet](../anvil-tool-tags.md)

## Hammers

### `#materia:basic_hammers`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/basic_hammers.json`
- **Includes**: `materia:hammer_stone`, `materia:stone_hammer`

### `#materia:stone_hammers`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/stone_hammers.json`
- **Includes**: `#materia:basic_hammers`, `#materia:bronze_hammers`

### `#materia:steel_hammers`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/steel_hammers.json`
- **Includes**: `materia:steel_hammer`

### `#materia:all_hammers`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_hammers.json`
- **Includes**: `#materia:stone_hammers`

## Chisels

### `#materia:bronze_chisels`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/bronze_chisels.json`
- **Includes**: `materia:bronze_chisel`, `#materia:iron_chisels`

### `#materia:iron_chisels`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/iron_chisels.json`
- **Includes**: `materia:iron_chisel`

### `#materia:all_chisels`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_chisels.json`
- **Includes**: `#materia:bronze_chisels`

## Bores

### `#materia:bronze_bores`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/bronze_bores.json`
- **Includes**: `materia:bronze_bore`, `#materia:iron_bores`

### `#materia:iron_bores`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/iron_bores.json`
- **Includes**: `materia:iron_bore`

### `#materia:all_bores`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_bores.json`
- **Includes**: `#materia:bronze_bores`

## Drawplates

### `#materia:bronze_drawplates`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/bronze_drawplates.json`
- **Includes**: `materia:bronze_drawplate`, `#materia:iron_drawplates`

### `#materia:iron_drawplates`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/iron_drawplates.json`
- **Includes**: `materia:iron_drawplate`

## Tongs

### `#materia:iron_tongs`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/iron_tongs.json`
- **Includes**: `materia:iron_tongs`, `#materia:steel_tongs`

### `#materia:steel_tongs`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/steel_tongs.json`
- **Includes**: `materia:steel_tongs`

