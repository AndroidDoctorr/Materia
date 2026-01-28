## Progression (high-level guide)

This page ties Materia systems together into a high-level “what do I do next?” progression.

For exact recipes, use JEI:

- [JEI (recommended)](jei.md)

If you’re trying to find what’s missing (docs gaps or possible code gaps), keep this open too:

- [Docs gaps & audit checklist](../reference/GAPS.md)

## Core equivalencies (important)

- **1 pot/bucket = 3 cups/bottles**
  - Cups/crucibles are “bottle-sized”
  - Pots are “bucket/cauldron-sized”

See: [Liquids and containers](../reference/tags/liquids-and-containers.md)

## Eras (how the mod frames progression)

These “eras” are a documentation lens: they’re meant to describe **milestones**, not strict historical claims.

### Paleolithic (stone survival)

Defined by:

- stone/organic tools and components (rock, flint, fiber, lashings)
- minimal stations; you’re mostly unlocking *basic* crafting and gathering

What you can do now:

- Make stone + flint tools (cutting + basic mining)
- Turn logs into early components (rough planks, handles)
- Start early gathering loops (fiber → lashing; bones → bone handles)

Good entry pages:

- [Early game quickstart](../getting-started/EARLY_GAME.md)
- [Knapping](knapping.md)
- [Hewing](hewing.md)

## Stone Age quickstart (spawn → primitive crafting table)

This is the “first time playing” walkthrough. The goal is to get to the **primitive crafting table** and understand what to look for next.

If you have JEI installed (recommended), keep it open:

- search for **“primitive”**
- search for **“hewing”**
- search for **“knapped flint”**

### What to look for (early targets)

- **Surface rocks**: small `materia:rock` blocks in the world (more common in mountains/hills/badlands)
- **Gravel**: for `minecraft:flint`
- **Sugar cane**: a reliable plant fiber source (1 cane → 9 fiber)
- **Clay**: `minecraft:clay_ball` from river/lake clay patches (used to make a clay bowl → crucible)
- **Passive mobs**: cows/pigs/sheep can drop bones when killed by player (useful for a bone handle)

### Step 1) Rocks → hammer stone → hand axe

1. Break `materia:rock` blocks to get `materia:rock` items.
2. Craft a **hammer stone**:
   - `materia:rock` → `materia:hammer_stone`
3. Craft a **hand axe**:
   - `materia:hammer_stone` + `materia:rock` → `materia:hand_axe`

Notes:

- If you can’t find rock blocks, you can craft rocks from cobblestone later:
  - `minecraft:cobblestone` → 4× `materia:rock`

### Step 2) Logs → rough planks (hewing)

Use **hewing** to get rough planks:

- 1× any log (`#minecraft:logs`) + 1× basic axe (`#materia:basic_axes`) → 4× rough planks

This is an early woodworking backbone: rough planks become handles, sticks, and more.

### Step 3) Fiber → lashing

Make lashings (you will use a lot of them early):

- 3× `materia:plant_fiber` → 1× `materia:lashing`

Fast path:

- `minecraft:sugar_cane` → 9× plant fiber

### Step 4) Flint → knapped flint (your first “cutting tool”)

1. Get `minecraft:flint` (usually from gravel).
2. Knapping:
   - `minecraft:flint` + `materia:hammer_stone` → `materia:knapped_flint`

Important:

- `materia:knapped_flint` counts as a **basic cutting tool** (`#materia:basic_cutting_tools`) for recipes like handles and bone handles.

### Step 5) Handles + bone handle

Once you have rough planks and knapped flint, you can make handles:

- `#materia:rough_planks` + `#materia:basic_cutting_tools` → `materia:handle`

For a bone handle:

- `minecraft:bone` + `#materia:basic_cutting_tools` → `materia:bone_handle`

Tip:

- In Materia, “basic cutting tools” includes `materia:knapped_flint`, so you can do this before you have a knife.

### Step 6) Flint knife + stone hammer (keep them fresh)

Flint knife (custom recipe):

