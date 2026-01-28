## Posts

Posts are craftable wood components with variants per wood family.

## Supported variants

In `shared/src/main/resources/data/materia/recipes/`, posts exist for:

- oak, spruce, birch, jungle, acacia, dark oak
- rubber wood (mod)
- cherry

Version note:

- Mangrove posts exist in `1.19.2/src/main/resources/data/materia/recipes/mangrove_post.json` (not currently in `shared`).

## Recipes

Recipe files are variant-per-wood:

- `shared/src/main/resources/data/materia/recipes/*_post.json`

Common recipe pattern:

- **Shapeless**
  - **1 log** (wood-family specific)
  - **1 saw**: `#materia:all_saws` (see [tag reference](../../reference/tags/tools-and-tool-like-tags.md#materiaall_saws))
  - **Output**: 4 posts

See also:

- [Saws tag (`#materia:all_saws`)](../../reference/tags/tools-and-tool-like-tags.md#materiaall_saws)

## Notes

- There is a special-case recipe that allows olive wood logs to produce oak posts:
  - `shared/src/main/resources/data/materia/recipes/oak_post_from_olive_wood.json`

See: [Version differences](../../reference/VERSION_DIFFERENCES.md)
