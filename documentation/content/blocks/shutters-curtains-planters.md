## Shutters, curtains, planters, and urns

Wall-mounted shutters and curtains plus carved-stone planters and urns for Mediterranean-style builds. Added in the 1.2.2 decorative pass.

## Shutters

- **Blocks**: `materia:{wood}_shutters` for oak, spruce, birch, jungle, acacia, dark oak, cherry, mangrove, rubber wood, fig, cedar, and eucalyptus
- **Placement**: always **upright** on a vertical face — click the wall/jamb face directly, or click the **top of the sill** / bottom of the lintel to place in the window block above/below. Right-click toggles open/closed; redstone opens shutters on placement
- **Inside vs outside**: clicking a **wall face** always mounts flush on that face. Clicking a **sill/lintel** (top or bottom face): the half of that face **closer to you** places the panel on the near side of the window; the **far half** places it on the far side
- **Models**: closed panel is 2 px thick against the wall; open halves fold outward with correct edge UVs on the thin and wide faces
- **Sound**: fence-gate open/close
- **Support**: can sit on the top of a block with only that block underneath (no wall behind required)
- **Recipe** (yields 2): shaped `JSJ` / `PSP` / `JSJ` — `#materia:all_wood_joiners`, stick, matching **smooth** plank per wood (`shared/src/main/resources/data/materia/recipes/{wood}_shutters.json`)

## Curtains

- **Blocks**: `materia:{color}_curtains` for all 16 vanilla dye colors plus Materia palette colors (ochre, red ochre, lavender, indigo, tyrian purple, taupe, olive, charcoal gray, burgundy, teal)
- **Placement**: same upright rules as shutters — vertical faces only; sill/lintel clicks place into the adjacent window air block. Toggle open/closed with right-click
- **Support**: in a window, curtains need a **solid block behind** the panel (wall or jamb). Shutters can sit on the top of a block with only that block underneath
- **Inside vs outside**: same rules as shutters
- **Render**: cutout layer (translucent fabric)
- **Sound**: custom `materia:curtains`
- **Recipe** (yields 2): shaped `CSC` — matching **carpet** + stick + matching carpet (`shared/src/main/resources/data/materia/recipes/{color}_curtains.json`)

## Stone planter

- **Block**: `materia:stone_planter` — half-slab footprint (8×16×8 collision) with optional **facing**
- **Obtain**: stonecutter from `minecraft:stone` (`stone_planter_from_stonecutting.json`)
- **Planting**: right-click with a block in `#materia:planter_plants` to fill one of **two** slots (vanilla flowers, saplings, ferns, mushrooms, Materia wild crops/flowers, etc.). Right-click empty hand on a filled slot removes that plant. Breaking drops the planter and any plants inside
- **Tag**: `shared/src/main/resources/data/materia/tags/blocks/planter_plants.json`
- **Note**: tall double plants (yucca, plantain) do **not** fit — use an urn instead

## Stone urn

- **Block**: `materia:stone_urn` — full-block collision with a carved bowl
- **Obtain**: stonecutter from `minecraft:stone` (`stone_urn_from_stonecutting.json`)
- **Planting**: right-click with a block in `#materia:urn_plants` (includes everything in planter plants plus tall flowers, tall grass, yucca, plantain, etc.). One plant per urn; saplings display but do not grow. Same remove/pickup/break rules as the planter
- **Tag**: `shared/src/main/resources/data/materia/tags/blocks/urn_plants.json`

## Related stone decor

Stonecutter variants (tiles, small bricks, polished/chiseled where textures exist) and **`materia:stone_balustrade`** (fence-style rails with corner multipart) share the same decorative stone set. See recipe files under `shared/src/main/resources/data/materia/recipes/*_from_stonecutting.json`.

## Version note

Planter and urn block entities (interactive planting) are implemented on **1.20.1**; shutters and curtains are registered on all supported Materia versions.
