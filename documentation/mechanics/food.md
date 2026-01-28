## Food (overview)

Materia adds a small set of new food items and early cooking/processing chains, mostly based on the “new crops” loop.

This page is the system overview. For the item list and exact hunger values, see:

- [Food (items)](../content/items/food.md)

Related:

- [Crops and farming](crops.md)
- [Oven](../content/blocks/oven.md) (custom recipe type)
- [Fire pit](fire-pit.md) / [Kilns](kilns.md) (early heat)

## Core idea

Early food progression is meant to feel like:

- find wild crops → plant on farmland → process into “steps” (meal/dough) → cook into meals

## Key early chains (source of truth)

### Corn → cornmeal → masa dough → tortilla → burrito

- Cornmeal: `shared/src/main/resources/data/materia/recipes/cornmeal.json` (uses a mortar and pestle)
- Masa dough: `shared/src/main/resources/data/materia/recipes/masa_dough.json` (cornmeal + water)
- Tortilla (smelting): `shared/src/main/resources/data/materia/recipes/tortilla.json`
- Tortilla (campfire): `shared/src/main/resources/data/materia/recipes/tortilla_campfire.json`
- Burrito: `shared/src/main/resources/data/materia/recipes/burrito.json` (tortilla + beans + peppers)

### Corn cob → popcorn

- `shared/src/main/resources/data/materia/recipes/corn_to_popcorn_campfire.json`

### Squash → seeds / slices / dried gourd

- Squash → seeds: `shared/src/main/resources/data/materia/recipes/squash_seeds.json`
- Squash → sliced squash: `shared/src/main/resources/data/materia/recipes/sliced_squash.json` (requires `#materia:all_cutting_tools`)
- Squash → dried gourd: `shared/src/main/resources/data/materia/recipes/squash_to_dried_gourd.json`

### Flour → dough → bread

- Flour: `shared/src/main/resources/data/materia/recipes/flour.json`
- Dough: `shared/src/main/resources/data/materia/recipes/dough.json`
- Bread (campfire): `shared/src/main/resources/data/materia/recipes/dough_to_bread_campfire.json`
- Bread (oven): `shared/src/main/resources/data/materia/recipes/oven_bread.json` (custom recipe type: `materia:oven`)

## The oven (custom cooking)

The oven supports a custom recipe type (`materia:oven`) used to cook foods like bread and meats.

Recipe JSONs live under:

- `shared/src/main/resources/data/materia/recipes/oven_*.json`