- 1× `materia:knapped_flint` + 1× `materia:bone_handle` + 1× (`materia:lashing` or `materia:glue`) → `materia:flint_knife`

Stone hammer:

- `materia:rock` + `materia:handle` + 2× `materia:lashing` → `materia:stone_hammer`

Two details that matter for the next milestone:

- The **primitive crafting table** recipe requires the **stone hammer** and **flint knife** to be at least **50% durability**
- The primitive crafting table recipe **consumes** its ingredients (it does *not* return tools)

### Step 7) Make a crucible (campfire-fired clay bowl)

You need a **crucible** for the primitive crafting table.

1. Make a clay bowl:
   - `minecraft:clay_ball` → `materia:clay_bowl`
2. Cook it on a **vanilla campfire**:
   - `materia:clay_bowl` → `materia:crucible` (campfire cooking)

### Step 8) Craft the primitive crafting table (milestone)

Crafting (special recipe):

- 1× `materia:rock`
- 1× `materia:stone_hammer` (≥ 50% durability)
- 1× `materia:flint_knife` (≥ 50% durability)
- 1× `materia:crucible`

Result:

- `materia:primitive_crafting_table`

What this unlocks:

- a dedicated early workstation with a **3×2** grid
- more “tool-gated” early recipes (check JEI’s **Primitive crafting** category)

### What’s next (after this milestone)

Common next steps are:

- Build a [Fire pit](fire-pit.md) for early cooking/processing
- Start containers/water handling ([Water pot](water-pot.md))
- Start early farming loops ([Crops and farming](crops.md))

### Neolithic (settlement + ceramics + processing)

Defined by:

- your first **stations** (primitive crafting, fire pit)
- **ceramics and water handling** becoming reliable (cups/crucibles/pots, amphora storage)
- “process over time” systems start to matter (boiling recipes, fermentation)

What you can do now:

- Run early processing on stations (fire pit, water pot boiling)
- Build out containers and start “chemistry-lite” crafting loops
- Start farming and preservation (drying, brining, fermentation)

Good entry pages:

- [Primitive crafting](primitive-crafting.md)
- [Fire pit](fire-pit.md)
- [Water pot](water-pot.md)
- [Amphora and liquids](amphora-and-liquids.md)

## Neolithic: stations → kilns → first metals (tin/copper) → bronze

This is the “next chapter” after you’ve built the **primitive crafting table**.

### What changes in this era

- You stop doing everything by hand.
- You start building **stations** that run recipes over time.
- You start thinking in **resource loops** (fuel → heat → output; containers → liquids; ore → nuggets → ingots).

### 1) Build the fire pit

The fire pit is made from:

- `#materia:earth_blocks` + `minecraft:campfire` + `minecraft:dirt`
  - `shared/src/main/resources/data/materia/recipes/fire_pit.json`

Why you care:

- It gives you early processing (and lots of recipes live here).
- It’s a reliable early source of **charcoal** and **ash** (which show up in other crafting like grout).

### 2) Build the kiln

The kiln is made from:

- `#materia:earth_blocks` + `minecraft:campfire`
  - `shared/src/main/resources/data/materia/recipes/kiln.json`

Why you care:

- It’s your entry point for **high heat** (metals start here).
- Some kiln outputs can be **hot**, so you should have **wood tongs** ready.

### 3) Make wood tongs (before doing metals)

Wood tongs recipe:

- `shared/src/main/resources/data/materia/recipes/wood_tongs.json`

Why you care:

- Kiln metal outputs can be hot and burn you.
- Tongs are also required for later anvil work.

### 4) Find tin and copper sources

In 1.18.2, the “starter metals” are set up to be visible and learnable:

- **Tin**: [Tin gravel](../content/blocks/gravel-tin.md) (`materia:gravel_tin`)
  - Common in rivers/beaches/shallow water deposits
- **Copper**: [Malachite](../content/blocks/malachite.md) (`materia:malachite`)
  - Drops `minecraft:raw_copper`

### 5) Kiln: raw ore → nuggets

Materia often routes early metals through **nuggets first**:

