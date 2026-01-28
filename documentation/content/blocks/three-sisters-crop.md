## Three Sisters crop

The Three Sisters crop is a special farmland crop block that lets you plant **corn**, **beans**, and **squash** together on a single tile.

Core idea:

- you can “stack” up to **three crops** into the same block
- each crop grows and harvests independently

Mechanics overview:

- [Crops and farming (mechanics)](../../mechanics/crops.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/ThreeSistersBlock.java`
- `1.18.2/src/main/java/com/torr/materia/ThreeSistersCornUpperBlock.java`
- `1.18.2/src/main/java/com/torr/materia/events/CropRightClickHarvestHandler.java`

## Planting

Block id:

- `materia:three_sisters_crop`

You plant into the Three Sisters crop in two common ways:

### 1) Plant normally on farmland (auto-creates the block)

Corn, beans, and squash seeds use a shared “smart seed” item class (`ThreeSistersSeedItem`).

If you use the seed on farmland:

- if the space above is air, it places a `materia:three_sisters_crop` block
- then it plants the specific crop into it (at age 1)

### 2) Add more crops into an existing Three Sisters block

If you right-click an existing Three Sisters crop block while holding:

- `materia:corn`
- `materia:beans`
- `materia:squash_seeds`

…it will plant that crop into the shared tile, if that crop isn’t already present.

## Growth

Internally, the block tracks three independent age properties:

- `corn_age` (0–7)
- `beans_age` (0–7)
- `squash_age` (0–7)

Rules:

- Each crop grows independently.
- A crop only “exists” if its age is at least 1.

### Tall corn

When corn reaches age **4+**, the block spawns an upper “visual” corn block above it:

- `materia:three_sisters_corn_upper`

This upper block:

- has no collision (walk through it like crops)
- does not drop loot (the base block controls drops)
- is automatically removed if corn drops below the tall threshold (mostly relevant for harvest resets)

## Harvesting (drops and right-click QoL)

### Drops (loot table)

Drops are defined in:

- `shared/src/main/resources/data/materia/loot_tables/blocks/three_sisters_crop.json`

At a glance:

- If a crop is present (age 1–7), you always get its seed back.
- At maturity (age 7), you also get the harvest item(s):
  - corn: `materia:corn_cob`
  - beans: extra `materia:beans`
  - squash: `materia:squash` (and sometimes extra seeds)

### Right-click harvest with replant

Right-click harvesting is supported (server-side):

- each mature sub-crop (age 7) is harvested
- that sub-crop’s age is reset to **1**
- the other sub-crops are left alone

If you right-click the **upper corn block**, the handler redirects to the base block below so harvesting still works naturally.

## Screenshot / GIF ideas

- One farmland tile hosting all three crops at once, showing:
  - planting them in sequence
  - corn becoming tall at age 4+
  - right-click harvesting resetting each sub-crop independently

