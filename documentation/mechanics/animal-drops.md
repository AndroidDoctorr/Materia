## Animal drops (bones + fat)

Materia adds a few **global loot modifiers** that give some passive mobs extra drops when you kill them.

Source of truth:

- Loot modifier list: `shared/src/main/resources/data/forge/loot_modifiers/global_loot_modifiers.json`
- Modifier implementation (1.18.2): `1.18.2/src/main/java/com/torr/materia/loot/AnimalDropModifier.java`

## Rules (important)

These drops only apply when all conditions are met:

- **Killed by player** (`minecraft:killed_by_player`)
- **Correct mob type** (cow/pig/sheep/horse)
- **Random chance** per-kill (varies by mob and drop)

## What drops (by mob)

All values here are pulled from the `shared/` loot modifier JSONs.

### Cow

- Bones: **2–4** `minecraft:bone` (100%)
  - `shared/src/main/resources/data/materia/loot_modifiers/cow_bones.json`
- Fat: **1–3** `materia:animal_fat` (100%)
  - `shared/src/main/resources/data/materia/loot_modifiers/cow_animal_fat.json`

### Pig

- Bones: **1–2** `minecraft:bone` (50%)
  - `shared/src/main/resources/data/materia/loot_modifiers/pig_bones.json`
- Fat: **1–2** `materia:animal_fat` (75%)
  - `shared/src/main/resources/data/materia/loot_modifiers/pig_animal_fat.json`

### Sheep

- Bones: **1–2** `minecraft:bone` (75%)
  - `shared/src/main/resources/data/materia/loot_modifiers/sheep_bones.json`
- Fat: **1–2** `materia:animal_fat` (75%)
  - `shared/src/main/resources/data/materia/loot_modifiers/sheep_animal_fat.json`

Related (sheep-only):

- Wool clumps (see: [Wool clumps](../content/items/wool-clumps.md))

### Horse

- Bones: **2–3** `minecraft:bone` (100%)
  - `shared/src/main/resources/data/materia/loot_modifiers/horse_bones.json`
- Fat: **1–2** `materia:animal_fat` (25%)
  - `shared/src/main/resources/data/materia/loot_modifiers/horse_animal_fat.json`

## What you do with the drops

### Animal fat

Animal fat is used as:

- a **lipid** (poultices, dyes, etc.): `#materia:lipids`
- a possible **lamp oil**: `#materia:lamp_oils`
- a **leather finish** ingredient: [`#materia:leather_finishes`](../reference/tags/bindings-and-adhesives.md#materialeather_finishes)

References:

- [Liquids and containers](../reference/tags/liquids-and-containers.md)
- [Bindings and adhesives](../reference/tags/bindings-and-adhesives.md)
- Item page: [Animal fat](../content/items/animal-fat.md)

### Bones → glue (boiling)

Boiling a bone in a water pot makes glue:

- `minecraft:bone` → `materia:glue`

Source of truth (1.18.2):

- `1.18.2/src/main/java/com/torr/materia/blockentity/WaterPotBlockEntity.java`

See: [Water pot](water-pot.md)

### Bones → crushed bones

There are (at least) two ways:

- Crafting with a crusher:
  - `shared/src/main/resources/data/materia/recipes/crushed_bones.json`
  - Uses: [`#materia:crushers`](../reference/tags/tools-and-tool-like-tags.md#materiacrushers)
- Mortar and pestle (more efficient):
  - `shared/src/main/resources/data/materia/recipes/mortar_crushed_bones.json`

Item page: [Crushed bones](../content/items/crushed-bones.md)

### Bone char (fire pit)

- `minecraft:bone` → `materia:bone_char`
  - `shared/src/main/resources/data/materia/recipes/bone_to_bone_char.json`
- `materia:crushed_bones` → `materia:bone_char`
  - `shared/src/main/resources/data/materia/recipes/crushed_bones_to_bone_char.json`

Item page: [Bone char](../content/items/bone-char.md)

### Bone meal (alternative recipe)

Materia provides a bone meal recipe that uses fat + calcite:

- [`#materia:calcites`](../reference/tags/natural-materials.md#materiacalcites) + `materia:animal_fat` → 2× `minecraft:bone_meal`
  - `shared/src/main/resources/data/minecraft/recipes/bone_meal.json`

