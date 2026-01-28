## Wood components

Materia adds craftable wood components across many wood families (vanilla + modded). These tend to exist in multiple variants (oak/birch/spruce/… and newer woods like mangrove/cherry as supported by the current MC version).

This section is intentionally “high level”: individual wood variants are usually the same mechanics with different textures and ingredient wood.

Version note:

- Wood-family availability differs by Minecraft version. See: [Version differences](../../reference/VERSION_DIFFERENCES.md)

## What’s in the wood family

- **Posts** (decorative / structural)
- **Tables**
- **Joists**
- **Trellises** (often used with vines)

## Supported variants (from `shared` recipes)

This table is derived from the recipe filenames in `shared/src/main/resources/data/materia/recipes/`.

| Wood family | Posts | Tables | Joists | Trellises |
|---|---:|---:|---:|---:|
| Oak | ✅ | ✅ | ✅ | ✅ |
| Spruce | ✅ | ✅ | ✅ | ✅ |
| Birch | ✅ | ✅ | ✅ | ✅ |
| Jungle | ✅ | ✅ | ✅ | ✅ |
| Acacia | ✅ | ✅ | ✅ | ✅ |
| Dark oak | ✅ | ✅ | ✅ | ✅ |
| Rubber wood (mod) | ✅ | ✅ | ✅ | ✅ |
| Cherry | ✅ | ✅ | ✅ | ✅ |
| Mangrove | ❌ | ❌ | ✅ | ❌ |
| Crimson | ❌ | ❌ | ✅ | ❌ |
| Warped | ❌ | ❌ | ✅ | ❌ |

Notes:

- Mangrove is currently represented in `shared` via **joists**, but (in this repo) mangrove posts/tables/trellises exist in the `1.19.2` folder rather than `shared`.
- Nether wood (crimson/warped) currently shows up in `shared` via **joists**, but other components may live in version folders.

See: [Version differences](../../reference/VERSION_DIFFERENCES.md)

## Recipes (source of truth)

Most variants are defined as separate recipe JSON files in:

- `shared/src/main/resources/data/materia/recipes/`

Examples (not exhaustive):

- Posts: `*_post.json`
- Tables: `*_table.json`
- Joists: `*_joists.json`
- Trellises: `*_trellis.json`

## Pages

- [Posts](wood-posts.md)
- [Tables](wood-tables.md)
- [Joists](wood-joists.md)
- [Trellises](wood-trellises.md)
