# Changelog

All notable changes to **Materia** (mod id: `materia`) will be documented in this file.

Planned work is tracked in [`documentation/release-roadmap.md`](documentation/release-roadmap.md).

## Unreleased

#### Decorative building (1.3.x polish)

- **Column bases:** `materia:{material}_column_base` for stone, limestone, marble, sandstone, blackstone, and terracotta — stonecutter from matching base block; stacks under columns like capitals stack above.
- **Wrought iron fence & gate:** connection logic rewrite — straight runs use a single oriented panel; corners/T-junctions use post + arms; connects to **fence gates** as well as other fences; neighbor refresh on place/break. **Jump height** matches vanilla wood fences (1.5-block collision). **Outline vs collision** split like vanilla (`getShape` / `getVisualShape` at 1 block; `getCollisionShape` at 1.5 blocks).
- **Water wash-away fix:** wrought iron fences/gates, balustrades, and metal finials (spires, ball finials, acorn finials) no longer break when flowing water passes through; finials use solid collision voxels instead of `noCollision` props.

#### Docs

- Updated **stone trim**, **wrought iron building**, **balustrades**, and **metal finials** pages; release roadmap checklist extended for column bases and decor fixes.

## 1.3.2

Patch release: hot-metal quenching UX, loot compatibility, and torch crafting.

#### Hot metals

- **Quenching is player-initiated:** heated `#materia:heatable_metals` no longer cool automatically when dropped in water (follow-up to **1.3.1** performance work).
- **Right-click quench** with hot metal in hand on a **water source block**, a **water pot with water**, or a **filled water cauldron** (cauldron loses one layer per quench).
- **Tongs → water pot** quenching unchanged.

#### Compatibility

- **Mob loot modifiers** (bones, fat, gut, sheep wool clumps) only apply on **vanilla entity loot tables** (`minecraft:entities/...`, including sheep color sub-tables such as `entities/sheep/white`), so modded mobs with custom loot tables are not affected.
- **Fix:** sheep wool clumps again replace wool blocks — the earlier `forge:loot_table_id` JSON gate matched only `minecraft:entities/sheep`, but vanilla wool drops roll from per-color tables like `minecraft:entities/sheep/white`.

#### Items

- **Animal fat** added to `#materia:torch_fuels` — craft torches with fat + stick (alongside pitch and tar).

#### Docs

- Updated **water pot**, **hot metals**, **animal drops**, **animal fat**, and compatibility notes for quenching and loot gating.

## 1.3.1

Patch release: server TPS fix for hot-metal quenching.

#### Performance

- **Hot metal quench:** `HotMetalQuenchHandler` no longer scans every `ItemEntity` in the world each tick (via a ±30M-block AABB per dimension). Only heated metal drops are tracked and checked — fixes severe server lag when many items exist.

## 1.3.0

Architecture update after **1.2.0**: multi-version layout (1.18.2–1.21.1), shared assets, and a large decorative-building pass. Patch fixes: **1.3.1** (quench lag), **1.3.2** (quench UX, loot compat, torch fat).

#### Architecture

- **Multi-version repo:** shared resources plus per-port Java; decor and recipes ship on all supported Forge versions.
- **Mosaic & alphabet blocks:** prototype code kept in-repo but **not shipped in Materia** — planned as a separate optional signage mod.
- **Planter & urn planting:** interactive block entities on **1.18.2**, **1.19.2**, and **1.20.1**.

#### Still planned (may slip past 1.3.0)

- **Cork oak**, **cork**, cork bottles/item frames, **cork board** (notes with red string).
- **Marble**-themed blocks/items beyond current stonecutter set (exact scope TBD).
- **Flower boxes** — additional variants beyond stone planter (TBD).

#### Bug fixes & roofs

