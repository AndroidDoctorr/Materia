## Water pot

The water pot is a utility block for boiling and water handling.

## Boiling

The water pot boils when placed above a lit campfire.

## Water level

Water level ranges from **0 (empty)** to **3 (full)**.

### Automation (pipes / hoppers)

Forge **`IFluidHandler`** (water only) and a **one-slot** item handler are available for pipes and item logistics; level maps to tank contents as in [Fluid and pipe compatibility](../fluid-pipe-compat.md).

You can move water in/out using:

- water cups / crucibles
- glass bottles (water bottles)
- buckets
- cauldrons (sneak-right-click to transfer)

## Boiling recipes

The water pot has simple in-place recipes using its 1-slot inventory:

- Bone → Glue (requires boiling)
- Tanned leather → Hardened leather (requires boiling)
- Murex glands → boiled versions (require boiling)
- Earth blocks (2) → clay balls + dirt (does not require boiling)
  - Accepts `#materia:earth_blocks` (Earth in 1.18.2; packed mud in newer versions)
- Paper mixture → paper pulp (does not require boiling)
- Tea leaves → tea pot in place (does not require boiling; water level is preserved)
  - Use a **crucible** on the tea pot to draw a tea cup (same as wine/beer pots)

Some recipes may change the block (e.g. earth separation replaces the water pot with an empty pot).

See also:

- [Earth (and packed mud)](earth-and-packed-mud.md)

## Quenching (hot metals)

**Heatable metals** tagged **`#materia:heatable_metals`** that are warm (heated or cooling) snap to fully cold stacks when:

- **`ItemEntity`** drops are **under vanilla water**
- **`#materia:heatable_metals`** stacks sit in the water pot **item slot** while the pot **has water** (not necessarily boiling)

(This uses the same “strip hot capability” path as gradual cooling.)

If you **right-click a water pot with tongs holding hot metal**, the existing tong-focused quench path still applies—see **[Tongs](../content/items/tongs.md)**.

Related:

- [Crucible](../content/items/crucible.md)
- [Water cup](../content/items/water-cup.md)
- [Pot (empty)](../content/blocks/pot.md)
- [Tongs](../content/items/tongs.md)
- [Hot metals](hot-metals.md)
