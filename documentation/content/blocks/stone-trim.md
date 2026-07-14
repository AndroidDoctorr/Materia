## Stone columns, capitals, cornices, and brackets

Classical trim and column pieces for stone, limestone, marble, sandstone, blackstone, and terracotta. All variants are carved on the **stonecutter** from the matching base block (or `minecraft:terracotta` for terracotta pieces).

## Columns and capitals

- **Columns**: `materia:{material}_column` — a fluted shaft (12×12 px footprint, full block height)
- **Capitals**: `materia:{material}_column_capital` — a Doric-style capital that stacks on top of a column or wall
- **Materials**: stone, limestone, marble, sandstone, blackstone, terracotta
- **Behaviour**: plain decorative blocks (no connection logic)
- **Inventory icon**: rendered from the block model at an angle (not a flat item sprite)
- **Recipes**: `{material}_column_from_{material}_stonecutting.json` and `{material}_column_capital_from_{material}_stonecutting.json` under `shared/src/main/resources/data/materia/recipes/`

## Cornices and brackets

- **Cornices**: `materia:{material}_cornice` — wall-mounted trim with straight, inner-corner, and outer-corner shapes (same connection rules as vanilla stairs)
- **Brackets**: `materia:{material}_bracket` — wall-mounted console; requires solid backing
- **Materials**: same six as columns
- **Inventory icon**: dedicated item textures in `assets/materia/textures/item/`
- **Asset generator**: `tools/generate_marble_trim_assets.py` (regenerates blockstates, models, loot, and stonecutter recipes for all materials)

## Related

- [Shutters, curtains, planters, and urns](shutters-curtains-planters.md)
- [Marble](marble.md) — worldgen and other marble stonecutter outputs