- **Cannons:** fix E/W vs N/S barrel model rotation (axis-dependent aim yaw sign in BER; firing unchanged).
- **Rainbow eucalyptus:** leaves from rainbow trees tagged `rainbow=true` → drop rainbow saplings (5% chance, same as normal).
- **Copper roofs:** copper plate on roof frame (shapeless craft or right-click); vanilla-style oxidation on placed blocks; custom corner textures per stage.
- **Shingle roofs:** tar/pitch + smooth planks → shingles; 4 shingles + roof frame → shingle roof block; wood scrape sound on placement.
- **Wood doors and trapdoors:** fig, cedar, eucalyptus, and rubber wood — vanilla-style models, brass hinge/handle recipes matching oak.

#### Wood doors & trapdoors

- **Doors and trapdoors** for remaining Materia wood types (palm, cypress, baobab, maple, etc.) — vanilla-style models and brass hinge/handle recipes matching oak.

#### Glazed terracotta

- Ten Materia dye colors (**ochre**, **red ochre**, **olive**, **burgundy**, **tan**, **teal**, **indigo**, **tyrian purple**, **charcoal gray**, **taupe**) smelted from matching terracotta in Materia kiln (plus vanilla furnace smelting for compatibility).

#### Decorative building

- **Shutters:** wall-mounted louvered panels for all Materia wood types (except nether); right-click or redstone toggles open/closed; joiner + stick + smooth-plank recipe (×2). **Fix:** open-half model UVs — wide panel faces on E/W, thin edge faces on N/S.
- **Curtains:** 26 dye/carpet colors; upright window placement (same rules as shutters); custom curtain sound; carpet + stick recipe (×2).
- **Awnings:** 26 colors; roof-frame-style sloped canopy with shared arm texture; **inner/outer corner joins** like roof tiles (straight / inner-left / inner-right / outer-left / outer-right shapes); dedicated **corner triangle textures** per color when art exists; stick + colored carpet + white carpet + rope recipe; cutout rendering.
- **Floor rugs:** 2×1 double blocks (foot/head halves). **Medallion** (pattern 1), **Ornate** (2), **Rosette** (3), and **Lattice** (4) in red/blue/green/purple — woven on the loom from rug base + field dye + pattern item (patterns: paper + gold nugget + Materia flower; rug base: neutral blanket + 16× white/taupe string). **Rare rugs** (5–10: Dragon, Diamonds, Navajo, Welcome, Agrabah, Rainbow) are loot-only from structure chests (not craftable).
- **Stone planter:** half-slab window box from stonecutter; two `#materia:planter_plants` slots; facing blockstate.
- **Stone urn:** stonecutter-carved urn; one `#materia:urn_plants` slot (includes tall yucca/plantain); saplings display without growing.
- **Stone decor:** stonecutter tiles/small bricks/polished variants (stone, marble, limestone, sandstone, blackstone).
- **Balustrades:** **`materia:{material}_balustrade`** for **stone, limestone, marble, terracotta, blackstone, and sandstone** — stonecutter from matching base block; **connect only to the same material**; multipart straight, **L-corner**, **T**, and **4-way** joins; isolated segments keep a **facing** so N/S placement works; dedicated item icons.
- **Stone trim:** columns and column capitals for stone, limestone, marble, sandstone, blackstone, and terracotta (plain decorative blocks; 3D block item models). **Cornices** and **brackets** for the same six materials (wall-mounted trim; dedicated item textures). **Stone acorn finials** for stone, limestone, marble, sandstone, and terracotta (stonecutter from base block).
- **Metal finials:** bronze, gold, and wrought iron **spires**, **ball finials**, and **acorn finials** — tall cross models (2-block spire/ball, 1-block acorn); forged on the iron anvil from hot plate + rod with hammer, tongs, and chisel. Gold finials use dedicated item icons.
- **Stone statues:** body + bust pairs for stone, limestone, marble, sandstone, blackstone, and terracotta; stonecutter-carved with per-material face textures; bust auto-aligns when stacked on its body.
- **Wrought iron building:** fence, fence gate, door, **bracket**, and **grate** forged on the iron anvil (rods + bands for fence/gate; plates + rivets for door; band + wire + hammer + tongs for bracket; 4× wire + hammer + tongs for grate on iron or bronze anvil). **Fence:** thin centered panels that **connect to adjacent wrought iron fences only** (not gates) with post + side arms; isolated segments keep **facing** for N/S vs E/W orientation; side-arm UVs match face width (16 px broad faces, 2 px thin edges). **Fence gate fix:** open collision matches model rotation for south/east facings; gate stays a full-width panel (no fence-style connection). **Grate:** 1-pixel vertical panel (2-pixel collision); mount on any block face; N/S or E/W from placer look direction; cutout render; angled item icon.
- **Marble & limestone slabs/stairs:** stonecutter from the base stone block (2 slabs or 1 stair per block).

