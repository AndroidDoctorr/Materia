## Metal finials

Roof and pillar finials in **bronze**, **gold**, and **wrought iron**. Spires and ball finials are **two blocks tall** (lower + upper cross models, like yucca/plantain). Acorn finials are **one block tall**.

## Variants

| Shape | Block IDs | Height |
|-------|-----------|--------|
| Spire | `materia:{metal}_spire` | 2 blocks |
| Ball finial | `materia:{metal}_ball_finial` | 2 blocks |
| Acorn finial | `materia:{metal}_acorn_finial` | 1 block |

`{metal}` is `bronze`, `gold`, or `wrought_iron`.

## Crafting (iron anvil)

All finials are forged on the **Materia iron anvil** with **hot** plate + rod inputs and **hammer**, **tongs**, and **chisel** in the tool slots (`#materia:iron_hammers`, `#materia:iron_tongs`, `#materia:iron_chisels` — bronze-tier tools also qualify).

| Shape | Inputs (hot) |
|-------|----------------|
| Spire | 1× plate + **2×** rod |
| Ball finial | 1× plate + 1× rod |
| Acorn finial | 1× plate + 1× rod |

| Metal | Plate | Rod |
|-------|-------|-----|
| Bronze | `materia:bronze_plate` | `materia:bronze_rod` |
| Gold | `materia:gold_plate` | `materia:gold_rod` |
| Wrought iron | `materia:iron_plate` | `materia:iron_rod` |

Recipe files: `shared/src/main/resources/data/materia/recipes/iron_anvil/{metal}_{shape}_from_plate_rod.json`

## Placement & rendering

- **Tall finials** use `FinialBlock` (`DoublePlantBlock`) — place on any block with a solid top face; upper half appears automatically.
- **Acorn finials** are a single cross-textured block.
- All finials use **cutout** rendering (transparent texture edges).
- Inventory icons use dedicated item textures where available (`gold_spire`, `gold_ball_finial`, `gold_acorn`); others use the lower block model with **45° GUI rotation**.

## Asset generator

`tools/generate_metal_finial_assets.py` — regenerates blockstates, cross models, loot tables, item models, and iron-anvil recipes.

## Related

- [Stone columns, capitals, cornices & brackets](stone-trim.md)
- [Iron anvil](../../reference/anvil-recipes.md)
