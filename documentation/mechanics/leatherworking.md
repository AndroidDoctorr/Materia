## Leatherworking (tanning + hardening)

Materia splits “leather progression” into a few steps used by armor, horse gear, and some crafting components:

- **Leather** (vanilla) → **tanned leather** (drying rack)
- **Tanned leather** → **hardened leather** (boiling in a water pot)
- **Tanned leather** + cutting tools → **leather straps** (crafting)
- **Tanned/hardened leather** + finishes → leather components (used by some recipes)

Source of truth (1.18.2):

- Drying rack interaction: `1.18.2/src/main/java/com/torr/materia/DryingRackBlock.java`
- Drying rack timing: `1.18.2/src/main/java/com/torr/materia/blockentity/DryingRackBlockEntity.java`
- Hardening (water pot boiling recipe): `1.18.2/src/main/java/com/torr/materia/blockentity/WaterPotBlockEntity.java`

## Tanning (drying rack)

### Make a drying rack

- Recipe: `shared/src/main/resources/data/materia/recipes/drying_rack.json`
- Uses: `#materia:basic_bindings` (tag reference: [Bindings and adhesives](../reference/tags/bindings-and-adhesives.md))

Block page:

- [Drying rack](../content/blocks/drying-rack.md)

### Use a drying rack (leather mode)

1. Place a drying rack.
2. Place a **lit campfire adjacent** to the rack (one block away on a side).
3. Right-click the rack with **vanilla leather** (`minecraft:leather`).

What happens:

- The rack stores an internal “stretched” form (`materia:leather_stretched`).
- When a lit campfire is adjacent, it starts drying.
- After **600 ticks** (30 seconds), `leather_stretched` becomes `materia:tanned_leather_stretched`.
- Right-click to take the item back:
  - `tanned_leather_stretched` is returned to the player as **`materia:tanned_leather`**
  - If you remove it early, you get regular `minecraft:leather` back instead.

Important limitation:

- A rack can be used for **either** leather **or** meat at a time (it won’t accept meat if leather is loaded, and vice versa).

## Hardening (boiling in a water pot)

Once you have `materia:tanned_leather`, you can harden it:

1. Put a [Water pot](water-pot.md) above a **lit campfire**.
2. Insert `materia:tanned_leather` into the water pot’s 1-slot inventory.
3. After **160 ticks** (8 seconds), it outputs `materia:hardened_leather`.

Note:

- This recipe is “in-place” code logic, not a datapack recipe JSON.

## Leather straps (crafting)

Leather straps are cut from tanned leather:

- `shared/src/main/resources/data/materia/recipes/leather_strap.json`
- Ingredients:
  - `materia:tanned_leather`
  - `#materia:all_cutting_tools`

## Leather finishes

Some leather component recipes take a “finish” ingredient:

- [`#materia:leather_finishes`](../reference/tags/bindings-and-adhesives.md#materialeather_finishes): `shared/src/main/resources/data/materia/tags/items/leather_finishes.json`

In `shared`, this includes:

- `materia:animal_fat`
- `minecraft:honeycomb`
- `materia:olive_oil`

## Where these show up

- [Saddle and horse armor](../content/items/horse-gear.md)
- Leather components:
  - `shared/src/main/resources/data/materia/recipes/leather_chestpiece.json`
  - `shared/src/main/resources/data/materia/recipes/leather_backpiece.json`
  - `shared/src/main/resources/data/materia/recipes/leather_shoulder.json`
  - `shared/src/main/resources/data/materia/recipes/leather_boot.json`

## Related

- Items:
  - [Tanned leather](../content/items/tanned-leather.md)
  - [Hardened leather](../content/items/hardened-leather.md)
  - [Leather strap](../content/items/leather-strap.md)
- Mechanics: [Animal drops](animal-drops.md) (animal fat)

