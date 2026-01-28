## Amphora and liquids

Amphorae can store **either solids or liquids**.

## Storage modes

- **Solid storage**: 6 slots (3 rows × 2 columns)
- **Liquid storage**: up to 9 “units”

Liquid units are measured as:

- 1 unit = 1 bottle/cup worth
- 3 units = 1 bucket/pot worth
- 9 units = 3 buckets

An amphora can’t mix liquid types, and can’t store solids at the same time as liquids.

## Liquid types

Supported liquid types include:

- **Water**
  - Common containers: [Water cup](../content/items/water-cup.md), [Water pot](../content/blocks/water-pot.md)
- **Grape juice**
  - [Grape juice](../content/items/grape-juice.md)
- **Olive oil**
  - [Olive oil](../content/items/olive-oil.md)
- **Vinegar**
  - [Vinegar](../content/items/vinegar.md)
- **Wine**
  - [Wine cup](../content/items/wine-cup.md)
- **Beer**
  - Common containers: beer cup/bottle and beer bucket/pot (see: [Hops and beer](hops-and-beer.md))
- **Milk**
  - Common containers: [Milk cup](../content/items/milk-cup.md), [Milk bottle](../content/items/milk-bottle.md), [Milk pot](../content/blocks/milk-pot.md)
- **Lava**
  - Stored via amphora (container parity for lava is not fully documented yet)

## Filling and extracting

Amphora liquid transfer supports multiple container sizes:

- **Bottle/cup**: transfers 1 unit
- **Pot/bucket**: transfers 3 units

Common containers you’ll use:

- **Cups/crucibles (1 unit)**:
  - [Crucible](../content/items/crucible.md)
  - [Water cup](../content/items/water-cup.md)
  - [Milk cup](../content/items/milk-cup.md)
- **Pots (3 units)**:
  - [Pot (empty)](../content/blocks/pot.md)
  - [Water pot](../content/blocks/water-pot.md)
  - [Milk pot](../content/blocks/milk-pot.md)

See also: [Liquids and containers (reference)](../reference/tags/liquids-and-containers.md)

## Lids and fermentation

Amphorae support lids:

- **Lid**: starts fermentation of grape juice into vinegar (about 20 minutes)
- **Sealed lid**: starts fermentation of grape juice into wine (about 60 minutes)

### Fermentation mechanic (grape juice → vinegar or wine)

Fermentation is a **time-based conversion** that happens inside an amphora.

- **Requirements**
  - The amphora must contain **grape juice** as its stored liquid.
  - You must add a **lid** (vinegar) or a **sealed lid** (wine).
- **Result**
  - When fermentation completes, the amphora’s liquid type changes:
    - **Lid** → `vinegar`
    - **Sealed lid** → `wine`
  - **Amount stays the same** (only the liquid type changes).
- **Timing**
  - Vinegar: \(24000\) ticks ≈ **20 minutes**
  - Wine: \(72000\) ticks ≈ **60 minutes**
- **Interaction rule**
  - While fermenting, the amphora **does not accept interactions** until the process completes.

Related:

- [Amphora (block)](../content/blocks/amphora.md)

### Fermentation mechanic (beer mash → beer)

Beer uses a staged recipe before fermentation:

- **Requirements**
  - The amphora must contain **water**.
  - You must add **4× wheat** and **1× hops** (any order).
  - The amphora will **not accept a lid** until the mash is complete.
- **Start**
  - After the mash is complete, add a **lid** to begin fermentation.
- **Result**
  - The amphora’s liquid becomes **beer** when fermentation finishes.
  - **Beer mash is not extractable** (one-way only).

See: [Hops and beer](hops-and-beer.md)
