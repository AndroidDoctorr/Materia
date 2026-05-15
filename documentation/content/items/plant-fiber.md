## Plant fiber

<img src="../../../shared/src/main/resources/assets/materia/textures/item/plant_fiber.png" alt="Plant fiber" width="64" height="64">

Plant fiber is a basic early-game material used for bindings (like lashing).

## Getting plant fiber

- From sugar cane:
  - [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/plant_fiber_from_sugarcane.json)
- From grass and ferns:
  - small chance to drop plant fiber when broken normally (no shears / no silk touch)
- From leaves:
  - small chance to drop plant fiber when broken normally (no shears / no silk touch)
- From stripping logs:
  - stripping a log with an axe drops 2× plant fiber

## Used for

- [Lashing](lashing.md)
- Bundle, paper mixture, lamp wicks (any recipe that asks for `#forge:fibers`)

## Mod compatibility

Materia's core fiber recipes (lashing, bundle, paper mixture) consume the
`#forge:fibers` tag rather than `materia:plant_fiber` directly, so any other
mod's fiber item that joins that tag is a drop-in equivalent.

- **Immersive Engineering** — `immersiveengineering:hemp_fiber` is bridged
  into `#forge:fibers` (and `#forge:fiber`) automatically. Industrial hemp is
  effectively the same as plant fiber for Materia. No optional datapack or
  config toggle is needed; the bridge is always-on but the entry is marked
  `required: false`, so it does nothing if IE is not installed.
- **Hemp → string** is intentionally **not** added to Materia's compat
  datapack: IE already ships a `4× hemp_fiber → 1× string` crafting recipe,
  and `minecraft:string` is already in `#forge:strings`, which Materia accepts
  via `#materia:strings`. Adding a Materia path would just duplicate IE's
  conversion (or fight it for `taupe_string`).
