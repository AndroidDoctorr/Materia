## Screenshot checklist (high value)

This is a “grab these screenshots when you’re playing” list. These are the places where **a screenshot explains more than text**.

Guidelines:

- Store screenshots under `documentation/assets/screenshots/`
- Use consistent names like `mechanic_step_001.png` or `block_context_001.png`
- Reference: [Docs style guide](STYLE.md)

## Early game (paleolithic/neolithic)

- [ ] Hewing in action (log + basic axe → rough planks) with the tool being used
- [ ] Knapping UI / process (show hammer stone + flint → knapped flint)
- [ ] Primitive crafting table UI (show the 3×2 grid size clearly)

## Heat and safety

- [ ] Hot metal warning in hotbar/inventory (player taking damage or warning message)
- [ ] Tongs containing a hot item (hot overlay/icon/state if visible)
- [ ] Quenching with tongs in a water pot (before/after cooled)

## Water pot (container equivalencies)

- [ ] Water pot water levels (0–3) in-world (side-by-side or labeled)
- [ ] Transfer examples:
  - [ ] glass bottle ↔ water pot
  - [ ] bucket ↔ water pot
  - [ ] cauldron ↔ water pot (sneak-transfer)
  - [ ] crucible ↔ water pot (water cup conversion)
- [ ] Boiling state (campfire under pot; boiling particles visible)
- [ ] One boiling recipe example (bone → glue), showing input and output

## Amphora (storage + liquids + fermentation)

- [ ] Amphora solid storage UI (6 slots layout)
- [ ] Amphora liquid state visible in-world (liquid type indicator)
- [ ] Liquid transfer examples:
  - [ ] bottle/cup transfer (1 unit)
  - [ ] pot/bucket transfer (3 units)
- [ ] Lid vs sealed lid (what it looks like when closed)
- [ ] Fermentation in progress (particles) and finished result:
  - [ ] grape juice → vinegar (lid)
  - [ ] grape juice → wine (sealed lid)

## Storage and textiles

- [ ] Sack UI (4 slots) + tooltip showing “Contains: X items”
- [ ] Basket open/close state (blockstate `open` true/false)

## Kilns and anvils (forging loop)

- [ ] Basic kiln UI (inputs, fuel, output)
- [ ] “Hot output” moment (why tongs matter)
- [ ] Each anvil tier UI (stone/bronze/iron) showing different slot layouts
- [ ] Example anvil recipe with tool slots filled (one screenshot per tier)

## Dyes (high-signal chains)

- [ ] Indigo source plant in-world (what to look for)
- [ ] Lavender from grapes (the chain’s “entry point” screenshot)
- [ ] Tyrian purple chain (murex → gland → boil → iota/blob/dye) – even one “step” screenshot helps

## Future content buckets (when you start documenting them)

- [ ] Cannons: loading + firing (safety/aiming)
- [ ] Gunpowder trails: placement + ignition + propagation
- [ ] Logic blocks: one “wiring example” screenshot per major gate family
