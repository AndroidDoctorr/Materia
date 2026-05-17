## Mod compatibility tag bridges

This page documents where Torr’s Mod deliberately **aligns with Forge tags** so other mods’ items can be used in Torr’s recipes (and so other mods can recognize Torr’s items).

### Design rule (important)

- In general, **recipes keep using `#materia:*` tags**, but the tag contents are set up so that `#materia:*` tags often **include `#forge:*` tags**.
- This means we can improve compatibility without rewriting lots of recipe JSONs.

### Common “ingredient economy” tags

- **Strings**
  - `#materia:strings` includes `#forge:strings`
  - Forge tags provided: `#forge:strings`, `#forge:string`
- **Seeds**
  - `#materia:seeds` includes `#forge:seeds`
- **Flour**
  - Forge tags provided: `#forge:flour`, `#forge:flours`
- **Dough**
  - Forge tags provided: `#forge:dough`, `#forge:doughs`
- **Salt**
  - Forge tags provided: `#forge:salt`, `#forge:salts`
- **Milk**
  - `#materia:milk` includes `#forge:milk`
  - Forge tags provided: `#forge:milk`
- **Oil / fat**
  - `#materia:lamp_oils`, `#materia:lipids`, `#materia:lubricants` include Forge tags
  - Forge tags provided: `#forge:oil`, `#forge:oils`, `#forge:fat`, `#forge:fats`

### Tool tags

These are primarily for “tool-gated crafting” and cross-mod tool compatibility:

- `#materia:all_knives` includes `#forge:tools/knives`
- `#materia:all_axes` includes `#forge:tools/axes`
- `#materia:all_hammers` includes `#forge:tools/hammers`
- `#materia:all_saws` includes `#forge:tools/saws`
- `#materia:all_needles` includes `#forge:tools/needles`
- `#materia:all_picks` includes `#forge:tools/pickaxes`
- `#materia:all_tongs` includes `#forge:tools/tongs`

### Dyes

Forge dye tags are provided under `#forge:dyes/*`, including Torr’s extended palette (olive/indigo/charcoal_gray/taupe/tyrian_purple/lavender/ochre/red_ochre).

### Intentional non-interop areas

- **Plant fibers**: intentionally not generalized to cross-mod fibers (to avoid unintended recipe mixing).

