## Wrought iron fence, gate, and door

Decorative wrought iron building blocks forged on the **iron anvil** — fence rails, a fence gate, and a tall door with the same placement behavior as Materia wood doors.

## Obtaining

All three are shaped on the iron anvil with a **hammer** and **tongs** (hot inputs required):

| Block | Inputs |
| --- | --- |
| Wrought iron fence (`materia:wrought_iron_fence`) | 4× `materia:iron_rod` + 2× `materia:iron_band` |
| Wrought iron fence gate (`materia:wrought_iron_fence_gate`) | 3× `materia:iron_rod` + 3× `materia:iron_band` |
| Wrought iron door (`materia:wrought_iron_door`) | 8× `materia:iron_plate` + 4× `materia:iron_rivets` |

Recipe JSON:

- [Fence](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_fence_from_rod_band.json)
- [Fence gate](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_fence_gate_from_rod_band.json)
- [Door](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/wrought_iron_door_from_plate_rivets.json)

See also: [Iron anvil](iron-anvil.md)

## Behavior

- **Fence:** thin centered iron panel (2-pixel thick slab in the block space); each segment is independent — no vanilla fence connection logic.
- **Gate:** same closed panel as the fence; right-click (or redstone on placement) splits the panel open like shutters, hinged from the center.
- **Door:** two-block tall door; opens on right-click like wood doors.
