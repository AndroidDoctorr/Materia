## Tables

Tables are craftable wood components with variants per wood family.

## Supported variants

In `shared/src/main/resources/data/materia/recipes/`, tables exist for:

- oak, spruce, birch, jungle, acacia, dark oak
- rubber wood (mod)
- cherry

Version note:

- Mangrove tables exist in `1.19.2/src/main/resources/data/materia/recipes/mangrove_table.json` (not currently in `shared`).

## Recipes

Recipe files are variant-per-wood:

- `shared/src/main/resources/data/materia/recipes/*_table.json`

Common recipe pattern:

- **Shaped**
  - uses **posts** of the matching wood family
  - uses **smooth planks** of the matching wood family
  - uses **nails**: `#materia:all_nails` (see [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_nails))
  - **Output**: 1 table

Related tags:

- `#materia:all_nails` (see [tag reference](../../reference/tags/tools-and-tool-like-tags.md#materiaall_nails); tag JSON: `shared/src/main/resources/data/materia/tags/items/all_nails.json`)

See: [Version differences](../../reference/VERSION_DIFFERENCES.md)
