## Food

This page documents Materia’s food items and the early cooking chains they’re part of.

System overview:

- [Food (overview)](../../mechanics/food.md)
- [Crops and farming](../../mechanics/crops.md)

## Quick reference (edible items, 1.18.2)

Hunger values are from `1.18.2/src/main/java/com/torr/materia/ModItems.java` food properties.

Notes:

- Hunger is shown as \(nutrition\) (“food points”); **1 hunger icon = 2 food points**.
- Saturation is shown as the `saturationMod` value (vanilla-style).

| Item | Nutrition | Saturation | Notes |
|---|---:|---:|---|
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/beans.png" alt="Beans" width="16" height="16"> `materia:beans` | 3 | 0.4 | Also acts as “seed” for beans crop |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/peppers.png" alt="Peppers" width="16" height="16"> `materia:peppers` | 1 | 0.2 | — |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/corn_cob.png" alt="Corn cob" width="16" height="16"> `materia:corn_cob` | 4 | 0.5 | Used for popcorn |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/popcorn.png" alt="Popcorn" width="16" height="16"> `materia:popcorn` | 4 | 0.5 | Campfire cooking |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/grapes.png" alt="Grapes" width="16" height="16"> `materia:grapes` | 2 | 0.2 | From grape vines |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/sliced_squash.png" alt="Sliced squash" width="16" height="16"> `materia:sliced_squash` | 1 | 0.2 | Needs cutting tool in recipe |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/baked_squash.png" alt="Baked squash" width="16" height="16"> `materia:baked_squash` | 5 | 0.6 | No recipe JSON found yet (see note below) |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/tortilla.png" alt="Tortilla" width="16" height="16"> `materia:tortilla` | 4 | 0.4 | Smelting or campfire cooking |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/burrito.png" alt="Burrito" width="16" height="16"> `materia:burrito` | 8 | 0.9 | Tortilla + beans + peppers |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/fresh_cheese.png" alt="Fresh cheese" width="16" height="16"> `materia:fresh_cheese` | 4 | 0.5 | Made from milk + vinegar + salt + linen |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/porridge.png" alt="Porridge" width="16" height="16"> `materia:porridge` | 7 | 0.8 | No recipe JSON found yet (see note below) |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/chili.png" alt="Chili" width="16" height="16"> `materia:chili` | 1 | 0.2 | No recipe JSON found yet (see note below) |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/salt.png" alt="Salt" width="16" height="16"> `materia:salt` | 1 | 0.1 | `alwaysEat()` (you can eat it even when full) |
| <img src="../../../shared/src/main/resources/assets/materia/textures/item/flour.png" alt="Flour" width="16" height="16"> `materia:flour` | 1 | 0.1 | Ingredient item, but edible in 1.18.2 |

Also edible (no icons shown here):

- `materia:dried_gourd` (made by cooking squash; see recipes below)

## Ingredient / processing items (not edible)

These are “food chain” ingredients but do not have food properties in 1.18.2:

- <img src="../../../shared/src/main/resources/assets/materia/textures/item/cornmeal.png" alt="Cornmeal" width="16" height="16"> `materia:cornmeal`
- <img src="../../../shared/src/main/resources/assets/materia/textures/item/masa_dough.png" alt="Masa dough" width="16" height="16"> `materia:masa_dough`
- <img src="../../../shared/src/main/resources/assets/materia/textures/item/dough.png" alt="Dough" width="16" height="16"> `materia:dough`

## Recipes (source of truth)

### Corn and tortillas

- Cornmeal: `shared/src/main/resources/data/materia/recipes/cornmeal.json`
- Masa dough: `shared/src/main/resources/data/materia/recipes/masa_dough.json`
- Tortilla (smelting): `shared/src/main/resources/data/materia/recipes/tortilla.json`
- Tortilla (campfire): `shared/src/main/resources/data/materia/recipes/tortilla_campfire.json`
- Burrito: `shared/src/main/resources/data/materia/recipes/burrito.json`
- Popcorn: `shared/src/main/resources/data/materia/recipes/corn_to_popcorn_campfire.json`

### Squash processing

- Squash → seeds: `shared/src/main/resources/data/materia/recipes/squash_seeds.json`
- Squash → sliced squash: `shared/src/main/resources/data/materia/recipes/sliced_squash.json`
- Squash → dried gourd: `shared/src/main/resources/data/materia/recipes/squash_to_dried_gourd.json`

### Bread (dough)

- Flour: `shared/src/main/resources/data/materia/recipes/flour.json`
- Dough: `shared/src/main/resources/data/materia/recipes/dough.json`
- Bread (campfire): `shared/src/main/resources/data/materia/recipes/dough_to_bread_campfire.json`
- Bread (oven): `shared/src/main/resources/data/materia/recipes/oven_bread.json`

### Fresh cheese

- `shared/src/main/resources/data/minecraft/recipes/fresh_cheese.json`

Ingredients used:

- `#materia:milk`
- `#materia:vinegar`
- `materia:salt`
- `#materia:linens`

## Notes (items without a recipe yet)

As of the current `shared/` datapack state:

- I didn’t find any recipe JSONs that produce:
  - `materia:baked_squash`
  - `materia:porridge`
  - `materia:chili`

Those items are registered in code with food values, but may be “future” or produced by a system not yet expressed as datapack recipes.

