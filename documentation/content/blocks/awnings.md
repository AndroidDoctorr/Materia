## Awnings

Sloped fabric canopies for shopfronts and patios. Added in the 1.3.0 decorative pass.

## Blocks

- **Registry:** `materia:{color}_awning` for all **26** curtain/awning colors (16 vanilla dyes plus Materia palette: ochre, red ochre, lavender, indigo, tyrian purple, taupe, olive, charcoal gray, burgundy, teal, tan).
- **Geometry:** same sloped frame as **`roof_tiles`** stage 0 (shared arm texture `awning_arm`; colored fabric per dye).
- **Render:** cutout layer.

## Crafting

Shaped recipe (yields 1):

```
SRS
CSW
```

- **S** — stick  
- **R** — `materia:rope`  
- **C** — matching **colored carpet** for that awning  
- **W** — `minecraft:white_carpet`

Example: [teal awning](../../../shared/src/main/resources/data/materia/recipes/teal_awning.json)

## Corners

Adjacent awnings of the **same color** auto-connect at 90° like roof tiles:

- **Straight** — single slope along the ridge facing.
- **Inner-left / inner-right** — two slopes meet with backs together (L-shaped run).
- **Outer-left / outer-right** — two slopes meet at the open edge.

When `{color}_awning_corner_left.png` and `{color}_awning_corner_right.png` exist in `assets/materia/textures/block/`, corner models use those triangle textures on the joined faces; otherwise corners reuse the flat awning fabric.

## Placement

- Place against a solid face; **facing** follows the placer (ridge direction).
- Walkable sloped collision matches the visible fabric (including corner shapes).

## Asset generator

`tools/generate_awning_assets.py` — block models (straight + four corner shapes), blockstates, item models, recipes, and lang for all colors.

## Related

- [Roof tiles & thatch roofs](roof-tiles.md) — shared slope/corner connection rules
- [Shutters, curtains, planters & urns](shutters-curtains-planters.md)
