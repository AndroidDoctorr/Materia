## Anvil recipes (reference)

This is a compact reference for “what can I make on an anvil, and what tools/tier does it require?”.

Source note: derived from `1.18.2/src/reference/anvil_recipes.csv`.

## How to read this table

- **Anvil**: minimum anvil tier needed (Stone / Bronze / Iron)
- **Tool columns (Hammer / Tongs / Bore / Drawplate / Chisel)**:
  - `-` means “not required”
  - a material name (ex: **Bronze**) means “that tier or higher”

For how the tool *tags* map to actual items, see:

- [Anvil tool tags (cheat sheet)](anvil-tool-tags.md)

## Recipes

| Metal | Output | Input | Anvil | Hammer | Tongs | Bore | Drawplate | Chisel |
|---|---|---|---|---|---|---|---|---|
| Copper | Rod | Ingot | Stone | Stone | - | - | - | - |
| Copper | Plate | Ingot | Stone | Stone | - | - | - | - |
| Copper | Wire | Rod | Bronze | - | Bronze | - | Bronze | - |
| Copper | Ring (x9) | Wire | Iron | Bronze | - | Iron | - | Bronze |
| Bronze | Hammer head | Ingot | Stone | Stone | - | - | - | - |
| Bronze | Axe head | Ingot (x3) | Stone | Stone | - | - | - | - |
| Bronze | Hoe head | Ingot (x2) | Stone | Stone | - | - | - | - |
| Bronze | Rod | Ingot | Stone | Stone | - | - | - | - |
| Bronze | Blade | Rod | Stone | Stone | - | - | - | - |
| Bronze | Plate | Ingot | Stone | Bronze | - | - | - | - |
| Bronze | Anvil | Block (x3) | Stone | Bronze | - | - | - | - |
| Bronze | Block | Ingot (x9) | Stone | Bronze | - | - | - | - |
| Bronze | Chisel | Rod | Stone | Bronze | - | - | - | - |
| Bronze | Sword Blade | Ingot (x3) | Bronze | Bronze | - | - | - | - |
| Bronze | Pickaxe head | Rod (x3) | Bronze | Bronze | - | - | - | - |
| Bronze | Tongs | Rod (x2) | Bronze | Bronze | Wood | - | - | - |
| Bronze | Nuggets (x9) | Rod | Bronze | Bronze | - | - | - | Bronze |
| Bronze | Bore | Rod | Bronze | Bronze | Bronze | - | - | - |
| Bronze | Drawplate | Plate | Bronze | - | Bronze | Bronze | - | - |
| Bronze | Wire | Rod | Bronze | - | Bronze | - | Bronze | - |
| Bronze | Scale | Rivet | Bronze | Bronze | - | - | - | Bronze |
| Bronze | Ring (x9) or 7?? | Wire | Iron | Bronze | - | Iron | - | Bronze |
| Brass | Rod | Ingot | Stone | Stone | - | - | - | - |
| Brass | Plate | Ingot | Stone | Bronze | - | - | - | - |
| Brass | Wire | Rod | Bronze | - | Bronze | - | Bronze | - |
| Brass | Ring (x9) or 3?? | Wire | Bronze | Bronze | - | - | - | Bronze |
| Brass | Rivet (x18) | Wire | Bronze | Bronze | - | Bronze | - | Bronze |
| Brass | Hinge (x2) | Plate, Wire | Iron | Bronze | Bronze | Bronze | - | - |
| Brass | Latch (x2) | Plate, Wire | Iron | Bronze | Bronze | Bronze | - | - |
| Iron | Hammer head | Ingot | Bronze | Bronze | - | - | - | - |
| Iron | Axe head | Ingot (x3) | Bronze | Bronze | - | - | - | - |
| Iron | Hoe head | Ingot (x2) | Bronze | Bronze | - | - | - | - |
| Iron | Rod | Ingot | Bronze | Bronze | - | - | - | - |
| Iron | Blade | Rod | Bronze | Bronze | - | - | - | - |
| Iron | Plate | Ingot | Bronze | Iron | - | - | - | - |
| Iron | Anvil | Block (x3) | Bronze | Iron | - | - | - | - |
| Iron | Block | Ingot (x9) | Bronze | Iron | - | - | - | - |
| Iron | Chisel | Rod | Iron | Iron | - | - | - | - |
| Iron | Sword Blade | Ingot (x3) | Iron | Iron | - | - | - | - |
| Iron | Pickaxe head | Rod (x3) | Iron | Iron | - | - | - | - |
| Iron | Tongs | Rod (x2) | Iron | Iron | Bronze | - | - | - |
| Iron | Nuggets (x9) | Rod | Iron | Iron | - | - | - | Iron |
| Iron | Bore | Rod | Iron | Iron | Bronze | - | - | - |
| Iron | Drawplate | Plate | Iron | - | Iron | Iron | - | - |
| Iron | Wire | Rod | Iron | - | Iron | - | Iron | - |
| Iron | Scale | Rivet | Iron | Iron | - | - | - | Iron |
| Gold | Rod | Ingot | Bronze | Bronze | - | - | - | - |
| Gold | Plate | Ingot | Bronze | Bronze | - | - | - | - |
| Gold | Wire | Rod | Bronze | - | Bronze | - | Bronze | - |
| Gold | Ring (x9) or 1?? | Wire | Bronze | Bronze | - | - | - | Bronze |

## Notes / TODOs from the reference file

The CSV includes a few “??” quantities (rings), which likely deserve confirmation from the current recipe JSON:

- Bronze ring output count
- Brass ring output count
- Gold ring output count
