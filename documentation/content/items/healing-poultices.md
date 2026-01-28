## Healing poultices

<img src="../../../shared/src/main/resources/assets/materia/textures/item/weak_poultice.png" alt="Weak poultice" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/medium_poultice.png" alt="Medium poultice" width="32" height="32">
<img src="../../../shared/src/main/resources/assets/materia/textures/item/strong_poultice.png" alt="Strong poultice" width="32" height="32">

Healing poultices are early-game, consumable healing items.

They do not apply potion effects; they directly heal the player.

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/item/PoulticeItem.java`
- Registered in `1.18.2/src/main/java/com/torr/materia/ModItems.java` (heal amounts)

## Using a poultice

- Right-click and hold for **20 ticks** (~1 second).
- On completion, it:
  - heals the player
  - plays a drink-like sound
  - spawns “instant effect” particles
  - consumes 1 poultice (unless you’re in creative)

Animation note:

- Uses the **bow** use animation (it’s a “use” action, not an eat/drink animation).

## Heal amounts (1.18.2)

Minecraft uses **2 health points = 1 heart**.

- **Weak poultice**: 2.0 health = **1 heart**
- **Medium poultice**: 4.0 health = **2 hearts**
- **Strong poultice**: 8.0 health = **4 hearts**

## Crafting (recipes)

- Weak: `shared/src/main/resources/data/materia/recipes/weak_poultice.json`
- Medium: `shared/src/main/resources/data/materia/recipes/medium_poultice.json`
- Strong: `shared/src/main/resources/data/materia/recipes/strong_poultice.json`

### Common ingredients

All poultices use:

- a linen/bandage ingredient: `#materia:linens`
- a fat/oil: `#materia:lipids`
- a water ingredient: `#materia:water`

Higher tiers add:

- vinegar (`materia:vinegar`) and boiled bark (`materia:boiled_bark`)
- strong poultice also uses `materia:resin` and `minecraft:honeycomb`

Tag references:

- `#materia:water`: [Liquids and containers](../../reference/tags/liquids-and-containers.md#materiawater)
- `#materia:lipids`: [Liquids and containers](../../reference/tags/liquids-and-containers.md#materialipids)
- `#materia:linens`: [Textiles and storage materials](../../reference/tags/textiles-and-storage.md#materialinens)

