## Pot (empty)

<img src="../../../shared/src/main/resources/assets/materia/textures/item/pot.png" alt="Pot" width="64" height="64">

The pot is the base ceramic container block. On its own, it’s empty—but it can be converted into liquid pots (water, milk, etc.).

## Obtaining

1) Craft an unfired clay pot:

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/clay_pot.json)

2) Fire it in a kiln:

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/clay_pot_to_pot.json)

## Water conversions (vanilla compatibility)

An empty pot can be converted into a **water pot** by:

- using a [Water cup](../items/water-cup.md) on it (1 unit)
- using a vanilla **water bottle** on it (1 unit)
- using a vanilla **water bucket** on it (3 units)
- sneak-using with a **cauldron above** (pulls up to 3 layers)

See: [Water pot (mechanic)](../../mechanics/water-pot.md)

## Beer and wine conversions (serving pots)

An empty pot can also be converted into a **beer pot** or **wine pot** (the placeable serving blocks):

- **From cup** (1 unit)
  - `materia:beer_cup` → `materia:beer_pot` (level 1), returns **crucible**
  - `materia:wine_cup` → `materia:wine_pot` (level 1), returns **crucible**
- **From bottle** (1 unit)
  - `materia:beer_bottle` → beer pot (level 1), returns **glass bottle**
  - `materia:wine_bottle` → wine pot (level 1), returns **glass bottle**
- **From bucket** (3 units)
  - `materia:beer_bucket` → beer pot (level 3), returns **bucket**
  - `materia:wine_bucket` → wine pot (level 3), returns **bucket**
- **From pot item** (3 units)
  - `materia:beer_pot` (item) → beer pot (level 3), returns **empty pot item**
  - `materia:wine_pot` (item) → wine pot (level 3), returns **empty pot item**

Serving behavior:

- Multiple players can take servings from the same placed pot.
- When a beer/wine pot reaches **0**, it becomes an empty `materia:pot`.
- Breaking a beer/wine pot drops an **empty pot**.

## Related

- [Water pot (block)](water-pot.md)
- [Milk pot (block)](milk-pot.md)
- [Hops and beer](../../mechanics/hops-and-beer.md)
- [Liquids and containers (reference)](../../reference/tags/liquids-and-containers.md)
