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

### Village chests (70% each)

Village buildings use **separate Materia pools** — one loot table per building theme, injected into the matching vanilla village chest table. All village injectors use **70%** chance (slightly lower than dungeons so villages stay early-game, not jackpot chests).

| Vanilla loot table | Materia loot table |
|---|---|
| `minecraft:chests/village/village_plains_house` | `materia:chests/village_house` |
| `minecraft:chests/village/village_desert_house` | `materia:chests/village_house` |
| `minecraft:chests/village/village_savanna_house` | `materia:chests/village_house` |
| `minecraft:chests/village/village_snowy_house` | `materia:chests/village_house` |
| `minecraft:chests/village/village_taiga_house` | `materia:chests/village_house` |
| `minecraft:chests/village/village_fletcher` | `materia:chests/village_fletcher` |
| `minecraft:chests/village/village_shepherd` | `materia:chests/village_shepherd` |
| `minecraft:chests/village/village_butcher` | `materia:chests/village_butcher` |
| `minecraft:chests/village/village_fisher` | `materia:chests/village_fisher` |
| `minecraft:chests/village/village_mason` | `materia:chests/village_mason` |
| `minecraft:chests/village/village_toolsmith` | `materia:chests/village_toolsmith` |
| `minecraft:chests/village/village_weaponsmith` | `materia:chests/village_weaponsmith` |
| `minecraft:chests/village/village_armorer` | `materia:chests/village_armorer` |
| `minecraft:chests/village/village_tannery` | `materia:chests/village_tannery` |
| `minecraft:chests/village/village_cartographer` | `materia:chests/village_cartographer` |
| `minecraft:chests/village/village_temple` | `materia:chests/village_temple` |

Modifier JSON paths follow `shared/.../loot_modifiers/chests/chest_<name>.json` (village files prefixed `chest_village_`).

**Village design intent:** basic survival teasers only — fiber, rope, lashing, pebbles, seeds, salt, early tools, poultices, bedroll, nails, gut — **no** rugs, mansion decor, tyrian purple, or other late-game dungeon rewards.

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

### Village house — `village_house.json`

Shared by plains/desert/savanna/snowy/taiga house chests: **plant fiber**, **pebble**, **lashing**; bonus pool **bedroll**, **weak poultice**.

### Village fletcher — `village_fletcher.json`

**Plant fiber**, **rope**, **lashing**, **flax seeds**.

### Village shepherd — `village_shepherd.json`

**Plant fiber**, **lashing**, **flax seeds**, **cotton seeds**.

### Village butcher — `village_butcher.json`

**Animal fat**, **beans**, **peppers**, **salt**.

### Village fisher — `village_fisher.json`

**Salt**, **crushed shells**, **plant fiber**, **rope**.

### Village mason — `village_mason.json`

**Pebble**, **rough oak plank**, **knapped flint**.

### Village toolsmith — `village_toolsmith.json`

**Knapped flint**, **pebble**, **bronze nails**, **iron nails**; rare **flint knife**, **hand axe**.

### Village weaponsmith — `village_weaponsmith.json`

**Knapped flint**, **flint knife**, **hand axe**, **pebble**.

### Village armorer — `village_armorer.json`

**Bronze nails**, **iron nails**, **iron band**.

### Village tannery — `village_tannery.json`

**Lashing**, **rope**, **animal gut**, **clean gut**.

### Village cartographer — `village_cartographer.json`

**Bedroll**, **weak poultice**, **esparto**, **plant fiber**.

### Village temple — `village_temple.json`

**Weak poultice**, **medium poultice**, **bedroll**.

## Design notes

- **No full bronze/iron armor or iron tools** in these tables — those would bypass Materia’s age gates.
- **No cannon or dynamite** in routine structure loot.
- **Tyrian purple** is teased via **iota** / **glands** in treasure/stronghold loot; full **tyrian purple dye** only appears at jackpot weight in **buried treasure**.
- Pack makers can **override** `materia:chests/*` loot tables or disable individual entries in `global_loot_modifiers.json` without touching Java.

## Related

- [Hand cart](cart.md) — travel vehicle; village loot supplies early cart/cover materials
- [Animal drops (bones + fat)](animal-drops.md) — other global loot modifiers
- [Healing poultices](../content/items/healing-poultices.md)
- [Sacks](../content/items/sacks.md)
- [Tyrian purple](../chemistry/dyes/tyrian-purple.md)
