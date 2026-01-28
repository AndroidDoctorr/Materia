## Metalworking (bronze and beyond)

This page explains the “metalworking grammar” used throughout Materia: most metal parts are made by repeatedly shaping **hot** metal into a small set of standard forms (plates, rods, wires, blades, bands, etc.). The *pattern* repeats across metals; the limiting factor is usually the **anvil tier** and **tool-tag tier** required.

If you ever feel stuck, open JEI and search the part name (ex: “wire”, “rod”, “drawplate”) and check which anvil category it appears under.

Related foundation pages:

- [Kilns](kilns.md) (heat source)
- [Hot metals](hot-metals.md) (why items must be hot, and why tongs matter)
- [Anvils](anvils.md) (slot rules + tool tags + hot-input requirement)
- [Tongs](../content/items/tongs.md)

## The basic shapes (parts you keep remaking)

In general, many higher-level crafts boil down to having these on hand:

- **Ingots / nuggets** (starting metal)
- **Plates** (flat stock; used for saws, bands, scales, gears, etc.)
- **Rods** (long stock; used to make handles, poles, blades, wire)
- **Wire** (fasteners, needles, nails, rings, and logic/insulated-wire chains)

Where these come from depends on the metal, but the “verbs” are the same.

## Tier gating: “stronger tools count as weaker tools”

Most anvil recipes use **item tags** instead of fixed items, and the tags are intentionally tiered so stronger tools satisfy weaker requirements.

For example:

- `#materia:bronze_tongs` includes `#materia:iron_tongs`
  - Tag JSON: `shared/src/main/resources/data/materia/tags/items/bronze_tongs.json`
- `#materia:bronze_drawplates` includes `#materia:iron_drawplates`
  - Tag JSON: `shared/src/main/resources/data/materia/tags/items/bronze_drawplates.json`
- `#materia:bronze_chisels` includes `#materia:iron_chisels`
  - Tag JSON: `shared/src/main/resources/data/materia/tags/items/bronze_chisels.json`

Practical takeaway:

- If a recipe says it needs a “bronze” tool tag, an “iron” (or better) tool will usually work too.

## Example loop: wire

Wire is the cleanest example of the “same recipe across metals” idea.

### Bronze wire (bronze anvil)

- `materia:bronze_rod` → `materia:bronze_wire`
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_wire_from_rod.json`
  - Tools: `#materia:bronze_tongs` + `#materia:bronze_drawplates`

To get there, you also need:

- Plate from ingot:
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_plate_from_ingot.json`
- Drawplate from plate:
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_drawplate_from_plate.json`
- Rod from ingot:
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_rod_from_ingot.json`

### Iron wire (iron anvil)

- `materia:iron_rod` → `materia:iron_wire`
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_wire_from_rod.json`
  - Tools (3 slots): `#materia:iron_tongs` + `#materia:iron_drawplates` + `#materia:iron_tongs`

This looks “stricter” than bronze wire because iron-anvil recipes support 3 tool slots (and sometimes 2 inputs). Conceptually it’s still the same action: **draw wire from a rod** using **tongs + drawplate**.

## Example loop: saw parts (bands / blades)

This pattern shows how “plate + chisel + hammer” becomes cutting tools.

- Bronze saw band:
  - `materia:bronze_plate` → `materia:bronze_saw_band`
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_saw_band_from_plate.json`
  - Tools: `#materia:bronze_hammers` + `#materia:bronze_chisels`
- Iron sawblade:
  - `materia:iron_plate` → `materia:iron_sawblade`
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_sawblade_from_plate.json`
  - Tools (3 slots): `#materia:iron_hammers` + `#materia:iron_hammers` + `#materia:iron_chisels`

## Example loop: nails

Small fasteners are typically “wire → many small outputs”.

- `materia:iron_wire` → 16× `materia:iron_nails`
  - Bronze anvil: `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_nails_from_wire.json`
  - Iron anvil: `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_nails_from_wire.json`

Same output; different station tier and tool-slot layout.

## A practical Bronze Age “starter kit”

Once you have bronze ingots and a bronze anvil, the usual “metalworking unlock kit” is:

- **Bronze hammer** (primary shaping tool): [Bronze hammer](../content/items/bronze-hammer.md)
- **Bronze tongs** (handling + forging): [Tongs](../content/items/tongs.md)
- **Bronze chisel** (turn plates into bands/blades): [Bronze chisel](../content/items/bronze-chisel.md)
- **Bronze drawplate** (turn rods into wire): [Bronze drawplate](../content/items/bronze-drawplate.md)

With those, most bronze-era “component chains” become straightforward: you can make plates/rods, then bands/wire, then saw/shears/needles/nails/rivets/etc.