#### Docs

- Block pages for **balustrades**, **floor rugs**, **awnings**, and **glazed terracotta**; updated **wrought iron building**, **stone trim**, **shutters/curtains/planters**, and **roof tiles** docs for connection/corner behavior.
- Reference page for mosaic & alphabet blocks (separate mod): [`documentation/content/blocks/mosaic-and-alphabet-blocks.md`](documentation/content/blocks/mosaic-and-alphabet-blocks.md)

### 1.4 (planned)

- **Cart** — primary focus; land vehicle / hand-cart content. Maybe other vehicles or entities, but probably not.
- See release roadmap — bark uses, willow, fire-setting, airiots, cork (if not shipped in 1.3.0).

## 1.2.0

- **New plants & trees:** **esparto** (decorative grass; drops plant fiber and wheat seeds like Materia tall grass), **rice** and **cotton** crops (8 growth stages; wild variants use stage-6 art), **tea bush** (4 stages; right-click mature bush for tea leaves, regrows from stage 2), **palm**, **Mediterranean cypress**, **baobab**, **maple**, **fig**, **cedar**, and **eucalyptus** trees. All spawn in appropriate biome tags (desert/temperate esparto, river/warm-wet wild rice, tropical/grassy cotton, temperate tea, beach/tropical palms, temperate cypress, tropical baobab, temperate maple/fig/cedar, eucalyptus groves).

- **Rice:** requires a **water source within 2 blocks** to grow; mature crop drops **rice seeds** (shell in crafting to **shelled rice**); **shelled rice** is inedible raw but **cooks to cooked rice** in a boiling water pot (6 nutrition / 0.6 saturation).

- **Cotton:** white **`materia:cotton`** bolls (not taupe like wool clumps); dyeable like wool clumps; spins on **hand spindle** / **spinning wheel** to matching **colored string** (white cotton → white string); included in **`#materia:bedfills`** alongside wool clumps.

- **Palm trees:** straight trunks with **`upper`** log segment texture on the top few blocks; **flat 2D palm leaves** at the crown (diagonal trunk-adjacent leaves deferred).

- **Fig tree:** temperate worldgen; `has_figs` leaves (harvest `materia:fig`, 20% regrowth); hew `fig_log` → 4× `rough_fig_plank`; `smooth_fig_plank` item.

- **Cedar tree:** spruce-like normal tree + 2×2 mega (`cedar_mega_tree`); temperate / temperate_forest; hew → `rough_cedar_plank`.

- **Eucalyptus:** grove worldgen (3–7 trees, ~8 block radius); tall single-trunk feature for saplings; 5% rainbow log/leaves/sapling groves via separate rare modifier; hew both log types → `rough_eucalyptus_plank`.

- **Agave:** desert random-patch shrub (esparto pattern); places on sand, red sand, coarse dirt, dirt, grass.

- **Decorative plants:** **yucca** (desert + temperate clusters), **plantain** (tropical double-plant clusters), **reeds** (river + warm-wet banks; requires adjacent water), **taro** (tropical patches of semi-mature crop on grass/dirt; break for `materia:taro`, replant on farmland or wild grass). **Raw taro** is edible but applies brief Poison I; **cooked taro** (5 nutrition / 0.6 saturation) from boiling in a water pot over a campfire or baking in an oven.

- **Decorative flowers (1.2.0):** **white lily** and **bluebonnet** (two-block tall plants), **purple coneflower**, **fuchsia**, **marigold**, and **hibiscus** (single-block cross flowers), and **lotus** (cross flower on a biome-tinted lily pad). Worldgen in river, warm-wet, temperate, grassy, desert, tropical, and temperate-forest biomes as appropriate. **Lotus** places only on **shallow water** (1–2 blocks deep) via custom placement logic; marigold → yellow dye, hibiscus → pink dye.

