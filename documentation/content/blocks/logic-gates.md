## Logic gates (AND/OR/XOR/NAND/NOR/XNOR/NOT)

These are the basic combinational logic blocks.

They’re designed to chain cleanly:

- **Inputs** are read from the **left** and **right** sides (relative to facing)
- The **output** is emitted on the **front**
- Output is **binary** (0 or 15)
- Gates provide **strong** and **weak** power on their output side (so adjacent chaining works)

Mechanics page:

- [Logic blocks (mechanics)](../../mechanics/logic-blocks.md)

## Shared IO convention (AND/OR/XOR/NAND/NOR/XNOR)

These six gates share a common base class in 1.18.2:

- `1.18.2/src/main/java/com/torr/materia/block/LogicGateBlock.java`

IO mapping:

- **A input**: left side
- **B input**: right side
- **Output**: front

Quick diagram (top-down):

```text
      output (front)
            |
  A (left) [ GATE ] B (right)
```

## Gates

### AND gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/and_0.png" alt="AND gate (top texture)" width="64" height="64">

- **Block id**: `materia:and_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/and_gate.json`
- **Logic**: output is true only if A and B are true

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 0 |
| 1 | 0 | 0 |
| 0 | 1 | 0 |
| 1 | 1 | 1 |

### OR gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/or_0.png" alt="OR gate (top texture)" width="64" height="64">

- **Block id**: `materia:or_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/or_gate.json`
- **Logic**: output is true if A or B is true

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 0 |
| 1 | 0 | 1 |
| 0 | 1 | 1 |
| 1 | 1 | 1 |

### XOR gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/xor_0.png" alt="XOR gate (top texture)" width="64" height="64">

- **Block id**: `materia:xor_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/xor_gate.json`
- **Logic**: output is true if A and B differ

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 0 |
| 1 | 0 | 1 |
| 0 | 1 | 1 |
| 1 | 1 | 0 |

### NAND gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/nand_0.png" alt="NAND gate (top texture)" width="64" height="64">

- **Block id**: `materia:nand_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/nand_gate.json`
- **Alternative recipe**: `shared/src/main/resources/data/materia/recipes/nand_gate_from_and.json`
- **Logic**: output is the inverse of AND

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 1 |
| 1 | 0 | 1 |
| 0 | 1 | 1 |
| 1 | 1 | 0 |

### NOR gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/nor_0.png" alt="NOR gate (top texture)" width="64" height="64">

- **Block id**: `materia:nor_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/nor_gate.json`
- **Alternative recipe**: `shared/src/main/resources/data/materia/recipes/nor_gate_from_or.json`
- **Logic**: output is the inverse of OR

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 1 |
| 1 | 0 | 0 |
| 0 | 1 | 0 |
| 1 | 1 | 0 |

### XNOR gate

<img src="../../../shared/src/main/resources/assets/materia/textures/block/xnor_0.png" alt="XNOR gate (top texture)" width="64" height="64">

- **Block id**: `materia:xnor_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/xnor_gate.json`
- **Alternative recipe**: `shared/src/main/resources/data/materia/recipes/xnor_gate_from_xor.json`
- **Logic**: output is true if A and B are the same

Truth table:

| A | B | OUT |
|---|---|-----|
| 0 | 0 | 1 |
| 1 | 0 | 0 |
| 0 | 1 | 0 |
| 1 | 1 | 1 |

## NOT gate (inverter)

<img src="../../../shared/src/main/resources/assets/materia/textures/block/not_0.png" alt="NOT gate (top texture)" width="64" height="64">

- **Block id**: `materia:not_gate`
- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/not_gate.json`
- **Logic**: output is the inverse of the back input

IO mapping (from 1.18.2 `NotGateBlock`):

- **Input**: back
- **Output**: front

Truth table:

| IN | OUT |
|----|-----|
| 0 | 1 |
| 1 | 0 |

Related tag note:

- The mod also defines an “inverters” tag used elsewhere: `shared/src/main/resources/data/materia/tags/items/inverters.json`