- `materia:raw_tin` → 9× `materia:tin_nugget`
  - `shared/src/main/resources/data/materia/recipes/raw_tin_to_tin_ingot.json`
- `minecraft:raw_copper` → 9× `materia:copper_nugget`
  - `shared/src/main/resources/data/materia/recipes/raw_copper_to_copper_ingot.json`

### 6) Stone anvil: nuggets → ingots (consolidation)

Once you have hot nuggets, you can consolidate them into ingots using the **stone anvil**:

- Stone anvil crafting:
  - `shared/src/main/resources/data/materia/recipes/stone_anvil.json`
- Tin ingot:
  - `shared/src/main/resources/data/materia/recipes/stone_anvil/tin_ingot_from_nuggets.json`
- Copper ingot:
  - `shared/src/main/resources/data/materia/recipes/stone_anvil/copper_ingot_from_nuggets.json`

This is a major “metalworking feels real now” milestone.

### 7) Bronze alloying (advanced kiln / chimney upgrade)

Bronze is made in an **advanced kiln** recipe that requires a **chimney** (two-input kiln):

- `materia:tin_nugget` + `minecraft:raw_copper` → `materia:bronze_ingot`
  - `shared/src/main/resources/data/materia/recipes/bronze_alloy.json`

Important note:

- The chimney itself is intentionally **easy to craft** in `shared/` (bricks + clay).
- Lime does **not** require a chimney as a hard gate:
  - There is a kiln recipe that converts `#materia:calcites` → `materia:quicklime` **without** requiring a chimney (`calcite_to_quicklime.json`).
  - Some other quicklime recipes *do* require a chimney (ex: `quicklime_from_calcite_block.json`) because they’re higher-yield.

Next steps once bronze is online:

- Bronze tools (more durable + unlock more crafting)
- Bronze-tier anvil chains (see [Anvils](anvils.md))

### Copper (starter metal) vs bronze (first forging tier)

In 1.18.2 `shared/`, copper is a **starter metal**, not a full “tool tier” on its own.

What copper is for:

- early metal familiarity (find it, heat it, consolidate it)
- a few practical parts (plates/rods/wire/needles) used by later crafting (especially electrical-ish items and some machine parts)
- bronze alloying input (raw copper is used directly in the bronze alloy recipe)

What bronze is for:

- your first **real forging tier** (bronze anvil + bronze tool kit)
- unlocking lots of “infrastructure parts” (plates/rods/wire/nails/rivets/etc.) and key milestones like the vanilla crafting table recipe

Practical takeaway:

- If you’re asking “is there a copper age?”: treat it as “starter metals online” (tin + copper), but **bronze** is the first big tier jump.

Key pages:

- [Kilns](kilns.md)
- [Anvils](anvils.md)

### Bronze age (first true forging tier)

Defined by:

- reliable **heat → handle → shape** loop (kiln + tongs + anvil tier)
- bronze tools enabling more advanced processing chains

What you can do now:

- Build a real metalworking toolkit (hammer + tongs + chisel + drawplate)
- Make “infrastructure parts” (plates, rods, wire, nails/rivets/rings) to unlock lots of recipes
- Unlock the vanilla crafting table milestone (in this mod)

Good entry pages:

- [Kilns](kilns.md)
- [Hot metals](hot-metals.md)
- [Anvils](anvils.md)
- [Tongs](../content/items/tongs.md)
- [Metalworking (overview)](metalworking.md)

### Iron age (reliable iron tooling)

Defined by:

- iron-tier tools becoming normal, not rare exceptions
- iron-tier anvil chains (more complex recipes + tool requirements)

What you can do now:

- Upgrade your workstation tier by crafting the **iron anvil**
- Start iron-anvil multi-tool chains (3 tools, sometimes 2 inputs)
- Produce iron-anvil “starter kit” parts (rod/plate/wire, better tongs)

Good entry pages:

- [Anvils](anvils.md)
- [Anvil tool tags cheat sheet](../reference/anvil-tool-tags.md)
- [Metalworking (overview)](metalworking.md)

