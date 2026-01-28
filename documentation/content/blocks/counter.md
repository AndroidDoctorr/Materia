## Counter

<img src="../../../shared/src/main/resources/assets/materia/textures/block/counter_0.png" alt="Counter (top texture)" width="64" height="64">

The counter is a compact 1-bit “toggle” counter with a **carry output**.

It’s designed to chain into multi-bit counters:

- the front output acts like the stored bit \(Q\)
- the carry output pulses when the bit wraps from 1 back to 0

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/block/CounterBlock.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/counter.json`

## IO mapping

Top-down view (relative to facing):

```text
         Q output (front)
                |
  input (left) [ COUNTER ] carry (right)
                |
          reset (back)
```

- **Input**: left side
- **Reset**: back
- **Q output**: front
- **Carry output**: right side

## Behavior (edge-triggered)

### Reset

On a **rising edge** of reset (0 → 1):

- \(Q\) is set to 0
- carry is cleared

### Counting (toggle on rising edge)

On a **rising edge** of input (0 → 1), if reset is not active:

- If \(Q=0\): it toggles to \(Q=1\) and carry stays off
- If \(Q=1\): it toggles to \(Q=0\) and **carry becomes 1** (a carry pulse)

### Carry pulse duration

Carry is cleared on the **falling edge** of the input (1 → 0), so the carry pulse effectively lasts “while the input is high” on the wraparound transition.

Practical wiring tip:

- If you want a clean one-tick carry pulse, feed input from a one-tick pulse source.

## Screenshot / GIF ideas

- An input pulse generator feeding input, lamp on Q and lamp on carry (shows carry on wrap).