- **Yucca / plantain rendering:** tall-plant block models now use split **lower/upper** textures so in-world and creative placement show one coherent plant instead of a smushed double texture.

- **Taro harvest:** mature crops (age 3) drop **2–4** taro (was 6–8); immature breaks drop **1** seed.

- **Flower → dye policy:** with **`materia_vanilla_overrides`** enabled, only these vanilla flowers still craft dye directly — **poppy** → pink, **dandelion** → yellow, **cornflower** → light blue, **rose bush** → pink (×2). Materia adds **indigo** → indigo dye, **marigold** → yellow, **hibiscus** → pink. Other vanilla flower→dye recipes are disabled (barrier overrides).

- **Armor rendering:** **bronze** armor uses custom **`bronze_armor_1/2`** layer textures again; **wrought iron** (`materia:iron_*`) uses vanilla **iron_layer_1/2** on all supported versions (fixes purple/black broken armor models).

- **Fruit leather:** shapeless `FFF` from `#materia:fruits` (sweet/glow berries, apple, fig); 5 nutrition / 0.6 saturation.

- **Taro wild spawn fix:** crop survives on grass without crop-block light checks; worldgen replaces short vegetation and uses surface heightmap so patches appear in shaded tropical biomes.

- **New dyes:** **verdigris** (vinegar + raw copper, custom recipe), **burgundy** (red + brown), **tan** (brown + yellow); burgundy/tan/teal textile blocks (wool, glass, concrete, terracotta, candle, cotton, string, wool clumps); teal sack/blanket/bed use verdigris dye.

- **Woodworking:** fig/cedar/eucalyptus **planks, stairs, slabs** (smooth plank + nails); **posts, trellises, and tables** for each wood type; doors/trapdoors skipped.

- **Hewing:** fig/cedar/eucalyptus/rainbow eucalyptus logs; **maple** → `rough_birch_plank` listed in JEI hewing.

- **Docs:** block pages for fig, cedar, eucalyptus, agave, decorative plants (yucca, plantain, reeds, taro), **decorative flowers**, and **roof tiles & thatch roofs**; dye pages for verdigris, burgundy, tan, and **flower→dye policy**; armor texture notes on bronze / wrought iron pages.

- **Roof tiles & thatch roofs:** **roof frame** places empty **`roof_tiles`**. **Tiled roofs:** craft 8 terracotta tiles around a frame → **`roof_tiles`**, or tile by hand. **Thatch roofs:** craft 2 bundles + frame → **`thatch_roof`** item, or bundle twice on a placed frame — both place the same **`roof_tiles`** thatch block. **`thatch_slope`** is **Thatch Stairs** only (bundles + lashing); unrelated to roof frames. **Sloped collision** on roof blocks matches walkable tile/thatch geometry (straight ramps, inner-corner valleys, outer-corner hips). **Cannonball damage:** stone cannonballs **50/50** obliterate vs partial tile loss; **iron cannonballs always obliterate** the whole block (frame included). Violent breaks drop mostly dust with at most ~**1/3** crushed ceramic; player breaks yield a mix of intact tiles, crushed ceramic, and lost material (thatch gives no bundles on violent impact). Fixed a crash when obliteration refreshed stair shapes on air.

- **Food recipes:** **baked squash** (oven, from sliced squash), **chili** (beans + peppers + salt), **beans and rice** (cooked rice + beans).

- **Plant worldgen:** tightened biome tags so warm-climate plants (eucalyptus, fig, olive, cedar, cypress, tea, etc.) no longer spawn in snowy taiga / boreal spruce forests; added `#materia:subtropical`, `#materia:prairie`, and `#materia:temperate_boreal` tags.

- **Tea:** drinking a **tea cup** restores a little hunger and grants brief **Speed I** (30 seconds).

- **Advancements:** **Rainbow Grove** (find rainbow eucalyptus) and **Full Spectrum** (collect every Materia dye).

