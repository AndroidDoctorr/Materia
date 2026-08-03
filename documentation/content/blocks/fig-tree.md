## Fig tree

Fig trees are a temperate worldgen tree with harvestable fruit on the leaves.

## Where it generates

- Biome modifier: `shared/src/main/resources/data/materia/forge/biome_modifier/materia_fig_trees.json`
- Biomes: `#materia:subtropical` (Mediterranean and warm temperate)

**1.18.2:** same rule, injected via `1.18.2/.../ModWorldEvents.java` (not biome modifier JSON in the jar).

**1.20.1 / 1.21.1:** tree shape also in `{port}/src/generated/resources/data/materia/worldgen/configured_feature/fig_tree.json`.

## Blocks

- Log: `materia:fig_log` — hew to 4× `materia:rough_fig_plank`
- Leaves: `materia:fig_leaves` (`has_figs` property; right-click harvest, 20% random-tick regrowth)
- Sapling: `materia:fig_sapling`

Blockstates and models: `shared/src/main/resources/assets/materia/blockstates/fig_leaves.json` (variants use **`has_figs`**, not `has_olives` — olive leaves are a separate block with `has_olives`).

Java source: `{port}/src/main/java/com/torr/materia/block/FigTreeLeavesBlock.java` — the boolean property **must** be registered as `"has_figs"`.

## Items

- `materia:fig` — food
- `materia:rough_fig_plank`, `materia:smooth_fig_plank`
