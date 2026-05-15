# Release testing checklist (Materia **1.1.x**)

Use this when validating **1.1.0** (or any 1.1.x patch) before publish. Run the **Minecraft versions you ship** (1.18.2 / 1.19.2 / 1.20.1 / 1.21.1); not every line applies to every port.

## Baseline (every port)

- [x] Game **client** launches; create/load a **single-player** world without crash.
- [x] **JEI** (or equivalent) loads; open Materia item/block panels without errors.
- [ ] **Server** (optional): dedicated server starts with Materia; one player joins and loads chunks.

## Built-in datapacks

- [x] **`materia_vanilla_overrides`** is present and **enabled by default**; spot-check that a known override still applies (e.g. vanilla planks recipe gated the way you expect).
- [ ] **`materia_compat_recipes`** is **off by default**; enable with `/datapack enable "builtin/materia_compat_recipes"` (or Datapacks screen) and **reload**; no datapack errors in log.

## Water pot (all ports with fluid API)

- [ ] **Items:** insert/extract input stack from the side automation expects (hopper/pipe).
- [ ] **Fluids (1.19.2+):** fill water via pipe or bucket; level 0–3 updates block/visuals; **drain** partially works without soft-lock.
- [ ] **1.20.1 / 1.21.1:** boil a recipe that needs campfire heat; confirm output spawns and water consumption matches recipe.

## Amphora / pots (fluids)

- [ ] **1.20.1 / 1.21.1:** amphora liquid mode accepts/drains fluids from a pipe where intended; water pot still matches `documentation/fluid-pipe-compat.md` notes.

## Optional compat pack (`materia_compat_recipes` on)

Install only the mods you need for each subsection; recipes are conditional on `mod_loaded`.

## Reference mod stack (e.g. **1.18.2** + common pack)

Use this when you want a **single session** that exercises **JEI + map + the big-ticket auto-processing mods** together (same set you have been using: **Create**, **Farmer’s Delight**, **Immersive Engineering**, **Mekanism**, **JEI**, **JourneyMap**). Not every bullet is a “Materia feature”; it is a **smoke test** that nothing in the environment fights Materia’s datapacks or UIs.

- [x] **Cold start:** client launches with the full set; create/load a world; **no datapack/recipe errors** on first load (if you use `materia_compat_recipes`, enable it, then run **`/reload`** once and re-check the log).
- [x] **JEI:** open Materia items/blocks, search a few **compat** outputs (`flour`, `cornmeal`, `pebble`, etc.); recipes resolve and no red error panels for conditional chains.
- [x] **Immersive Engineering + Mekanism:** place **one** IE machine and **one** Mek unit that you care about (at minimum: run the **crusher** / **crushing** checks below); break/replace in-world if needed; **no** spam about failed conditional recipe parse on world load.
- [x] **Create + Farmer’s Delight:** run the **Create** and **FD** subsections below in the same world (order does not matter).
- [ ] **JourneyMap:** **Fullscreen map** and **minimap** update while moving; create/delete a **waypoint**; open/close the map while a Materia screen is available (chest, pot, **cannon GUI** if applicable) to catch **keybind** or **GUI** overlap issues.
- [ ] **Cannon (Materia):** see **Cannon: aim vs barrel** below — in this stack you are mainly checking that **nothing else** (camera mods, off-hand, etc.) breaks aiming; the barrel/model vs shot direction is a **Materia** issue if it is wrong.

## Cannon: aim vs barrel (regression)

You want **three** things to agree: (1) the **aiming UI / reticle** (2) the **cannonball’s trajectory** (3) the **barrel’s visual** heading and elevation. File bugs when any pair disagrees.

- [ ] **Place a cannon** on **flat ground**; use the **cannon’s GUI/aiming** to pick a **clear diagonal** (not axis-aligned) with moderate **upward pitch**; **fire** and watch where the ball goes.
- [ ] **Compare:** the **barrel** should point **in the same horizontal quadrant** and **up/down sense** as the shot (e.g. if the shot goes northeast and up, the barrel should not look like it points southwest at the same pitch).
- [ ] **Repeat** with the cannon block placed on **at least two** different **horizontal facings** (e.g. south-facing base vs west-facing) so `facing` + yaw math is covered.
- [ ] (Optional) **Multplayer:** second client or LAN host + client, same cannon, third-person view, to see if **only one side** of integrated vs remote is wrong (helps narrow client renderer vs server sync).

### Create

- [ ] **Milling:** wheat → `materia:flour`; corn → `materia:cornmeal` (×2); fiber → string (chance) shows in JEI.
- [ ] **Mixing:** dough, masa dough, batter appear and run in the **Mechanical Mixer + basin** (or equivalent).
- [ ] **1.21.1 only:** confirm **no** recipe load errors for Create mixing (uses port-specific JSON under `1.21.1/.../create/mixing/`).

### Farmer’s Delight

- [ ] Cutting board: flour/cornmeal/fiber recipes; **squash → sliced squash** (×2 with knife).
- [ ] Cooking pot: **corn cob → popcorn**.

### Mekanism / Immersive Engineering

- [ ] **Crusher:** wheat/corn → flour/cornmeal where applicable; **rock → 2× pebble** (Mek + IE).
- [ ] Mek **4× plant_fiber → string** still works.

### Tags / Pam’s (optional)

- [ ] With **Pam’s** installed, no log spam about missing `#pamhc2crops:seeds` (entry is optional).

## Progression & regressions (spot checks)

- [ ] **Wood:** hewing → rough → smooth → nailed planks chain still behaves; modded **saw** routes (Create/FD) still acceptable for your pack philosophy.
- [ ] **Kiln / furnace substitution:** `materia:furnace_kiln` path still reachable if overrides are on.
- [ ] **No recipe/datapack spam:** no duplicate-ID errors; fix pack list if both `recipes/` and `recipe/` ever conflict (mainly 1.21.1 build layout).

## Logs & polish

- [ ] No repeating **ERROR** lines on idle world load related to Materia recipes or tags.
- [ ] **Multiplayer** (optional): two clients, water pot + shared progression smoke test.

---

For broader modpack compatibility context, see [mod-compatibility-roadmap.md](mod-compatibility-roadmap.md) section **F** and [mod-compat-recipes.md](mod-compat-recipes.md).
