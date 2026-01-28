## Half adder

<img src="../../../shared/src/main/resources/assets/materia/textures/block/half_adder_0.png" alt="Half adder (top texture)" width="64" height="64">

The half adder is a compact “two-bit in → sum + carry out” block.

It’s explicitly designed for chaining into adders:

- Sum continues forward
- Carry comes out the right side

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/block/HalfAdderBlock.java`

## Obtaining / crafting

In the current `shared/` datapack assets, there is **no crafting recipe JSON** for `materia:half_adder`.

That usually means one of:

- it’s intended to be creative/commands-only for now, or
- the survival recipe hasn’t been added yet.

## IO mapping

This block’s IO is intentionally “adder-shaped” (different from the generic left/right gate convention):

- **A input**: back
- **B input**: left
- **Sum output**: front
- **Carry output**: right

Top-down view (relative to facing):

```text
        sum (front)
            |
 B (left) [ HALF ] carry (right)
            |
         A (back)
```

## Logic

- **Sum** = A XOR B
- **Carry** = A AND B

Truth table:

| A | B | SUM | CARRY |
|---|---|-----|-------|
| 0 | 0 | 0 | 0 |
| 1 | 0 | 1 | 0 |
| 0 | 1 | 1 | 0 |
| 1 | 1 | 0 | 1 |

## Screenshot / GIF ideas

- Two levers feeding A and B, lamp on sum and lamp on carry.

