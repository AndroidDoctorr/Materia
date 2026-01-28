## Hops and beer

This page documents the **hops** plant system and the **beer** brewing chain.

It’s designed to mirror the existing grape/wine flow:

- **Hops** grow as a vine-like crop (farmland → supports)
- **Beer** is fermented in an **amphora**, then moved into cups/bottles/pots/buckets

## Hops (plant)

Hops exist in three main forms:

- **Planted hops vine**: `materia:hops_vine`
- **Hanging hops** (placed under supports): `materia:hops_hanging`
- **Wild hops vine** (worldgen on trees): `materia:wild_hops_vine`

### Planting and growth (farmland → supports)

1) Plant `materia:hops_seeds` on **farmland**.
2) As it matures, it can **attach to nearby supports** (trellises/posts/joists).
3) Once attached, supports can eventually produce **hops** and can place **hanging hops** below if there’s air.

Supports are controlled by the block tag:

- `#materia:hops_vine_supports`

### Harvesting

Depending on growth state, right-click harvesting can yield:

- **Hops** (`materia:hops`)
- **Hops seeds** (`materia:hops_seeds`)

Wild hops vines can also drop seeds (and may drop plant fiber), similar to wild grapes/wisteria.

## Beer (brewing + fermentation)

Beer brewing happens in an **amphora** and has an “ingredient staging” phase before fermentation starts.

### Step 1: Make beer mash in an amphora

1) Fill an amphora with **water**.
2) Add ingredients by right-clicking the amphora (any order, one at a time):
   - **4× wheat**
   - **1× hops**

Rules:

- The amphora **won’t accept a lid** until the mash is complete.
- Once an ingredient is “fully satisfied” (e.g. hops already added), it stops accepting more of that ingredient.
- The amphora shows a status message (actionbar) describing what’s been added so far.

### Step 2: Ferment mash → beer

Once the mash is complete:

1) Add a lid to start fermentation.
2) Wait for fermentation to complete (time-based).
3) The amphora’s liquid becomes **beer**.

Important:

- **Beer mash is not extractable** (one-way only).
- Finished **beer** can be extracted like other amphora liquids using small/large containers.

## Beer containers and serving

Beer supports both “small” and “large” containers:

- **1 unit (cup/bottle-sized)**:
  - `materia:beer_cup`
  - `materia:beer_bottle`
- **3 units (bucket/pot-sized)**:
  - `materia:beer_bucket`
  - `materia:beer_pot` (item)

### Placeable beer/wine pots (shared servings)

There are world-placed pot blocks for communal serving:

- `materia:beer_pot` (block)
- `materia:wine_pot` (block)

Behavior:

- Pots have **levels 0–3** (low/medium/full visuals).
- Multiple players can take servings from the same placed pot.
- When the pot hits **0**, it becomes an empty `materia:pot`.
- When broken, filled pots drop an **empty pot**.

See also:

- [Amphora and liquids](amphora-and-liquids.md)
- [Pot (empty)](../content/blocks/pot.md)
- [Liquids and containers (reference)](../reference/tags/liquids-and-containers.md)
