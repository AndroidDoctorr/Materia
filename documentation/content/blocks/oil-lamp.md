## Oil lamp

<img src="../../../shared/src/main/resources/assets/materia/textures/item/oil_lamp.png" alt="Oil lamp" width="64" height="64">

The oil lamp is an early light source that burns “lamp oils” (like olive oil or animal fat).

Source of truth (stable behavior):

- `1.18.2/src/main/java/com/torr/materia/OilLampBlock.java`
- Registered in `1.18.2/src/main/java/com/torr/materia/ModBlocks.java` (`OIL_LAMP`, light level 15)

## Crafting

- **Recipe JSON**: `shared/src/main/resources/data/materia/recipes/oil_lamp.json`

Ingredients:

- `materia:crucible`
- `#materia:lamp_oils` (see [tag reference](../../reference/tags/liquids-and-containers.md#materialamp_oils))
- `#materia:lamp_wicks` (see [tag reference](../../reference/tags/liquids-and-containers.md#materialamp_wicks))

## Behavior

### Light and particles

- Emits **light level 15**
- Spawns **flame** particles (and occasional smoke) while it’s lit and dry

### Water interaction

The oil lamp is “water-hostile”:

- You **can’t place it into water** (placement fails if the target space contains water)
- If it ever becomes waterlogged, it **breaks itself** and drops as an item

Practical meaning: don’t use it underwater or in places where water flows through.

## Drops

- **Loot table**: `shared/src/main/resources/data/materia/loot_tables/blocks/oil_lamp.json`

