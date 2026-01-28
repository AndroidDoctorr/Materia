## Bedroll

<img src="../../../shared/src/main/resources/assets/materia/textures/item/bedroll.png" alt="Bedroll" width="64" height="64">

The bedroll is a **temporary sleeping item**. You can place it at night, sleep, and it will be **returned to you** after use.

Unlike beds, a bedroll **does not set your spawn point**.

Source of truth (stable behavior):

- `1.18.2/src/main/java/com/torr/materia/item/BedrollItem.java`
- `1.18.2/src/main/java/com/torr/materia/block/BedrollBlock.java`
- `1.18.2/src/main/java/com/torr/materia/events/BedrollSleepHandler.java`
- `1.18.2/src/main/java/com/torr/materia/events/BedrollSpawnHandler.java`

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/bedroll.json`

At a glance, it’s made from:

- 3× `materia:bundle`
- 3× `materia:rope`

## Using the bedroll

### Placement rules

When you use a bedroll on a block face, it places a **2-block** bedroll (like a bed):

- the “head” goes in front of you
- the “foot” goes behind the head (in the opposite direction of facing)

It requires both spaces to be replaceable.

### When can you place it?

The bedroll can only be used for sleeping when:

- it’s **night**, or
- there is a **thunderstorm**

(The item blocks placement during daytime in normal weather.)

### What happens when you sleep

When sleep succeeds:

- the game time advances to **morning**
- the placed bedroll blocks are removed
- you receive the bedroll item back into your inventory

There is an intentional short delay (~5 seconds) before the time-skip/cleanup so the sleep transition doesn’t feel instant.

### Spawn point

Bedrolls **cannot** be used to set your spawn point.

If a spawn-set event would be created from interacting with a bedroll, it is canceled (so you don’t accidentally “waste” your spawn on a disposable sleep surface).