#### Iron Age walkthrough (bronze → iron anvil)

This is the practical “you’re entering the Iron Age” route in `shared/`:

##### 1) Produce wrought iron

Wrought iron is the core input metal for the iron anvil tier.

Primary path:

- Kiln:
  - `minecraft:raw_iron` → `materia:wrought_iron_ingot`
  - `shared/src/main/resources/data/materia/recipes/wrought_iron_ingot.json`

Alternative paths:

- Smelting:
  - `shared/src/main/resources/data/materia/recipes/wrought_iron_ingot_from_smelting_raw_iron.json`
- Blasting:
  - `shared/src/main/resources/data/materia/recipes/wrought_iron_ingot_from_blasting_raw_iron.json`

Tip:

- If you’re still using early tooling, this is a good time to build a “Bronze Age kit” (hammer + tongs + chisel + drawplate). See: [Metalworking (overview)](metalworking.md)

##### 2) Make an iron hammer (bridge tool)

You need an iron-tier hammer to satisfy `#materia:iron_hammers`, which is required for crafting the iron anvil itself.

- Bronze anvil: wrought iron ingot → iron hammer head
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_hammer_head_from_ingot.json`
- Crafting: iron hammer head + bronze handle + bindings → iron hammer
  - `shared/src/main/resources/data/materia/recipes/iron_hammer.json`

See:

- [Iron hammer](../content/items/iron-hammer.md)
- [Wrought iron ingot](../content/items/wrought-iron-ingot.md)

##### 3) Make wrought iron blocks

- Bronze anvil: 9× wrought iron ingot → 1× wrought iron block
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_block_from_ingots.json`

See: [Wrought iron block](../content/items/wrought-iron-block.md)

##### 4) Craft the iron anvil (Iron Age milestone)

- Bronze anvil: 3× wrought iron block → iron anvil
  - `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_anvil_from_blocks.json`

Important:

- This recipe requires `#materia:iron_hammers` (your new iron hammer is what makes this possible).

Once you have the iron anvil, you’re “in the Iron Age” from the mod’s point of view: most higher-complexity metalworking chains become available.

##### 5) First iron-anvil parts (starter kit)

From here, the common unlock set is:

- Iron rod (input for lots of parts):
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_rod_from_ingot.json`
- Iron plate:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_plate_from_ingot.json`
- Iron tongs:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_tongs_from_rod.json`
- Iron drawplate:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_drawplate_from_plate.json`
- Iron wire:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/iron_wire_from_rod.json`

Note:

- Many iron-anvil recipes intentionally still accept some bronze-tier tools (ex: bronze tongs / bronze chisel) to smooth the transition.

### Steel age (reliable steel tooling)

Defined by:

- steel becoming a “baseline endgame material” for many tools/parts

What you can do now:

- Set up steelmaking (coal coke + high-heat kiln variant)
- Produce steel components and craft the vanilla “iron” gear set (labeled as steel)
- Reach a stable endgame tooling baseline for most of the mod

Good entry pages:

- [Kilns](kilns.md)
- [Anvils](anvils.md)
- [Metalworking (overview)](metalworking.md)
- [Heat and fuel overview](heat.md)

#### Steel Age walkthrough (iron anvil → steel tools)

In 1.18.2, Materia treats vanilla iron as **steel**:

- `minecraft:iron_ingot` is displayed as **Steel ingot**
- `minecraft:iron_nugget` is displayed as **Steel nugget**

See: [Steel ingot](../content/items/steel-ingot.md)

##### 1) Set up coke production (coal → coal coke)

Steelmaking requires **coal coke fuel**.

- Craft a [Coke oven](../content/blocks/coke-oven.md):
  - `shared/src/main/resources/data/materia/recipes/coke_oven.json`
- Run the coke oven:
  - Input: `minecraft:coal`
  - Fuel: `minecraft:coal`
  - Output: `materia:coal_coke`

See: [Coal coke](../content/items/coal-coke.md)

##### 2) Build a high-heat kiln (furnace kiln + furnace chimney) or a blast furnace kiln

Steelmaking does **not** run on a basic kiln.

Option A (furnace kiln):

- Furnace kiln (overrides vanilla furnace recipe):
  - `shared/src/main/resources/data/minecraft/recipes/furnace.json`
- Furnace chimney:
  - `shared/src/main/resources/data/materia/recipes/furnace_chimney.json`
- Place the furnace chimney **directly above** the furnace kiln.

Option B (blast furnace kiln):

- Blast furnace kiln (overrides vanilla blast furnace recipe):
  - `shared/src/main/resources/data/minecraft/recipes/blast_furnace.json`

##### 3) Smelt steel ingots (Steel Age fuel + recipe gate)

Steel ingots come from an advanced kiln “steel” recipe:

- `shared/src/main/resources/data/materia/recipes/steel_ingot.json`
  - Inputs: `minecraft:raw_iron` + `#minecraft:coals`
  - Output: `minecraft:iron_ingot` (displayed as “Steel ingot”)

