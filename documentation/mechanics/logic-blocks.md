## Logic blocks

Materia includes a set of compact “logic blocks” designed to be chained together into dense redstone-like circuits (adders, counters, latches, etc.).

This page documents the **common conventions** shared by the whole family. Individual block pages cover exact IO and behavior.

Source of truth for behavior in the stable version:

- `1.18.2/src/main/java/com/torr/materia/block/LogicGateBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/NotGateBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/RsLatchBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/TFlopBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/MuxBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/CounterBlock.java`
- `1.18.2/src/main/java/com/torr/materia/block/TimerBlock.java`
- `1.18.2/src/main/java/com/torr/materia/blockentity/TimerBlockEntity.java`
- `1.18.2/src/main/java/com/torr/materia/block/HalfAdderBlock.java`

Related block pages:

- [Logic blocks (blocks)](../content/blocks/logic.md)

## Common behavior (all logic blocks)

### Shape / placement

- They are **thin** blocks (2 pixels tall), so they behave more like a “plate” than a full cube.
- When placed, they generally set `facing = context.getHorizontalDirection()`, meaning they “point” **the direction you were looking** while placing.

### IO is directional (front / back / left / right)

Every block defines IO relative to its `facing`.

Terminology used throughout the docs:

- **Front**: the direction the block is facing (`facing`)
- **Back**: opposite of facing (`facing.getOpposite()`)
- **Left**: `facing.getCounterClockWise()`
- **Right**: `facing.getClockWise()`

Most blocks are designed so you can wire them **directly adjacent** (no repeaters) and still get clean behavior.

### Boolean signals (0 or 15)

Most logic blocks treat “powered” as a boolean:

- **true** → output strength 15
- **false** → output strength 0

They do not output analog strengths (1–14).

### Strong + weak power (for chaining)

The family generally implements both:

- `getSignal(...)` (weak power)
- `getDirectSignal(...)` (strong power)

The intent is that you can chain blocks face-to-face and side-to-side without losing the signal.

### Update model

Most blocks recompute when their neighbors change:

- they read inputs from specific sides
- if anything relevant changes, they update their own blockstate
- they notify the neighbor(s) on their output side(s)

This means they behave like “combinational logic blocks”, except for stateful blocks like the RS latch, counter, T-flop, and timer.

## Crafting notes (common ingredients)

Many logic recipes use these item tags:

- `#materia:insulated_wires`
- `#materia:all_plates`

Tag reference:

- [Heat and hot items (tag reference)](../reference/tags/heat-and-hot-items.md)

## Screenshot / GIF ideas (recommended)

These mechanics are much easier to understand with visuals. Suggested captures:

- **A single gate** with levers feeding left/right inputs, lamp on the front output.
- **RS latch** showing `S` set, `R` reset, and `Q` / `!Q` outputs.
- **Counter** showing toggle on rising edge and the **carry pulse** when it wraps.
- **Timer** showing the interval cycling and a 1-tick pulse.
- **A tiny “adder strip”** (half adders + carry combine) to show your “compact logic structures” goal.

