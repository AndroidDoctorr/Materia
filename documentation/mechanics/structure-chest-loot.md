## Structure chest loot

Materia adds **bonus loot** to many vanilla structure chests via Forge **global loot modifiers**. Vanilla chest contents are unchanged; when the roll succeeds, Materia items are **added** on top.

Source of truth:

- Modifier registry: `shared/src/main/resources/data/forge/loot_modifiers/global_loot_modifiers.json`
- Per-structure injectors: `shared/src/main/resources/data/materia/loot_modifiers/chests/*.json`
- Materia loot tables: `shared/src/main/resources/data/materia/loot_tables/chests/*.json`
- Java: `InjectLootTableModifier` in each version’s `.../loot/` package (registered as `materia:inject_loot_table`)

## How it works

1. A chest generates loot using the normal vanilla loot table (e.g. `minecraft:chests/simple_dungeon`).
2. Forge runs global loot modifiers. Materia’s **`inject_loot_table`** modifier checks:
   - **`forge:loot_table_id`** — matches the vanilla structure chest table
   - **`minecraft:random_chance`** — structure-specific probability (see table below)
3. On success, Materia rolls its own loot table and **appends** those stacks to the chest.

This is intentionally **additive**, not a replacement — players still get vanilla loot; Materia items are teasers and shortcuts, not full progression skips.

## Structures and roll chances

| Vanilla loot table | Materia loot table | Chance |
|---|---|---:|
| `minecraft:chests/simple_dungeon` | `materia:chests/simple_dungeon` | 80% |
| `minecraft:chests/abandoned_mineshaft` | `materia:chests/abandoned_mineshaft` | 85% |
| `minecraft:chests/desert_pyramid` | `materia:chests/desert_pyramid` | 90% |
| `minecraft:chests/jungle_temple` | `materia:chests/jungle_temple` | 85% |
| `minecraft:chests/stronghold_corridor` | `materia:chests/stronghold` | 70% |
| `minecraft:chests/stronghold_crossing` | `materia:chests/stronghold` | 70% |
| `minecraft:chests/stronghold_library` | `materia:chests/stronghold` | 75% |
| `minecraft:chests/buried_treasure` | `materia:chests/buried_treasure` | 95% |
| `minecraft:chests/shipwreck_treasure` | `materia:chests/shipwreck` | 90% |
| `minecraft:chests/shipwreck_supply` | `materia:chests/shipwreck_supply` | 75% |
| `minecraft:chests/woodland_mansion` | `materia:chests/woodland_mansion` | 85% |
| `minecraft:chests/igloo_chest` | `materia:chests/igloo` | 80% |

Modifier JSON paths follow `shared/.../loot_modifiers/chests/chest_<name>.json`.

## What can appear (by theme)

### Simple dungeon — `simple_dungeon.json`

Early survival teasers: **plant fiber**, **pebble**, **rough oak plank**, **lashing**, **rope**, **knapped flint**, crop seeds (**flax**, **beans**, **peppers**, **rice**, **cotton**). Bonus pool (lower roll count): **weak/medium poultice**, **bedroll**, **flint knife**, **hand axe**.

### Abandoned mineshaft — `abandoned_mineshaft.json`

Metal/chemistry hints: **raw tin**, **raw zinc**, **tin nugget**, **saltpeter**, **pebble**, **coal coke**, **quicklime**; bonus **bronze nails**, **handle**, **bronze chisel**.

### Desert pyramid — `desert_pyramid.json`

**Saltpeter**, **salt**, **ochre** / **red ochre**, **esparto**, **dried gourd**; bonus **tan dye**, **taupe dye**.

### Jungle temple — `jungle_temple.json`

**Indigo dye**, **beans**, **peppers**, **corn cob**, **cotton seeds**, **plant fiber**, **rope**; bonus **burrito**, **chili**.

### Stronghold — `stronghold.json`

Knowledge/dye theme: **indigo dye**, **lavender dye**, **verdigris**, **ink cup**, **fine paper pulp**, **paper frame**. Rare bonus: **and gate**, **timer**, **rs latch**, **tyrian purple iota**.

### Buried treasure — `buried_treasure.json`

Coastal prestige: **murex glands** (all three species), **crushed shells**, **wine bottle**, **olive oil bottle**, **vinegar bottle**, **grape juice bottle**. Very rare bonus: **boiled murex glands**, **tyrian purple iota**, **tyrian purple dye**.

### Shipwreck treasure — `shipwreck.json`

Lighter buried-treasure set: murex glands, **crushed shells**, bottled drinks; rare **tyrian purple iota**.

### Shipwreck supply — `shipwreck_supply.json`

Utility: **plant fiber**, **rope**, **salt**, **crushed shells**, **vinegar bottle**.

### Woodland mansion — `woodland_mansion.json`

Decorative/storage: dyed **sacks** and **blankets** (lavender, ochre, indigo, taupe, tyrian purple); bonus **wine cup**, **beer cup**, **soft cheese**, **air flute**, **air maraca**, occasional **ochre bed** / **lavender bed**.

### Igloo — `igloo.json`

Cold survival: **bedroll**, **animal fat**, **taupe/lavender blanket**; bonus **burrito**, **beans and rice**, **baked squash**.

## Design notes

- **No full bronze/iron armor or steel tools** in these tables — those would bypass Materia’s age gates.
- **No cannon or dynamite** in routine structure loot.
- **Tyrian purple** is teased via **iota** / **glands** in treasure/stronghold loot; full **tyrian purple dye** only appears at jackpot weight in **buried treasure**.
- Pack makers can **override** `materia:chests/*` loot tables or disable individual entries in `global_loot_modifiers.json` without touching Java.

## Related

- [Animal drops (bones + fat)](animal-drops.md) — other global loot modifiers
- [Healing poultices](../content/items/healing-poultices.md)
- [Sacks](../content/items/sacks.md)
- [Tyrian purple](../chemistry/dyes/tyrian-purple.md)
