## Floor rugs

Two-block-long decorative floor rugs (1 wide × 2 long). Added in the 1.2.2 decorative pass.

## Placement

Rugs are **double blocks** like beds:

- Place on a solid top face; the rug extends in the direction you face.
- Only the **foot** half drops the rug item when broken (head half breaks with it).

## Craftable rugs (loom)

Weave on the **vanilla loom** with three inputs:

| Slot | Item |
| --- | --- |
| Banner template (top) | `materia:rug_base` |
| Dye (middle) | Field color (see below) |
| Banner pattern (bottom) | `materia:rug_{1–4}_pattern` |

Inputs are consumed only when you take the finished rug from the output slot (`RugLoomHandler`).

### Patterns

| Pattern item | Motif | Pattern craft (shapeless) |
| --- | --- | --- |
| `materia:rug_1_pattern` | Medallion | paper + gold nugget + `materia:ochre` |
| `materia:rug_2_pattern` | Ornate | paper + gold nugget + `materia:taupe` |
| `materia:rug_3_pattern` | Rosette | paper + gold nugget + `materia:lavender` |
| `materia:rug_4_pattern` | Lattice | paper + gold nugget + `materia:olive` |

### Field colors

Each pattern is available in four field colors:

| Field dye | Accepts |
| --- | --- |
| Red | `minecraft:red_dye`, `materia:burgundy_dye` |
| Blue | `minecraft:blue_dye`, `materia:indigo_dye` |
| Green | `minecraft:green_dye` |
| Purple | `minecraft:purple_dye`, `materia:tyrian_purple_dye` |

Output blocks: `materia:rug_{1–4}_{red|blue|green|purple}` (16 craftable variants total).

### Rug base

Shapeless crafting-grid recipe (`materia:rug_base` type):

- **1×** neutral blanket from `#materia:rug_neutral_blankets`
- **16×** string from `#materia:rug_base_strings` (white or taupe string)

String stacks are partially consumed (16 total); the blanket is consumed entirely.

## Rare loot rugs (not craftable)

| Block ID | Display name | Typical loot sources |
| --- | --- | --- |
| `materia:rug_5` | Dragon Rug | Simple dungeon, mineshaft, igloo, shipwreck supply, … |
| `materia:rug_6` | Diamonds Rug | Dungeon tiers, desert pyramid, jungle temple, shipwreck, … |
| `materia:rug_7` | Navajo Rug | Same mid-tier structures |
| `materia:rug_8` | Welcome Rug | Stronghold, woodland mansion, buried treasure, … |
| `materia:rug_9` | Agrabah Rug | Buried treasure, stronghold, mansion |
| `materia:rug_10` | Rainbow Rug | Buried treasure, stronghold, mansion |

See structure loot tables under `shared/src/main/resources/data/materia/loot_tables/chests/`.

## Asset generator

`tools/generate_rug_assets.py` — block models, blockstates, item models, and lang for patterns 1–10.

## Related

- [Textiles (overview)](../../mechanics/textiles.md)
- [Structure chest loot](../../mechanics/structure-chest-loot.md)
