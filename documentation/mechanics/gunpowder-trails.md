## Gunpowder trails

Gunpowder can be placed on the ground like redstone to form a **gunpowder trail**.

Lighting one end makes the trail ignite in sequence (“dominoes”), and the end of the trail can ignite nearby blocks (TNT, flammables, etc.).

## Placing gunpowder

How to place:

- **Right-click** a block face with `minecraft:gunpowder` in hand.
- The mod places a `materia:gunpowder_trail` segment into the adjacent air/replaceable block space.

Placement rules:

- Can’t be placed into fluids.
- Requires a solid surface below (the block below must support it).
- Special-case: right-clicking gunpowder on a **cannon** charges the cannon instead (so gunpowder trail placement is skipped).

## Trail connections

Gunpowder trails connect to:

- other gunpowder trail segments
- `minecraft:tnt` (as a “connectable” endpoint)

## Ignition sources

Gunpowder trails ignite when:

- you light them with flint and steel (or a bow drill, which behaves like flint and steel here)
- they touch fire/lava (adjacent fire/soul fire/base fire blocks, or adjacent lava fluid)

## How ignition propagates

When a segment is ignited, it burns through a few short stages and then:

- spreads to connected trail segments
- can directly ignite adjacent TNT (as a connected endpoint)
- if it’s an **end** of the trail, it tries to light “in front” by placing fire in the space ahead (or on top of the block ahead)

Practical result:

- Trail ends can ignite blocks at the end of the chain, including TNT and anything else that will catch from fire (for example: flammable blocks, and containers that react to fire).

## Sparks / embers and fire spread

While a trail segment is burning:

- it emits spark/ember particles
- it has a small chance to ignite nearby flammable blocks by placing fire in adjacent air spaces

## Weather / water interactions

- **Rain**: burning gunpowder segments are extinguished.
- **Water**: if water replaces a trail segment, it drops **1× gunpowder** (wash-away behavior).

## Related

- [Gunpowder trail (block)](../content/blocks/gunpowder-trail.md)
- [Cannons](cannons.md) (gunpowder is used to charge cannons)
