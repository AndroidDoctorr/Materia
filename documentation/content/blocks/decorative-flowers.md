## Decorative flowers

Seven decorative flower blocks added in 1.2.0. All appear in the **Plants** creative tab and generate in the world via biome modifiers under `shared/src/main/resources/data/materia/forge/biome_modifier/`.

See also:

- [Decorative plants (yucca, plantain, reeds, taro)](decorative-plants.md)
- [Flower → dye policy](../../chemistry/dyes/flower-dyes.md)

## White lily

- **Block**: `materia:white_lily` — two-block-tall double plant (`WhiteLilyBlock`)
- **Worldgen**: `materia_white_lily.json` → `materia:white_lily_placed`
- **Biomes**: `#materia:river`, `#materia:temperate` (temperate pond margins)
- **Placement**: grass, dirt, coarse dirt, podzol, mud, muddy mangrove roots
- **Feature**: `TallPlantClusterFeature` (cluster patches)

## Bluebonnet

- **Block**: `materia:bluebonnet` — two-block-tall double plant (`BluebonnetBlock`)
- **Worldgen**: `materia_bluebonnet.json` → `materia:bluebonnet_placed`
- **Biomes**: `#materia:prairie` (Great Plains / Texas grassland)
- **Placement**: same soil list as white lily
- **Feature**: `TallPlantClusterFeature`

## Purple coneflower

- **Block**: `materia:purple_coneflower` — single-block cross flower
- **Worldgen**: `materia_purple_coneflower.json` → `materia:purple_coneflower_placed`
- **Biomes**: `#materia:prairie` (American prairies)

## Fuchsia

- **Block**: `materia:fuchsia` — single-block cross flower
- **Worldgen**: `materia_fuchsia.json` → `materia:fuchsia_placed`
- **Biomes**: `#materia:tropical`, `#materia:temperate_forest`

## Marigold

- **Block**: `materia:marigold` — single-block cross flower
- **Worldgen**: `materia_marigold.json` → `materia:marigold_placed`
- **Biomes**: `#materia:desert`, `#materia:tropical`
- **Dye**: crafts **yellow dye** — [recipe JSON](../../shared/src/main/resources/data/materia/recipes/yellow_dye_from_marigold.json)

## Hibiscus

- **Block**: `materia:hibiscus` — single-block cross flower
- **Worldgen**: `materia_hibiscus.json` → `materia:hibiscus_placed`
- **Biomes**: `#materia:tropical`
- **Dye**: crafts **pink dye** — [recipe JSON](../../shared/src/main/resources/data/materia/recipes/pink_dye_from_hibiscus.json)

## Lotus

- **Block**: `materia:lotus` — cross flower model over a **vanilla lily-pad** base tinted with biome foliage green
- **Worldgen**: `materia_lotus.json` → `materia:lotus_placed` in `#materia:river` and `#materia:warm_wet_surface` (custom water-surface feature; shallow water only)
- **Placement**: **shallow water only** (1–2 blocks deep). Uses `LotusBlock` (`WaterlilyBlock` subclass) plus `LotusBlockItem` so clicking water places the lotus in the air block above the surface.
- **Model**: `shared/src/main/resources/assets/materia/models/block/lotus.json` — lily pad element (`tintindex: 0`) + lotus cross
- **Survival**: breaks like a lily pad; cannot be placed on land

## Assets

Blockstates, models, loot tables, and item models live under `shared/src/main/resources/assets/materia/` and `shared/src/main/resources/data/materia/loot_tables/blocks/`.

Worldgen configured/placed features are generated per port (for example `1.20.1/src/generated/resources/data/materia/worldgen/`).
