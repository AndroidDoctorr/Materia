## Wrought iron fence, gate, door, bracket, and grate

Decorative wrought iron building blocks forged on the **iron anvil** (or **bronze anvil** for the grate) — fence rails, a fence gate, a tall door, a wall bracket, and a thin floor/wall grate.

## Obtaining

| Block | Inputs | Anvil |
| --- | --- | --- |
| Wrought iron fence (`materia:wrought_iron_fence`) | 4× `materia:iron_rod` + 2× `materia:iron_band` | Iron |
| Wrought iron fence gate (`materia:wrought_iron_fence_gate`) | 3× `materia:iron_rod` + 3× `materia:iron_band` | Iron |
| Wrought iron door (`materia:wrought_iron_door`) | 8× `materia:iron_plate` + 4× `materia:iron_rivets` | Iron |
| Wrought iron bracket (`materia:wrought_iron_bracket`) | 1× `materia:iron_band` + 1× `materia:iron_wire` | Iron |
| Wrought iron grate (`materia:wrought_iron_grate`) | 4× `materia:iron_wire` | Iron or bronze |

All recipes require **hammer** and **tongs** in tool slots (`#materia:iron_hammers`, `#materia:iron_tongs`; bronze hammer also works on the bronze anvil grate recipe).

Recipe JSON:

- [Fence](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_fence_from_rod_band.json)
- [Fence gate](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_fence_gate_from_rod_band.json)
- [Door](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_door_from_plate_rivets.json)
- [Bracket](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_bracket_from_band_wire.json)
- [Grate (iron anvil)](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_grate_from_wire.json)
- [Grate (bronze anvil)](../../../shared/src/main/resources/data/materia/recipes/bronze_anvil/wrought_iron_grate_from_wire.json)

See also: [Iron anvil](iron-anvil.md)

## Behavior

- **Fence:** thin centered iron panel (2-pixel thick slab in the block space). **Connects to adjacent wrought iron fences and fence gates** on N/S/E/W. Connection shape is computed automatically:
  - **Isolated** segment: single panel oriented by **facing** (N/S or E/W)
  - **Straight run** (one axis only): single full panel along that axis — no extra post or arms
  - **Corner / T / cross**: center post + side arms on connected faces only
  - Neighbors refresh when you place or break a segment
- **Jump height:** matches vanilla wood fences — **`getCollisionShape` is 1.5 blocks tall** (24 px) so players and mobs cannot jump over, but **`getShape` / `getVisualShape` stay 1 block tall** (16 px) so the crosshair outline matches the rendered model (same split as vanilla `CrossCollisionBlock`).
- **Water:** fences and gates are **not** washed away by flowing water.
- **Gate:** same closed panel as the fence; right-click (or redstone on placement) splits the panel open like shutters, hinged from the center. Fences connect to gates on adjacent faces. Open collision matches the rendered swing direction for all facings; closed gate uses the same 1-block outline / 1.5-block collision split as the fence.
- **Door:** two-block tall door; opens on right-click like wood doors.
- **Bracket:** wall-mounted wrought iron console (same placement rules as stone brackets).
- **Grate:** always a vertical panel (1 px render, 2 px collision); click any face of a block to mount on that side; N/S vs E/W orientation follows your look direction when placing. Cutout render layer; angled block item icon.

## Related

- [Iron anvil](iron-anvil.md)
- [Stone columns, capitals, cornices & brackets](stone-trim.md) — stone brackets (wrought iron bracket uses same wall rules)
