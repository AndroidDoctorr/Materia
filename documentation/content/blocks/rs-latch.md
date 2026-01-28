## RS latch

<img src="../../../shared/src/main/resources/assets/materia/textures/block/rs_latch_0.png" alt="RS latch (top texture)" width="64" height="64">

The RS latch is a **memory** block: it stores a single boolean state \(Q\) until it is changed by **Set** or **Reset** inputs.

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/block/RsLatchBlock.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/rs_latch.json`

## IO mapping

Top-down view (relative to facing):

```text
          Q output (front)
                 |
  S (left)   [ RS LATCH ]   R (right)
                 |
          !Q output (back)
```

- **S (Set)** input: left side
- **R (Reset)** input: right side
- **Q** output: front
- **!Q** output: back

## Behavior

### Normal cases

On neighbor updates, it recomputes the next stored state:

- If **S=1** and **R=0** → set \(Q=1\)
- If **R=1** and **S=0** → reset \(Q=0\)
- If **S=0** and **R=0** → latch (keep current \(Q\))

### Invalid case (S and R both high)

If **S=1** and **R=1**, it forces \(Q=0\) and also forces **both outputs low**.

Practical meaning:

- don’t drive both set and reset at the same time unless you want “force-off”.

## Notes for chaining

- Outputs are provided as both weak + strong redstone power in their output directions, intended for direct adjacency chaining.