- **Creative tabs:** Materia items now appear in the appropriate vanilla creative tabs (building blocks, food, tools, etc.) as well as the Materia tab.

- **Cannons:** **aim yaw/pitch** now **persists across world save/load** (stored on the cannon block entity). **Aiming UI:** mouse movement no longer snaps aim on the first move — fine-tuning starts from the cannon’s current angles. **Barrel model (1.20.x):** fixed mirrored horizontal rotation so the rendered barrel tracks aim direction. Sneak-right-click with an empty hand still resets aim to **0° / 0° pitch**.

- **Structure chest loot:** Materia items are **added** to many vanilla structure chests (dungeons, mineshafts, pyramids, jungle temples, strongholds, buried treasure, shipwrecks, woodland mansions, igloos) via Forge **`inject_loot_table`** global loot modifiers — vanilla loot unchanged; themed Materia bonuses (survival teasers, ores/dyes, murex/tyrian hints, sacks/blankets, etc.) at structure-specific **`random_chance`** rolls. See [`documentation/mechanics/structure-chest-loot.md`](documentation/mechanics/structure-chest-loot.md).

## 1.1.1 (2026-06-20)

- **Kiln / advanced kiln / smelting / blasting — Forge metal inputs:** Materia kiln nugget melts, wrought bloom work, wrought ingot kiln, alloy advanced-kiln inputs, steel’s raw iron, and furnace/blast paths for tin / wrought aluminum now accept **`#forge:raw_materials/*`** aligned tags (plus **`#forge:nuggets/tin`** and **`#forge:ingots/{copper,zinc}`** for bronze/brass routes). Outputs remain Materia’s items. **`#materia:kiln_compatible_raw_tins`** aliases **`#forge:raw_materials/tin`**.
- **`KilnBlockEntity`** brass/bronze slot math and zinc evaporation use the same Forge raw/ingot tags (still counting Materia copper/zinc **nugget** stacks where alloys expect them).

- **`materia:gravel_tin` (cassiterite) worldgen ~halved frequency** on all supported versions: **`count`** on placed features **2→1** (general biomes) and **4→2** (rivers), so there are roughly **half** as many generation passes per chunk; vein **size stays 18**.

- **`materia:thatch_slope` (thatched stair / roof slopes):** use a dedicated **`ThatchSlopeBlock`** with a **full-cube occlusion shape** (`Shapes.block()`) while keeping vanilla stair **collision / appearance**. This stops **rain** and **skylight** from leaking through the hollow voxels stair shapes leave on inner-corner and rotated layouts—same fix pattern as correcting “wet under a solid-looking roof.”

- **Hot metal — quench in water:** item drops tagged **`#materia:heatable_metals`** that are still warm (heated or cooling) **instantly become cold stacks** (`createCooledVersion`) when submerged in vanilla **water**, or when placed in a **water pot slot** while the pot holds water (sizzle FX).

## 1.1.0 (2026-05-13)

