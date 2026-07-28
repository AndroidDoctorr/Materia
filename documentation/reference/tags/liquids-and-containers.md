## Liquids and ceramic containers

Torr’s Mod leans on a simple “container size” system for early ceramics:

- **Crucible / cup** ≈ **bottle-sized**
- **Pot** ≈ **bucket/cauldron-sized**
- **1 pot/bucket = 3 cups/bottles**

This ties directly into:

- [Water pot](../../mechanics/water-pot.md)
- [Amphora and liquids](../../mechanics/amphora-and-liquids.md) (including fermentation)
- [Chemistry → Adhesives](../../chemistry/adhesives.md)
- [Chemistry → Dyes](../../chemistry/dyes.md)

Related content pages:

- [Crucible](../../content/items/crucible.md)
- [Water cup](../../content/items/water-cup.md)
- [Milk cup](../../content/items/milk-cup.md)
- [Pot (empty)](../../content/blocks/pot.md)
- [Water pot (block)](../../content/blocks/water-pot.md)
- [Milk pot (block)](../../content/blocks/milk-pot.md)

## Vanilla compatibility goal

The intended mental model is:

- Any place that accepts a **cup/crucible** should also accept **vanilla glass bottles (water bottles)** for water-type transfers.
- Any place that accepts a **pot** should behave like a **bucket** and (for water) interact sensibly with **cauldrons**.

If something breaks that expectation, it’s a good “possible code gap” to investigate.

## What currently works (confirmed in code)

### Water transfers

- **Crucible ⇄ water sources / cauldrons**
  - `materia:crucible` can fill from **water source blocks** and **water cauldrons** (1 layer) → becomes `materia:water_cup`.
- **Water cup ⇄ cauldrons**
  - `materia:water_cup` can pour into a cauldron (adds 1 layer) → returns `materia:crucible`.
- **Pot → water pot**
  - An empty `materia:pot` block can become a water pot by:
    - water cup (1 unit)
    - vanilla water bottle (1 unit)
    - vanilla water bucket (3 units)
    - sneak-use with a cauldron above (pulls up to 3 layers)
- **Water pot ⇄ vanilla bottles/buckets/cauldrons**
  - Water pot supports:
    - `minecraft:glass_bottle` ⇄ water bottle (1 level)
    - `minecraft:bucket` ⇄ water bucket (3 levels)
    - sneak-transfer with cauldron above (both directions)

### Milk transfers

- **Milk pot ⇄ vanilla bottles/buckets**
  - Milk pot supports:
    - `minecraft:glass_bottle` ⇄ `materia:milk_bottle` (1 level)
    - `minecraft:bucket` ⇄ `minecraft:milk_bucket` (3 levels)
- **Milk pot ⇄ crucible**
  - `materia:crucible` ⇄ `materia:milk_cup` (1 level)

### Amphora transfers (liquid storage)

Amphora liquid storage supports both “small” and “large” containers:

- **Small (1 unit)**: bottles / cups / crucibles
- **Large (3 units)**: pots / buckets

See: [Amphora and liquids](../../mechanics/amphora-and-liquids.md)

## Other liquid mechanics (currently amphora-first)

Some liquids have dedicated container *items* (ex: grape juice / vinegar / olive oil pots and bottles) and are supported for amphora transfer, but do not currently have matching “pot block” variants documented the way water/milk do.

Beer and wine are a special case:

- They *do* have dedicated **placeable pot blocks** for communal serving:
  - `materia:beer_pot` (block)
  - `materia:wine_pot` (block)

If you want full parity (“pot == bucket == cauldron-sized container” for more liquids), that’s a good place to either:

- add more pot-like blocks (vinegar pot, grape juice pot, etc.), or
- document the intended limitation (amphora is the main storage/transfer hub for non-water/milk liquids).

## Container variants by liquid (quick reference)

This is the “do we have a cup/bottle/pot/bucket for this liquid?” table.

Legend:

- **1 unit**: cup/bottle-sized
- **3 units**: pot/bucket-sized
- **Pot block**: the world-placed 0–3 level ceramic pot (water/milk only, currently)

