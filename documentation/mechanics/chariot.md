## Chariot

The **chariot** is a lightweight draft-pulled combat vehicle: two players ride (driver + archer), animals pull, and there is no storage, crafting, sleep, or add-ons. It reuses the hand cart’s draft team, surface speed, and wheel sounds.

Source of truth:

- Entity: `ChariotEntity` in each version’s `.../entity/` package
- Items: `ModChariots`, `ChariotType` (`bronze_chariot`, `iron_chariot`)
- Model / texture: `ChariotModel`, `textures/entity/{bronze,iron}_chariot.png` (48×32 atlas)
- Recipes: `shared/src/main/resources/data/materia/recipes/*_chariot.json`

## Crafting

Shapeless assembly — craft the finished chariot directly (no intermediate body item):

| Chariot | Ingredients |
|---|---|
| **Bronze** | 2× `materia:cart_wheel`, 2× `#materia:smooth_planks`, 3× `materia:bronze_plate`, 1× `materia:bronze_pole`, 1× `#materia:all_rivets` |
| **Iron** | 2× `materia:cart_wheel`, 2× `#materia:smooth_planks`, 3× `materia:iron_plate`, 1× `materia:iron_pole`, 1× `#materia:all_rivets` |

## Model

The hull is a **1×1 block** open box: front wall, floor, and side walls only (no top, no back). A **hitch** extends forward from the front for the draft lead. The texture atlas is **48×32**:

- **Top row (48×16):** right side | front (+ hitch) | left side (rider’s perspective, front-on view)
- **Bottom row (48×16):** wheel (left) | floor (center) | empty (right)

Two wheels sit at the **center of the length** (Z = 0), same diameter and ground clearance as the hand cart.

The **lead attach point** is at **wheel-axle height**, one block **forward of the chariot center** (at the hitch).

## Riding

- **Two player seats only** — no pets, shields, covers, or lanterns.
- **Driver** (first player to mount) sits **4 px from the front** (25% along the 16 px length).
- **Archer** (second player) sits **4 px from the back** (75% along the length) — 8 px between seat centers.
- The archer can **look around and attack** normally while the chariot moves.
- **W / S** — forward / reverse along draft heading.
- **A / D** — steer the **draft team heading** (same as the hand cart).
- Movement requires a **leashed draft team** (tamed horses and other eligible mobs).

### Draft team

Same lead interactions as the [hand cart](cart.md):

- **Lead + use** — attach a nearby eligible mob or transfer one you are leading.
- **Sneak + lead + use** — release all hitched mobs.

Draft pull weights, surface speed tags, and wheel rolling sounds are shared with the hand cart (`entity.cart.move_*`).

### Pickup

- **Sneak + use** (empty chariot, no riders) — pick up the chariot item (preserves health in NBT).
- If animals are still hitched, sneak-use **first releases the draft team**, then pickup on a second sneak-use.

## Variants and combat durability

Chariots use the same **60 HP baseline** as wood carts, then scale by metal type. Incoming damage is multiplied before subtracting health (lower multiplier = tougher). Fire damage gets an extra reduction on metal hulls.

| Type | Mass | Max health | Damage taken | Fire damage taken |
|---|---:|---:|---:|---:|
| Bronze | 1.05× | **~80** | **85%** (15% reduction) | **75%** |
| Iron | 1.25× | **~120** | **50%** (half damage) | **35%** |

For comparison, a typical **oak cart** has **60 HP** and takes **100%** damage; **dark oak** reaches **~75 HP** with **80%** damage taken. Bronze chariots sit above most wood carts; **iron chariots** are built for sustained arrow fire and melee brawls — roughly **double the effective durability** of a baseline oak cart.

Bronze is lighter; iron is tougher and slightly heavier (slower acceleration).

## See also

- [Hand cart](cart.md) — full travel vehicle with chest, sleep, covers, and shields