- **Advancements — Materia tab** (`shared/.../data/materia/advancements/`): progression tree from hammer stone through bronze, iron, steel (as `minecraft:iron_ingot` in Materia progression), kiln stacks, ferment, cannon, bows, weaving, sacks, dyes, Three Sisters crops, bottled wine/beer. Custom trigger for **Roundshot Ringer** (kill credited to a cannonball projectile). English strings under `advancements.materia.*` in `shared/.../lang/en_us.json`. Supporting **`#materia` item tags** include `#materia:spears`, `#materia:sacks_and_bindles`, and `#materia:story_purple_dyewares`.
- **Diamond-tier upgrades preserve wear:** shaped recipes from **iron-tier** bases (Materia’s steel tools use `minecraft:iron_*`) to diamond tools/armor plus **`materia:diamond_hammer`**, **`materia:diamond_spear`**, and **`materia:diamond_shovel_head`** now copy **fractional durability loss** onto the output (same ratio rule as **`FlintKnifeRecipe`**), fixing a **full-repair exploit** where JSON crafting always spawned pristine items. Implemented via **`DiamondTierUpgradeCraftHandler`** on **`PlayerEvent.ItemCraftedEvent`**; automation that performs crafting **without** that event may still output full durability until handled separately.
- **Fluid automation — water pot:** **1.19.2** exposes Forge **`IFluidHandler`** and invalidates the fluid optional correctly. **1.20.1** matches **1.21.1** for fluid/item capability layout (**fluid `LazyOptional`**, **`WaterPotFluidHandler`** where used).
- **Fluid automation — amphora (liquid mode)** (**1.20.1** and **1.21.1** only): **`ModFluids`** registers Forge fluid types/stacks for Materia ferment/cooking fluids (`wine`, `grape_juice`, `olive_oil`, `vinegar`, `beer`, `beer_mash`) so amphora liquid storage pairs with **`IFluidHandler`** for pipes/JEI-compatible **`FluidStack`** types (**`AmphoraFluidHandler`**); no fluid blocks/buckets intended. **1.19.2** stays water-pot-only on the fluid-capability front until a fuller fluid backport.
- **Optional `materia_compat_recipes` datapack** (`forge:conditional` + `forge:mod_loaded`; no compile-time dependency on other mods)
  - **Create — milling** (in `shared/`): wheat → `materia:flour`; `materia:corn` → `materia:cornmeal` (×2); chance-based `plant_fiber` → `materia:taupe_string`.
  - **Create — mixing:** `forge:flour` + water → `materia:dough`; cornmeal + water → `materia:masa_dough`; egg + sugar + flour + water + `forge:milk` → `materia:batter`. **1.18.2–1.20.1** use Create 0.5–style JSON under each port’s `.../compat/create/mixing/`. **1.21.1** uses Create 6–style JSON (`fluid_stack`, `id` results) under `1.21.1/.../compat/create/mixing/` only — mixing files are **not** in `shared/` so Gradle does not merge duplicate paths with 1.21.1 overrides.
  - **Farmer’s Delight:** cutting-board flour, cornmeal, chance-based fiber → string (knife via `forge:tools/knives`); **cutting** squash → 2× sliced squash; **cooking** corn cob → popcorn.
  - **Mekanism:** crusher 4× `plant_fiber` → `taupe_string`; `materia:rock` → 2× `materia:pebble`.
  - **Immersive Engineering:** crusher wheat/corn → flour/cornmeal; no stacked fiber input (IE format); rock → 2× pebble.
  - Compat JSON for milling and most recipes lives under **`shared/.../materia_compat_recipes/`**; earlier duplicate compat trees under version folders were removed where superseded by `shared/`. Keeping Create mixing out of `shared/` for **1.21.1** also fixes Gradle **`processResources`** duplicate-path errors vs per-port mixing files.
- **`forge:seeds`:** optional **`#pamhc2crops:seeds`** entry (`required: false`) for Pam’s packs.
- **`forge:fiber` / `forge:fibers`** (via `shared/`): include Materia **`plant_fiber`**, **`minecraft:wheat`**, and optional **`immersiveengineering:hemp_fiber`** (`required: false`) so lashings (3× **`#forge:fibers`**) cooperate with IE when present.
- **Documentation:** roadmap and compat recap (`documentation/mod-compatibility-roadmap.md`, `mod-compat-recipes.md`), **1.1** testing notes (`documentation/testing-1.1.md`, `documentation/testing-results.md`), **per-port quirks** (`documentation/reference/VERSION_DIFFERENCES.md`), **pipes/capabilities** (`documentation/fluid-pipe-compat.md`), and item/mechanics copy updates (for example **`lashing`** / **`plant-fiber`** and **`documentation/mechanics/water-pot.md`** for automation).

## 1.0.8 (2026-04-05)