| Liquid | 1 unit (cup/bottle) | 3 units (pot/bucket) | Pot block | Notes |
|---|---|---|---|---|
| Water | [Water cup](../../content/items/water-cup.md), vanilla water bottle | vanilla water bucket | [Water pot](../../content/blocks/water-pot.md) | Also supports cauldron transfer |
| Milk | [Milk cup](../../content/items/milk-cup.md), `materia:milk_bottle` | vanilla milk bucket | [Milk pot](../../content/blocks/milk-pot.md) | Bottle is modded; bucket is vanilla |
| Grape juice | `materia:grape_juice`, `materia:grape_juice_bottle` | `materia:grape_juice_pot`, `materia:grape_juice_bucket` | — | Typically transferred via amphora |
| Vinegar | `materia:vinegar`, `materia:vinegar_bottle` | `materia:vinegar_pot`, `materia:vinegar_bucket` | — | Typically transferred via amphora |
| Wine | `materia:wine_cup`, `materia:wine_bottle` | `materia:wine_pot`, `materia:wine_bucket` | `materia:wine_pot` | Also has a placeable serving pot |
| Beer | `materia:beer_cup`, `materia:beer_bottle` | `materia:beer_pot`, `materia:beer_bucket` | `materia:beer_pot` | Brewed via amphora (mash → beer) |
| Olive oil | `materia:olive_oil`, `materia:olive_oil_bottle` | `materia:olive_oil_pot`, `materia:olive_oil_bucket` | — | Typically transferred via amphora |
| Ink | `materia:ink_cup`, `materia:ink_bottle` | — | — | Used as an ingredient/container, not a world pot |
| Lava | — | vanilla lava bucket, `materia:lava_pot` | — | Amphora can store lava, but pot block parity isn’t defined |

## Liquid tags (reference)

These tags are a good way to express “any container of X” in recipe docs (or to spot missing variants).

### `#materia:water`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/water.json`
- **Currently includes**: `materia:water_cup`

Note: this tag does **not** currently include vanilla water bottles, buckets, or pots.

If you want recipes to accept “any water container”, expanding this tag (or adding a new tag) is a good future cleanup.

### `#materia:milk`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/milk.json`
- **Includes**: `materia:milk_cup`, `materia:milk_bottle`

### `#materia:vinegar`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/vinegar.json`
- **Includes**: `materia:vinegar`, `materia:vinegar_bottle`

### `#materia:lime`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/lime.json`
- **Includes**: `materia:quicklime`, `materia:slaked_lime`

### `#materia:weak_acids` / `#materia:all_acids`

- **Weak acids tag JSON**: `shared/src/main/resources/data/materia/tags/items/weak_acids.json`
  - Includes: `#materia:vinegar`
- **All acids tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_acids.json`
  - Includes: `#materia:weak_acids` and `#materia:strong_acids`

### `#materia:ink_containers`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/ink_containers.json`
- **Includes**: `materia:ink_bottle`, `materia:ink_cup`

## Chemistry-adjacent liquid-ish tags

These show up in dye/chemistry recipes and can be treated like “liquid ingredient families”:

### `#materia:lipids`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/lipids.json`
- **Includes**: olive oil, animal fat

### `#materia:lubricants`

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/lubricants.json`
- **Includes**: `#materia:lipids`

### `#materia:lamp_oils`

Used by: recipes that accept “any lamp fuel”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/lamp_oils.json`
- **Includes**: `materia:olive_oil`, `materia:animal_fat`

Related:

- [Oil lamp (block)](../../content/blocks/oil-lamp.md)
- [Olive oil](../../content/items/olive-oil.md)

### `#materia:torch_fuels`

Used by: Materia’s **vanilla torch recipe override** (stick + fuel → 2× `minecraft:torch`).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/torch_fuels.json`
- **Includes**: `materia:animal_fat`, `materia:pitch`, `materia:tar`
- **Recipe**: `shared/src/main/resources/data/materia/datapacks/materia_vanilla_overrides/data/minecraft/recipes/torch.json` (and per-version copies on 1.18.2 / 1.19.2)

Related:

- [Animal fat](../../content/items/animal-fat.md)

### `#materia:lamp_wicks`

Used by: recipes that accept “any wick-like fiber”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/lamp_wicks.json`
- **Includes**:
  - `#materia:strings`
  - `materia:plant_fiber`

Related:

- [Oil lamp (block)](../../content/blocks/oil-lamp.md)

### `#materia:sealants` / `#materia:hot_sealants`

- **Sealants tag JSON**: `shared/src/main/resources/data/materia/tags/items/sealants.json`
  - Includes: pitch, tar, resin
- **Hot sealants tag JSON**: `shared/src/main/resources/data/materia/tags/items/hot_sealants.json`
  - Includes: `#materia:hot_sealants` plus honeycomb (note: the tag is self-referential)
