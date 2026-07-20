## Stone balustrades

Low carved stone rail segments for balconies, terraces, and roof edges. Added in the 1.2.2 decorative pass.

## Blocks

| Material | Block ID | Stonecutter input |
| --- | --- | --- |
| Stone | `materia:stone_balustrade` | `minecraft:stone` |
| Limestone | `materia:limestone_balustrade` | `materia:limestone` |
| Marble | `materia:marble_balustrade` | `materia:marble` |
| Terracotta | `materia:terracotta_balustrade` | `minecraft:terracotta` |
| Blackstone | `materia:blackstone_balustrade` | `minecraft:blackstone` |
| Sandstone | `materia:sandstone_balustrade` | `minecraft:sandstone` |

Recipes: `{material}_balustrade_from_{material}_stonecutting.json` under `shared/src/main/resources/data/materia/recipes/`.

## Connection behavior

Balustrades use custom connection logic (not vanilla `FenceBlock`):

- Segments connect on **N/S/E/W** only to **other balustrades of the same material**.
- **Straight** runs, **L-corners**, **T-junctions**, and **4-way crosses** are chosen automatically from neighbor layout (multipart blockstate).
- An **isolated** segment (no neighbors) keeps a **facing** property so you can orient a short run **north–south** or **east–west** when placing.
- Neighbors refresh when you place or break a segment.

Collision follows the visible rails (posts, arms, and straight rail geometry).

## Inventory icons

Each material uses a dedicated item texture (`assets/materia/textures/item/{material}_balustrade.png`), not the block model snapshot.

## Asset generator

`tools/generate_balustrade_assets.py` — reads `stone_balustrade.json` and `stone_balustrade_corner.json` templates and emits models, blockstates, recipes, loot, lang, and item models for all six materials.

## Related

- [Stone columns, capitals, cornices & brackets](stone-trim.md)
- [Shutters, curtains, planters & urns](shutters-curtains-planters.md)
