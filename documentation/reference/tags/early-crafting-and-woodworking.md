## Early crafting and woodworking

These are the tags that show up in early-game crafting and basic woodworking recipes.

## `#materia:basic_cutting_tools`

Used by: [Handle](../../content/items/handle.md), [Bone handle](../../content/items/bone-handle.md), tree tapping, early recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/basic_cutting_tools.json`
- **Includes**: `materia:knapped_flint`, `materia:flint_knife`, `materia:bronze_knife`

## `#materia:all_cutting_tools`

Used by: recipes that accept “any cutting tool” (including higher tiers / swords).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_cutting_tools.json`
- **Includes**:
  - `#materia:basic_cutting_tools`
  - `#materia:all_knives`
  - vanilla swords: iron/diamond/netherite

## `#materia:rough_planks`

Used by: [Handle](../../content/items/handle.md), early woodworking chains.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/rough_planks.json`
- **Includes**: all rough plank variants. Some entries are marked optional to support older MC versions:
  - mangrove/crimson/warped rough planks may be `"required": false`

## `#materia:smooth_planks`

Used by: upgraded woodworking components, furniture, and some machine-ish blocks (ex: bellows).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/smooth_planks.json`
- **Includes**: all smooth plank variants; some entries are optional for version compatibility (mangrove/crimson/warped).

## `#materia:posts`

Used by: structural recipes (supports, some machines, etc.).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/posts.json`
- **Includes**: all post variants; some entries are optional for version compatibility (mangrove/crimson/warped).

## `#materia:basic_axes`

Used by: [Hewing](../../mechanics/hewing.md), early axe-accepted recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/basic_axes.json`
- **Includes**: `materia:hand_axe`, `minecraft:wooden_axe`, `minecraft:stone_axe`

## `#materia:all_axes`

Used by: recipes that accept higher-tier axes (ex: sticks from rough planks).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_axes.json`
- **Includes**:
  - `#materia:basic_axes`
  - `#materia:bronze_axes`
