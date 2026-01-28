## Joists

Joists are craftable wood components with variants per wood family (including some non-overworld woods in newer versions).

## Supported variants

In `shared/src/main/resources/data/materia/recipes/`, joists exist for:

- oak, spruce, birch, jungle, acacia, dark oak
- rubber wood (mod)
- mangrove
- cherry
- crimson
- warped

## Recipes

Recipe files are variant-per-wood:

- `shared/src/main/resources/data/materia/recipes/*_joists.json`

Common recipe pattern:

- **Shaped** (`PNP`)
  - `P`: smooth planks of the matching wood family
  - `N`: joinery/fastener tag: `#materia:all_wood_joiners` (see [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_wood_joiners))
  - **Output**: 1 joists block

Tag notes:

 - `#materia:all_wood_joiners` is a “composite” requirement; it currently includes nails plus strong adhesives and some other bindings (see [tag reference](../../reference/tags/tools-and-tool-like-tags.md#materiaall_wood_joiners)).
  - Tag JSON: `shared/src/main/resources/data/materia/tags/items/all_wood_joiners.json`

See: [Version differences](../../reference/VERSION_DIFFERENCES.md)
