## Rock (block)

Rocks are small blocks that can fall like sand/gravel and can be waterlogged.

## Where it comes from

Rocks generate in the world as small surface and cave piles:

- **Surface:** `#materia:overworld_land_non_rocky` (plains, forests, savannas, etc. — not oceans, rivers, or beaches)
- **Caves:** `#materia:overworld_non_rocky`
- **Rocky biomes** (mountains, badlands, windswept hills, …): higher surface + cave rates via `#materia:rocky`

Placement uses the **`materia:loose_ground_block`** feature so each rock sits in air **on top of** solid, non-aquatic ground (not floating or over water).

Approximate non-rocky **surface** rates (new chunks only):

| Port | Rate |
|---|---|
| 1.18.2 / 1.19.2 | ~1 rock per **2** chunks (Java `ModPlacedFeatures`) |
| 1.20.1 / 1.21.1 | ~1 rock per **3** chunks (generated placed-feature JSON) |

## Source of truth (by port)

| Port | Registration | Biome wiring |
|---|---|---|
| **1.18.2** | `{port}/.../ModConfiguredFeatures.java`, `ModPlacedFeatures.java` | `{port}/.../ModWorldEvents.java` (`BiomeLoadingEvent`) |
| **1.19.2** | Same Java files | `shared/.../forge/biome_modifier/materia_surface_rocks_non_rocky.json`, `materia_rocks_non_rocky.json`, `materia_rocks_rocky.json` |
| **1.20.1 / 1.21.1** | `{port}/src/generated/resources/data/materia/worldgen/` + bootstrap | Same shared biome modifier JSON |

See [Version differences — worldgen wiring](../../reference/VERSION_DIFFERENCES.md#worldgen-wiring-how-features-reach-biomes).

## Drops

Breaking a rock block drops the rock item:

- Loot table: `shared/src/main/resources/data/materia/loot_tables/blocks/rock.json`

## Notes

- Can become waterlogged
- Falls when unsupported