Important:

- The steel recipe requires **coal coke as fuel** (`requires_coke_fuel: true`)

##### 4) Forge steel components on the iron anvil

Once you have steel ingots, the iron anvil can shape them into steel parts:

- Steel rod:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/steel_rod_from_ingot.json`
- Steel plate:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/steel_plate_from_ingot.json`
- Steel wire:
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/steel_wire_from_rod.json`

##### 5) Craft a steel hammer (top-tier hammer)

- Steel hammer head (iron anvil):
  - `shared/src/main/resources/data/materia/recipes/iron_anvil/steel_hammer_head_from_ingot.json`
- Steel hammer (crafting):
  - `shared/src/main/resources/data/materia/recipes/steel_hammer.json`

See: [Steel hammer](../content/items/steel-hammer.md)

##### 6) Steel tools and armor (vanilla “iron” set)

Most vanilla “iron” gear recipes are overridden to use steel components.

Examples:

- Steel pickaxe (vanilla iron pickaxe):
  - `shared/src/main/resources/data/minecraft/recipes/iron_pickaxe.json`
- Steel sword (vanilla iron sword):
  - `shared/src/main/resources/data/minecraft/recipes/iron_sword.json`
- Steel armor pieces (vanilla iron armor):
  - `shared/src/main/resources/data/minecraft/recipes/iron_helmet.json`
  - `shared/src/main/resources/data/minecraft/recipes/iron_chestplate.json`

At this point, you have “reliable endgame” tooling for most of the mod.

### Crafting tables as a milestone (primitive vs vanilla)

The mod already has a **Primitive crafting table** (early milestone).

- See: [Primitive crafting](primitive-crafting.md)

In 1.18.2 `shared/`, the vanilla crafting table is not the “free early” station it is in vanilla Minecraft.

Instead, Materia provides a **milestone recipe** for `minecraft:crafting_table`:

- `shared/src/main/resources/data/minecraft/recipes/crafting_table.json`

It requires:

- `materia:bronze_saw`
- `materia:bronze_hammer`
- `materia:bronze_shears`
- `materia:water_pot`
- `materia:rope`
- `materia:primitive_crafting_table`

This effectively makes “getting the vanilla crafting table” a **late Neolithic / early metalworking** milestone.

See:

- [Bronze saw](../content/items/bronze-saw.md)
- [Bronze hammer](../content/items/bronze-hammer.md)
- [Bronze shears](../content/items/bronze-shears.md)
- [Rope](../content/items/rope.md)
- [Water pot](water-pot.md)

### Iron anvil milestone (from wrought iron blocks)

In 1.18.2 `shared/`, the iron anvil is made on a **bronze anvil** from wrought iron blocks:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_anvil_from_blocks.json`
  - 3× `materia:wrought_iron_block` → 1× `materia:iron_anvil`
  - Requires `#materia:iron_hammers`

The key “bridge” step is that you can make an **iron hammer** using a bronze anvil and wrought iron:

