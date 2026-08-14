## Heat and hot items

## `#materia:heatable_metals`

Used by: the hot-metal damage system (items in this tag can become hot and hurt the player when handled/kept in inventory).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/heatable_metals.json`
- **Includes**: many metal items and metal components (ingots/nuggets/plates/rods/wires/etc.), plus a few related items like rivets and blocks.

Related docs:

- [Hot metals](../../mechanics/hot-metals.md)
- [Tongs](../../content/items/tongs.md)

## `#materia:all_plates`

Used by: crafting recipes that accept “any plate” (including several logic-block recipes).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_plates.json`
- **Includes**:
  - `#materia:hard_plates` (bronze / wrought iron / refined iron plates)
  - `#materia:soft_plates` (copper/aluminum/gold/tin/brass/zinc/lead plates)

Related docs:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

## `#materia:insulated_wires`

Used by: logic-block crafting recipes that require insulated wiring.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/insulated_wires.json`
- **Includes**:
  - `materia:insulated_copper_wire`
  - `materia:insulated_gold_wire`
  - `materia:insulated_aluminum_wire`

Related docs:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)
