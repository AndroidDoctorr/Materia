## Cannons

Cannons are player-operated (or dispenser-loadable) artillery blocks.

High-level UX:

- **Load powder first** (up to 4)
- **Load ammo second**
- **Right-click with empty hand** to enter aim mode (“cannon’s eye view”)
- **Left-click** to fire (and exit aim mode)

## Loading (powder → ammo)

### 1) Charge with powder (1–4)

Accepted powder items:

- `minecraft:gunpowder`
- `materia:gunpowder_trail` (as an item)

Notes:

- Max charge is **4** powder.
- If the cannon already has ammo loaded, it will not accept more powder until fired/unloaded.

### 2) Load ammo

Ammo can be:

- `materia:stone_cannonball`
- `materia:iron_cannonball`
- `materia:canister_shot`
- `minecraft:tnt`

This is defined by:

- [`#materia:cannon_ammo`](../reference/tags/combat-and-explosives.md#materiacannon_ammo) (`shared/src/main/resources/data/materia/tags/items/cannon_ammo.json`)
- plus an explicit `minecraft:tnt` allowance

Important rule:

- You **cannot** load ammo until the cannon has **at least 1 powder**.

## Aim mode (“cannon’s eye view”)

Aim mode uses an overlay:

<img src="../../shared/src/main/resources/assets/materia/textures/gui/cannon.png" alt="Cannon aim overlay" width="512">

How to enter:

- With a **charged + loaded** cannon, **right-click with an empty hand**.

Controls:

- **Mouse**: aim
- **Arrow keys**: aim (fine control)
- **Left click**: fire and exit aim mode
- **Right click** or **ESC**: exit aim mode without firing

Angle conventions (useful when describing aiming):

- **Pitch**: \(0^\circ\) = straight up, \(90^\circ\) = horizon
- Pitch is clamped to **20°–90°** (can’t aim perfectly vertical)

## Power and range (what powder does)

Powder increases projectile speed.

- 1 powder: low power
- 4 powder: maximum power

Internally, the launch speed is:

- \(speed = 0.55 + 0.18 \times powder\)

## Ammo behavior

### Cannonballs (stone / iron)

- Deal **direct hit damage** to entities
- Do **not** explode (today)
- On impact they disappear (no block damage yet)

Planned / future note:

- You mentioned a future upgrade: cannonballs damaging stone / stone bricks (to cobble / cracked stone). This is **not implemented yet** in the current major-version work.

### Canister shot

- Has a short **fuse** and also detonates on impact
- Creates a small **no-block-damage** explosion effect and spawns shrapnel

### TNT

- Fired as a projectile with a short **fuse** and detonates on impact
- Uses a normal TNT explosion (block damage)

## Quality-of-life interactions

- **Sneak-right-click** on the cannon resets its stored aim angles (for “start over” aiming)
- If you try to interact while it’s already ready, it will tell you it’s loaded

## Automation (dispensers)

Dispensers can load cannons if they face the cannon block.

- **Powder**: dispensers can add powder **only if no ammo is loaded** and the cannon isn’t fully charged
- **Ammo**: dispensers can load ammo **only if powder is present** and no ammo is loaded

## Related

- [Cannon (block)](../content/blocks/cannon.md)
- Ammo items:
  - [Stone cannonball](../content/items/stone-cannonball.md)
  - [Iron cannonball](../content/items/iron-cannonball.md)
  - [Canister shot](../content/items/canister-shot.md)