- `shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_hammer_head_from_ingot.json`
- `shared/src/main/resources/data/materia/recipes/iron_hammer.json`

This is why “bronze age” naturally flows into “iron age” in the mod: bronze enables the tooling required to *build* the iron-tier workstation.

## Milestones

### 0) First tools (Stone Age start)

Goal: get basic cutting and woodworking online.

- Gather: [Rock](../content/items/rock.md), craft [Hammer stone](../content/items/hammer-stone.md)
- Craft: [Hand axe](../content/items/hand-axe.md)
- Make: [Rough planks](../content/items/rough-planks.md) via [Hewing](hewing.md)
- Gather: [Plant fiber](../content/items/plant-fiber.md), craft [Lashing](../content/items/lashing.md)
- Craft: [Knapped flint](../content/items/knapped-flint.md) via [Knapping](knapping.md)
- Craft: [Flint knife](../content/items/flint-knife.md), [Flint spear](../content/items/flint-spear.md)
- Craft: [Handle](../content/items/handle.md), [Bone handle](../content/items/bone-handle.md)
- Craft: [Stone hammer](../content/items/stone-hammer.md)

Quickstart version: [Early game quickstart](../getting-started/EARLY_GAME.md)

### 1) First stations

Goal: unlock basic processing and early crafting chains.

- Build: [Fire pit](fire-pit.md)
- Build: [Primitive crafting table](primitive-crafting.md)

Common “next” unlocks:

- **Charcoal and early processing** (fire pit chains)
- **Tool-gated crafting** (primitive crafting)

### 2) Ceramics and water handling (quality-of-life + chemistry foundation)

Goal: get containers and water transfer working; this becomes the backbone for later chemistry/liquids.

- **Pot / water pot**
  - See: [Water pot](water-pot.md)
- **Amphora**
  - See: [Amphora and liquids](amphora-and-liquids.md)

Why it matters:

- Boiling recipes (glue, hardened leather, paper pulp, etc.)
- Liquid transfer standards (cups/bottles vs pots/buckets)

### 3) Fermentation (grape juice → vinegar / wine)

Goal: introduce “time-based” processing and useful liquids.

- See: [Amphora and liquids](amphora-and-liquids.md) → fermentation section

Key rule:

- **Lid** → vinegar (~20 min)
- **Sealed lid** → wine (~60 min)

### 4) Storage and textiles (logistics + crafting breadth)

Goal: increase carrying capacity and open stitched/dyed outputs.

- Portable storage: [Sacks](../content/items/sacks.md)
- Block storage: [Basket](../content/blocks/basket.md)

Tag references that commonly appear in recipes:

- [Textiles and storage materials](../reference/tags/textiles-and-storage.md)
- [Bindings and adhesives](../reference/tags/bindings-and-adhesives.md)

### 5) Heat, kilns, and forging (metalworking loop)

Goal: establish the “heat → handle safely → shape on anvil” loop.

- Safety/system rules: [Hot metals](hot-metals.md)
- Handling: [Tongs](../content/items/tongs.md)
- Heating: [Kilns](kilns.md)
- Shaping: [Anvils](anvils.md)

This is where progression often bottlenecks if any of these are missing:

- A reliable heat source / fuel chain
- A safe way to move hot outputs (tongs)
- Clear anvil recipe requirements per tier

### 6) Chemistry and dyes (optional specialization, big content surface)

Goal: unlock dye families, mixing, and dyed material outputs.

- Overview: [Chemistry](../chemistry/README.md)
- Dyes: [Dyes](../chemistry/dyes.md)
- Dyes cheat sheet: [Dye cheat sheet](../chemistry/dyes/cheat-sheet.md)

## Using this page to find gaps/bottlenecks

When something feels “stuck”, it’s usually one of:

- A required **station** isn’t available early enough (or isn’t documented clearly)
- A required **tool tag** isn’t obvious (ex: “any cutting tool” vs “basic cutting tool”)
- A required **container variant** is missing (ex: vinegar/lime equivalents)

Tags are your friend for audits:

- [Tags (reference)](../reference/tags/README.md)

