## Profession blocks (job-site/workstation recipe overrides)

Materia changes many “villager job-site” / workstation block recipes so they fit the mod’s progression (woodworking, fasteners, sealants, and eventually metal parts and power components).

Source of truth: `shared/src/main/resources/data/minecraft/recipes/`

Tip:

- If a recipe uses a `#materia:...` tag you don’t recognize, check the tag reference index: [Tags (reference)](../../reference/tags/README.md)
- Many of the tags on this page are defined here:
  - [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md)

## At a glance

- **Early-ish**: composter, barrel (sealants + smooth planks)
- **Mid**: loom, lectern, cartography table, fletching table
- **Late**: smithing table, stonecutter, grindstone, brewing stand, cauldron, smoker

## Recipes

### Barrel (Fisherman)

- Output: `minecraft:barrel`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/barrel.json`
- Uses:
  - [`#materia:smooth_planks`](../../reference/tags/early-crafting-and-woodworking.md#materiasmooth_planks)
  - `#materia:hard_bands`
  - `#materia:sealants`

Related tag pages:

- `#materia:hard_bands`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_bands)
- `#materia:sealants`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiasealants)

### Composter (Farmer)

- Output: `minecraft:composter`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/composter.json`
- Uses:
  - [`#materia:smooth_planks`](../../reference/tags/early-crafting-and-woodworking.md#materiasmooth_planks)
  - `#materia:sealants`

Related tag pages:

- `#materia:sealants`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiasealants)

### Loom (Shepherd)

- Output: `minecraft:loom`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/loom.json`
- Uses:
  - [`#materia:smooth_planks`](../../reference/tags/early-crafting-and-woodworking.md#materiasmooth_planks)
  - `materia:rope`
  - `materia:frame_loom`
  - `materia:box_frame`
  - `#materia:all_wood_joiners`

Related tag pages:

- `#materia:all_wood_joiners`: [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_wood_joiners)

### Lectern (Librarian)

- Output: `minecraft:lectern`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/lectern.json`
- Uses:
  - [`#materia:posts`](../../reference/tags/early-crafting-and-woodworking.md#materiaposts)
  - [`#materia:smooth_planks`](../../reference/tags/early-crafting-and-woodworking.md#materiasmooth_planks)
  - `#materia:linens`
  - `#materia:all_nails`

Related tag pages:

- `#materia:linens`: [Textiles and storage materials](../../reference/tags/textiles-and-storage.md#materialinens)
- `#materia:all_nails`: [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_nails)

### Cartography table (Cartographer)

- Output: `minecraft:cartography_table`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/cartography_table.json`
- Uses:
  - `minecraft:paper` (tag)
  - `minecraft:compass` (tag)
  - `#materia:tables`

Related tag pages:

- `#materia:tables`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiatables)

### Fletching table (Fletcher)

- Output: `minecraft:fletching_table`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/fletching_table.json`
- Uses:
  - [`#materia:smooth_planks`](../../reference/tags/early-crafting-and-woodworking.md#materiasmooth_planks)
  - `#materia:tables`
  - `materia:rope`
  - `materia:target`
  - `#materia:adhesives`

Related tag pages:

- `#materia:adhesives`: [Bindings and adhesives](../../reference/tags/bindings-and-adhesives.md#materiaadhesives)
- `#materia:tables`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiatables)

### Smithing table (Toolsmith/Weaponsmith/Armorer)

- Output: `minecraft:smithing_table`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/smithing_table.json`
- Uses:
  - `minecraft:crafting_table`
  - `#materia:hard_plates`
  - `#materia:all_rivets`
  - [`#materia:all_bores`](../../reference/tags/anvil-and-forging-tools.md#materiaall_bores)
  - [`#materia:all_chisels`](../../reference/tags/anvil-and-forging-tools.md#materiaall_chisels)
  - `#materia:bronze_hammers`

Related tag pages:

- `#materia:all_rivets`: [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_rivets)
- `#materia:bronze_hammers`: [Anvil tool tags cheat sheet](../../reference/anvil-tool-tags.md)
- `#materia:hard_plates`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_plates)

### Stonecutter (Mason)

- Output: `minecraft:stonecutter`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/stonecutter.json`
- Uses:
  - `#materia:hard_plates`
  - `#materia:all_rivets`
  - `#materia:all_sawblades`
  - `materia:dynamo`
  - `materia:battery`

Related tag pages:

- `#materia:all_sawblades`: [Tools and tool-like tags](../../reference/tags/tools-and-tool-like-tags.md#materiaall_sawblades)
- `#materia:hard_plates`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_plates)

### Grindstone (Weaponsmith/Toolsmith)

- Output: `minecraft:grindstone`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/grindstone.json`
- Uses:
  - `minecraft:stone_slab`
  - `#materia:hard_rods`
  - [`#materia:posts`](../../reference/tags/early-crafting-and-woodworking.md#materiaposts)
  - `materia:dynamo`
  - `materia:battery`

Related tag pages:

- `#materia:hard_rods`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_rods)

### Brewing stand (Cleric)

- Output: `minecraft:brewing_stand`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/brewing_stand.json`
- Uses:
  - `minecraft:blaze_rod`
  - `#materia:hard_wires`
  - `#materia:hard_bands`
  - `#materia:hard_plates`

Related tag pages:

- `#materia:hard_wires`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_wires)
- `#materia:hard_bands`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_bands)
- `#materia:hard_plates`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_plates)

### Cauldron (Leatherworker)

- Output: `minecraft:cauldron`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/cauldron.json`
- Uses:
  - `#materia:hard_plates`
  - `#materia:all_nails`
  - `#materia:hot_sealants`

Related tag pages:

- `#materia:hard_plates`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahard_plates)
- `#materia:hot_sealants`: [Metalworking and construction tags](../../reference/tags/metalworking-and-construction.md#materiahot_sealants)

### Smoker (Butcher)

Materia changes the smoker recipe to use kiln parts and upgrades:

- Output: `minecraft:smoker`
- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/smoker.json`
- Uses:
  - `materia:kiln`
  - `materia:bellows`
  - `#materia:hard_plates`
  - `#materia:hot_sealants`

Related:

- [Kiln (block)](kiln.md)
- [Bellows](bellows.md)

## “Profession blocks” that become Materia machines

Some vanilla workstation blocks are overridden to craft Materia machines instead.

### Furnace → Furnace kiln

- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/furnace.json`
- Output: `materia:furnace_kiln`
- Doc: [Furnace kiln](furnace-kiln.md)

### Blast furnace → Blast furnace kiln

- Recipe JSON: `shared/src/main/resources/data/minecraft/recipes/blast_furnace.json`
- Output: `materia:blast_furnace_kiln`
- Doc: [Blast furnace kiln](blast-furnace-kiln.md)

