## Heat and fuel overview

This page is a “big picture” guide for **what stations can heat what**, and **what fuels / upgrades** you need to reach higher heat tiers.

Source note: the tier ranges and “what smelts where” matrix below is derived from `1.18.2/src/reference/smelting_rules.csv`. The *concept* is intended to stay stable across versions, but exact recipes/limits may evolve in newer branches.

## Heat tiers (stations and upgrades)

| Station / tier | Temperature range | Notes |
|---|---:|---|
| **Smoker** | < 500 | Safe for food and basic ceramics |
| **Campfire** | < 500 | Safe for food and basic ceramics |
| **Fire pit** | 300–600 | Early processing station. Some “smelting-like” outputs live here. |
| **Kiln** | 600–900 | Entry point for metalworking heat. Food burns to ash at this tier. |
| **Kiln + chimney** | 700–1100 | Higher heat tier for iron/quicklime and more complete melting. |
| **Kiln + chimney + bellows** | 900–1200 | High heat tier for steel and clear glass. |
| **Furnace kiln + chimney / blast-furnace kiln tier** | 1100+ | Highest tier (stone/glass/steel end of the scale). |

Related pages:

- [Kilns](kilns.md)
- [Fire pit](fire-pit.md)

## What cooks / smelts where (rules)

The table below is the “capability matrix” from the reference rules.

Key:

- **YES**: intended to work at that tier
- **NO**: cannot be processed at that tier
- **MAYBE**: undecided / experimental in the reference rules

### Food and cooking

- **Food**
  - Smoker / campfire: **YES**
  - Fire pit: **YES (NOT IMPLEMENTED)** in the reference rules
  - Kiln and hotter: **burns to ash**

### Ceramics

- **Ceramic bowl**: **YES** at all tiers (including smoker/campfire)
- **Other ceramics** (pots, amphorae, etc.): **Fire pit or hotter**

### “Fuel products” and processing

- **Charcoal**
  - Fire pit: **YES**
  - Kiln: **NO (ASH ONLY)** in the reference rules
- **Pitch / tar**
  - Fire pit: **YES**
  - Kiln+: **NO** in the reference rules
- **Rubber**: **MAYBE** at low tiers, **NO** at kiln+

### Metals and materials (by heat tier)

- **Tin / lead / copper / gold / aluminum**
  - Kiln: **YES (NUGGETS ONLY)** in the reference rules
  - Kiln + chimney and hotter: **YES**
- **Brass / bronze**
  - Kiln and hotter: **YES**
- **Zinc**
  - Kiln: **YES (NUGGETS ONLY)** in the reference rules
  - Kiln + chimney and hotter: **EVAPORATES** (watch your heat tier)
- **Quicklime**: requires **Kiln + chimney** or hotter
- **(Wrought) iron**: requires **Kiln + chimney** or hotter
- **Steel**: requires the highest tiers (see note below)
- **Clear glass**: requires **Kiln + chimney + bellows** or hotter
- **Stones**: require the **highest tier** (1100+)

## Fuel chain (what to burn, and where)

Fuel acceptance is station-specific, but the intent is:

- **Kilns**: accept a broad set of fuels (wood fuels, coal/charcoal, pitch/tar, etc.). **Coal coke** is supported and burns longer.
- **Fire pit**: accepts many early fuels (wood fuels, coal/charcoal, etc.), but **does not accept coal coke**.
- **Coke oven**: converts coal into coal coke, and only accepts **coal** as both input and fuel.

Related:

- [Coke oven](../content/blocks/coke-oven.md)
- [Coal coke](../content/items/coal-coke.md)

## Steel note (implementation vs reference)

The “tier table” above comes from `smelting_rules.csv`, but in the 1.18.2 implementation steelmaking is also gated by **which kiln variant** you’re using:

- Steel ingots are produced by an advanced kiln “steel” recipe that requires:
  - **coal coke fuel**
  - a **blast furnace kiln**, or a **furnace kiln** with a **furnace chimney**

See: [Steel ingot](../content/items/steel-ingot.md)

## Hot outputs and safety (tongs are “fuel tech” too)

Once you’re producing metal outputs, you’re also producing **hot items**.

- Hot metal burns you if it sits in your inventory.
- Use **tongs** to extract and move hot items.
- If you’re getting burned constantly: read [Hot metals](hot-metals.md) and use [Tongs](../content/items/tongs.md).

## How this ties into anvils

Anvils only work on **hot** inputs:

- The kiln/furnace tier you can reach determines which metals you can actually **shape** on an anvil.
- If recipes aren’t showing up, it’s usually because the input isn’t hot (or you’re at the wrong anvil tier).

See: [Anvils](anvils.md)
