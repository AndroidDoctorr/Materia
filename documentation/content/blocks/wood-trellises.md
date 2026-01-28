## Trellises

Trellises are craftable wood components with variants per wood family. They’re typically used alongside vine-type content.

## Supported variants

In `shared/src/main/resources/data/materia/recipes/`, trellises exist for:

- oak, spruce, birch, jungle, acacia, dark oak
- rubber wood (mod)
- cherry

Version note:

- Mangrove trellises exist in `1.19.2/src/main/resources/data/materia/recipes/mangrove_trellis.json` (not currently in `shared`).

## Recipes

Recipe files are variant-per-wood:

- `shared/src/main/resources/data/materia/recipes/*_trellis.json`

Common recipe pattern:

- **Shaped**
  - uses **posts** of the matching wood family
  - uses **smooth planks** of the matching wood family
  - **Output**: 1 trellis

## Related

- [Version differences](../../reference/VERSION_DIFFERENCES.md)
