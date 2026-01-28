## Mux (2→1)

<img src="../../../shared/src/main/resources/assets/materia/textures/block/mux_0_0.png" alt="Mux (top texture)" width="64" height="64">

The mux selects **one of two input signals** and forwards it to a single output.

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/block/MuxBlock.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/mux.json`

## IO mapping

Top-down view (relative to facing):

```text
          output (front)
                |
 left input  [  MUX  ]  right input
                |
          select (back)
```

- **Left input**: left side
- **Right input**: right side
- **Select**: back
- **Output**: front

## Behavior

Select chooses which channel is forwarded:

- If **select is OFF (0)** → output = **left input**
- If **select is ON (1)** → output = **right input**

Notes:

- Output is binary (0 or 15).
- Designed to chain directly into other logic blocks (strong + weak output power).

## Screenshot / GIF ideas

- Two levers on left/right inputs + one lever on select, lamp on output.