- **Vanilla override pack (`materia_vanilla_overrides`) — B1b fixes**
  - **`glass_puck_smelting`:** furnace now smelts `minecraft:glass` → `materia:glass_puck` (recycles glass blocks into pucks). Replaces a broken no-op recipe.
  - **Raw iron:** removed placeholder recipes that used `minecraft:barrier` as the ingredient for smelting/blasting raw iron; vanilla `raw_iron` → `iron_ingot` recipes apply again.
  - **Meat and fish (oven):** removed seven `minecraft:`-namespace `materia:oven` recipes that output `materia:ash` for beef/chicken/cod/mutton/porkchop/rabbit/salmon. Vanilla furnace/blast smelting recipe IDs for those items are restored; the Materia oven still uses `oven_*.json` for proper cooking.
  - Removed matching legacy copies under `1.18.2/` and `1.19.2/` `data/minecraft/recipes/` for the same files.
- **Documentation:** `documentation/mod-compatibility-roadmap.md` updated to reflect B1b completion.

## 1.0.7 (2026-04-05)

- Fixed crafting sticks and smooth planks from rough cherry, mangrove, and nether-based wood planks
- Fixed orientation of upside-down thatch stairs corners
- Added missing loot tables for all custom blocks
- **Cannonball piles** now correctly drop the exact number of cannonballs stored in the pile (1–14), not a fixed 3–5 range (this was always handled in Java; the incorrect loot table that was briefly added has been removed)
- **Vanilla recipe overrides** are now bundled as built-in datapacks, registered in Forge via `AddPackFindersEvent` on all supported versions (1.18.2, 1.19.2, 1.20.1, 1.21.1):
  - All 375 `minecraft`-namespace recipe overrides live in the shared `materia_vanilla_overrides` datapack (`data/materia/datapacks/materia_vanilla_overrides/`); each port supplies a matching `pack.mcmeta` (pack format per MC version)
  - **`materia_vanilla_overrides` is required (on by default)** so progression-gated vanilla crafting applies unless you explicitly disable it (e.g. `/datapack disable "builtin/materia_vanilla_overrides"`)
  - **`materia_compat_recipes`** (Create / Farmer's Delight compat) is also registered and **optional (off by default)** — enable when those mods are present
  - Disabling either pack does not strand key Materia items: non-override recipes exist for outputs such as `materia:furnace_kiln`, `materia:blast_furnace_kiln`, and `materia:plain_cake`
- **Documentation**: `documentation/mod-compatibility-roadmap.md` updated for B1 (override pack layout + registration) and a **dupe loop audit** (B1b: no infinite loops found; a few broken or awkward recipes called out for follow-up)


## 1.0.6 (2026-03-03)

- Fixed vine crop placement/survival edge cases across versions so seed-planted **wisteria/grape/hops** crop blocks no longer remain as free-floating stage-0 plants near support structures.
- Fixed **gravel loot regression**: restored **flint** drops while keeping Materia pebble behavior, and added occasional `materia:rock` bonus drops.
- Fixed **1.18.2 startup crash** related to worldgen registry initialization by correcting configured/placed feature registry keys for that version.
- Added clearer **metalworking guidance** in docs and JEI:
  - Anvil inputs must be **hot** (including `/heatmetal` note for testing/creative).
  - Some recipes consume **multiple units** (for example, 9 ingots -> 1 block).
  - Added explicit alloy ratio guidance for **bronze** and **brass**.
- Added missing item translations in `en_us` and `nl_be` for:
  - `batter`, `bronze_boot`, `gold_boot`, `gold_backpiece`, `gold_chestpiece`, `gold_shoulder`, `unfired_amphora`.

## 1.0.5 (2026-02-17)

- Fixed multiple **dedicated server** issues in the **1.20.1** port:
  - Prevented client-only classes from being loaded on the server during cannon aiming.
  - Improved cannon interaction so aiming UI can be opened reliably via server->client packet.
- Fixed **1.21.1** menu opening crashes for several blocks (kiln/oven/etc.) by updating to the newer `openMenu(..., BlockPos)` flow.
- Fixed several **recipe parsing** problems and improved **cross-version recipe compatibility** (schema/IDs) so servers start cleanly.
- Moved disruptive **vanilla recipe overrides** into an optional built-in datapack (`materia_vanilla_overrides`) and added safer **compat recipe** helpers in an optional built-in datapack (`materia_compat_recipes`).
- Added **JEI** support for **oven** recipes (so cooking recipes like `raw_sausage -> sausage` show up).
- Added throwable **Bomb** item (grenade-like), similar to dynamite, with ignition requirement and a larger explosion than dynamite.
- Fixed an exploit: **wild vines** (wild wisteria/grapes/hops) no longer drop seeds/fiber on **right-click**; loot is obtained by **breaking** only.

## 1.0.4 (2026-02-01)

- Added initial **cheese + gut processing** content (first-phase):
  - New items: `animal_gut`, `clean_gut`, `rennet`, `cheese_curds`, `soft_cheese`, `cord`
  - New placeable blocks: `fresh_cheese_wheel`, `aged_cheese_wheel`
  - New recipes:
    - `animal_gut` + vinegar -> `clean_gut`
    - `animal_gut` + cutting tool -> `rennet`
    - milk + vinegar -> `soft_cheese`
    - milk + rennet -> `cheese_curds`
    - 4x `cheese_curds` -> `fresh_cheese_wheel`
  - Updated **drying rack**: `clean_gut` can be dried near a lit campfire into `cord`
  - Cheese wheels:
    - Wheels are **slab-height** (cake-like)
    - **Fresh** wheels are **not edible** and **age in-world** into **aged** wheels (moving the block does **not** reset aging)
    - **Aged** wheels can be sliced into 8 servings (and can also be crafted into wedges with a cutting tool)
- Added **animal gut** as occasional animal loot (killed by player):
  - Cows / pigs / sheep have a **25%** chance to drop `animal_gut`
- Added **sausage**:
  - New items: `raw_sausage`, `sausage`
  - New recipes:
    - `clean_gut` + porkchop + salt + cutting tool -> `raw_sausage`
    - Cook `raw_sausage` -> `sausage` (oven or campfire)
- Improved **cannon** feel:
  - Increased max range for fully-charged shots
  - Made partial charges more meaningfully different
  - Added slight aim randomization (iron cannonballs a bit tighter; canister/TNT a bit wider)

## 1.0.3 (2026-01-31)

- Fixed **tool harvesting** in **1.20.1** and **1.21.1** (e.g. `materia:malachite`): added missing vanilla block tags for pickaxe mining and tool tiers so blocks that require the correct tool drop properly again.
- Added missing **wood button** and **wood pressure plate** recipes, consistent with the mod’s `smooth_*_planks` + nails/tool-based progression.
- Fixed **marble harvesting**: marble now drops **calcite powder** when broken with a **hammer**, or **marble** when broken with a **pickaxe**, and it now requires **bronze-tier** tools.
- Made **obsidian** (and `materia:obsidian_slab`) mine at a reasonable speed with a **wrought iron pickaxe** or **steel hammer**.
- Fixed **1.21.1 mod-block drops**: mirrored `loot_tables/` -> `loot_table/` during the build so loot tables load on 1.21+.
- Made **vanilla pickaxe tiers** mine obsidian faster than **wrought iron** in **1.21.1** (iron/diamond/netherite now progressively faster).
- Fixed missing texture/model for **`materia:taupe_string`** and made it the default “String” (taupe), with `materia:white_string` remaining the dyed variant.

## 1.0.2 (2026-01-31)

- Fixed **rock worldgen** in **1.20.1** and **1.21.1** by ensuring the rock placed/configured features are registered and by correcting the biome modifier biome tags.
- Fixed **gravel drops** (all versions): gravel now drops **4 pebbles** (with Silk Touch still returning gravel).
- Fixed **JEI** display for the **bone knife** (the `FlintKnifeRecipe`) in **1.18.2** and **1.19.2** by providing explicit ingredients for the custom recipe and including it in JEI’s crafting recipe list.
- Added **thatch slab** (`thatch_slab`) to complete the thatch block set (thatch block + slope + slab).
- Removed redundant `clump_of_taupe_wool` naming/registration; `clump_of_wool` is the default (taupe) wool clump and tags were updated accordingly.

## 1.0.1

- Fixed **rock spawning** for the **1.20.x** and **1.21.x** ports.

## 1.0.0

- Initial release (**deprecated**).

