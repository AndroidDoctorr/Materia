## Power components (battery, dynamo, solenoid, wires)

Materia has a small “power components” family that currently exists mostly as **crafting components**.

The intent is to:

- gate some late-ish vanilla recipe overrides behind “industrial parts”
- leave room for future **integrations** (ex: letting a tech mod like Create handle the real power/logistics side)

As of today, these items are **not** a full standalone power system in Materia.

## The core parts

### Insulators and insulated wires

Insulators are defined by the tag:

- `#materia:insulators` (`shared/src/main/resources/data/materia/tags/items/insulators.json`)
  - pitch / resin / tar / rubber sheet

Insulated wire items:

- `materia:insulated_copper_wire`
- `materia:insulated_gold_wire`
- `materia:insulated_aluminum_wire`

They’re grouped as:

- `#materia:insulated_wires` (`shared/src/main/resources/data/materia/tags/items/insulated_wires.json`)

Crafting (pattern):

- wire item + `#materia:insulators` → insulated wire

### Voltaics (battery chain)

- **Voltaic layer**: `materia:voltaic_layer`
  - recipe: `shared/src/main/resources/data/materia/recipes/voltaic_layer.json`
  - built from copper plate + zinc plate + linen + weak acid
- **Voltaic pile**: `materia:voltaic_pile`
  - recipe: `shared/src/main/resources/data/materia/recipes/voltaic_pile.json`
  - 4× voltaic layer → voltaic pile
- **Battery**: `materia:battery`
  - recipe: `shared/src/main/resources/data/materia/recipes/battery.json`
  - voltaic layer + any plate + insulator + redstone → battery

### Solenoid

`materia:solenoid` is a “strong magnet” style component used in several redstone/logic and recipe override chains.

- recipe: `shared/src/main/resources/data/materia/recipes/solenoid.json`
  - magnetized rod + insulated copper wire + redstone → solenoid

### Dynamo and blower

- **Dynamo**: `materia:dynamo`
  - recipe: `shared/src/main/resources/data/materia/recipes/dynamo.json`
  - hard plates + hard rods + solenoid + insulated copper wire + battery → dynamo
- **Blower**: `materia:blower`
  - recipe: `shared/src/main/resources/data/materia/recipes/blower.json`
  - hard plates + dynamo → blower

## Where these show up (why players care)

These components are used by some **vanilla recipe overrides** (examples in `shared/src/main/resources/data/minecraft/recipes/`):

- `minecraft:stonecutter` (requires a dynamo + battery)
- `minecraft:grindstone` (requires a dynamo + battery)
- `minecraft:blast_furnace` (requires a blower)
- several redstone components use insulated wires and solenoids

## Integration note (future-looking)

If/when Materia adds integrations, these parts are good “anchors” to hook into other mods’ systems without trying to reinvent a full tech tree inside Materia.

