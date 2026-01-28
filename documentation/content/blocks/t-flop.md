## T flip-flop

<img src="../../../shared/src/main/resources/assets/materia/textures/block/t_flop_0.png" alt="T flip-flop (top texture)" width="64" height="64">

The T flip-flop stores a boolean output and **toggles** it on each **rising edge** of its input.

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/block/TFlopBlock.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/t_flop.json`

## IO mapping

Top-down view:

```text
      output (front)
            |
        [ T-FLOP ]
            |
       input (back)
```

- **Input**: back
- **Output**: front

## Behavior (edge-triggered)

It tracks the previous input state and detects a **rising edge**:

- When input goes **0 → 1**, the block **toggles** output.
- When input goes **1 → 0**, it does **not** toggle (it just updates its stored “input” state).

This makes it useful for:

- dividing a pulse stream by 2 (toggle on every pulse)
- building counters and state machines

## Screenshot / GIF ideas

- A lever → repeater (pulse) → T-flop → lamp (shows toggle per pulse).

