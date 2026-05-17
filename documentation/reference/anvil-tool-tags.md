## Anvil tool tags (cheat sheet)

Anvil recipes refer to tools by **item tags** (strings like `materia:iron_hammers`).
This page tells you what those tags actually mean in practice.

## Hammers

- `materia:basic_hammers`
  - `materia:hammer_stone`
  - `materia:stone_hammer`

- `materia:stone_hammers`
  - `#materia:basic_hammers`
  - `#materia:bronze_hammers`

- `materia:bronze_hammers`
  - `materia:bronze_hammer`
  - `#materia:iron_hammers`

- `materia:iron_hammers`
  - `materia:iron_hammer`
  - `#materia:steel_hammers`

- `materia:steel_hammers`
  - `materia:steel_hammer`

- `materia:all_hammers`
  - `#materia:stone_hammers`

Practical takeaway:

- **Any “higher tier” hammer generally counts as the lower tiers** (because the tags nest upward).

## Chisels

- `materia:bronze_chisels`
  - `materia:bronze_chisel`
  - `#materia:iron_chisels`

- `materia:iron_chisels`
  - `materia:iron_chisel`

- `materia:all_chisels`
  - `#materia:bronze_chisels`

## Bores

- `materia:iron_bores`
  - `materia:iron_bore`

- `materia:bronze_bores`
  - `materia:bronze_bore`
  - `#materia:iron_bores`

- `materia:all_bores`
  - `#materia:bronze_bores`

## Tongs

- `materia:steel_tongs`
  - `materia:steel_tongs`

- `materia:iron_tongs`
  - `materia:iron_tongs`
  - `#materia:steel_tongs`

- `materia:bronze_tongs`
  - `materia:bronze_tongs`
  - `#materia:iron_tongs`

- `materia:all_tongs`
  - `#materia:wood_tongs`

## Where these show up

Look for these fields in anvil recipe JSON:

- Stone anvil: `"tool_tag": "materia:..."`
- Bronze/Iron anvil: `"tool_tags": ["materia:...", "...", ...]`
