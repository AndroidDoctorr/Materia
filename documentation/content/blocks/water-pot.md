## Water pot

<img src="../../../shared/src/main/resources/assets/materia/textures/item/water_pot.png" alt="Water pot" width="64" height="64">

The water pot stores water (0–3) and can boil when placed above a lit campfire.

It follows the core container sizing rule:

- **1 level** = 1 cup/bottle worth
- **3 levels** = 1 bucket/pot worth

See: [Liquids and containers (reference)](../../reference/tags/liquids-and-containers.md)

## Obtaining

You typically create a water pot by filling an empty [Pot](pot.md) in-world:

- 1× [Water cup](../items/water-cup.md) (**+1 level**)
- 1× vanilla **water bottle** (**+1 level**)
- 1× vanilla **water bucket** (**set to full / +3 levels**)
- Sneak-transfer with a **cauldron above** (moves up to 3 levels)

If you place a water pot block as an item, it starts full (level 3).

## Common uses

- Turn crucible into water cup (and back)
- Fill/empty with bottles and buckets
- Boil certain inputs into outputs (glue, hardened leather, etc.)
- Quench hot metal items held in tongs

## Vanilla-compatible transfers (water)

These transfers are designed to feel like vanilla container interactions:

- **Crucible ⇄ water cup**
  - Use a [Crucible](../items/crucible.md) on a water pot to get a [Water cup](../items/water-cup.md) (**-1 level**)
  - Use a water cup on a water pot to pour it back (**+1 level**, returns crucible)
- **Glass bottle ⇄ water bottle**
  - Glass bottle takes 1 level and becomes a vanilla water bottle
  - Vanilla water bottle adds 1 level and becomes a glass bottle
- **Bucket ⇄ water bucket**
  - Empty bucket takes all 3 levels (only when full)
  - Water bucket fills to full (3 levels)
- **Cauldron ⇄ water pot** (sneak-transfer)
  - Transfers 1–3 levels between a cauldron above the pot and the pot itself

## Boiling

The water pot boils when placed above a **lit campfire**.

While boiling, it can process certain inputs into outputs. See: [Water pot (mechanic)](../../mechanics/water-pot.md)

## Boiling recipes

The water pot uses a 1-slot inventory for simple processing:

- Bone → Glue (requires boiling)
- Tanned leather → Hardened leather (requires boiling)
- Murex glands → boiled versions (requires boiling)
- Earth blocks (2) → clay balls + dirt (does not require boiling)
  - Accepts `#materia:earth_blocks` (Earth in 1.18.2; packed mud in newer versions)
- Paper mixture → paper pulp (does not require boiling)

## Quenching (tongs)

If you right-click a water pot with **tongs containing hot metal**, the water pot can quench/cool the items.

Related:

- [Water pot (mechanic)](../../mechanics/water-pot.md)
- [Pot (empty)](pot.md)
- [Crucible](../items/crucible.md)
- [Water cup](../items/water-cup.md)
- [Tongs](../items/tongs.md)
- [Hot metals](../../mechanics/hot-metals.md)
