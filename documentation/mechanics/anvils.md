## Anvils

Anvils are used for shaping hot metals.

Block pages:

- [Anvils (blocks)](../content/blocks/anvils.md)
- [Stone anvil](../content/blocks/stone-anvil.md)
- [Bronze anvil](../content/blocks/bronze-anvil.md)
- [Iron anvil](../content/blocks/iron-anvil.md)

## Anvil tiers

There are multiple anvil tiers. They differ primarily in how many tools/inputs they support.

- **Stone anvil**: 1 tool slot + 1 input slot
- **Bronze anvil**: 2 tool slots + 1 input slot
- **Iron anvil**: 3 tool slots + 2 input slots

## Inputs must be hot

Anvil recipes only appear/validate when the input metal item is **hot**.

If you don’t see any recipes:

- make sure the metal is hot
- use [Tongs](../content/items/tongs.md) to move hot metal safely from a kiln/furnace onto the anvil

## Tool slots and recipe rules

Anvil recipes specify required tools using **item tags**.

Cheat sheet:

- [Anvil tool tags](../reference/anvil-tool-tags.md)
- [Anvil recipes (reference)](../reference/anvil-recipes.md)

### Stone anvil recipes

- Input: 1 metal item stack
- Tools: 1 tool tag (`tool_tag`)

Example:

- [Stone anvil: bronze ingot + any hammer → bronze hammer head](../../shared/src/main/resources/data/materia/recipes/stone_anvil/bronze_hammer_head_from_ingot.json)

### Bronze anvil recipes

- Input: 1 metal item stack
- Tools: 2 tool tags (`tool_tags`)

Rules:

- If both tags are the same, **only slot 1 needs to be filled** (one tool can satisfy both).
- If tags differ, the two tool slots can be in either order.

Example:

- [Bronze anvil: wrought iron ingot + bronze hammer → iron hammer head](../../shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_hammer_head_from_ingot.json)

### Iron anvil recipes

- Inputs: 1 required input (`input_a`) plus optional second input (`input_b`)
- Tools: 3 tool tags (`tool_tags`)

Rules:

- Inputs can be in either input slot.
- All 3 required tool tags must be present across the 3 tool slots.

Examples:

- [Iron anvil: iron rod + (hammers + chisel) → iron file](../../shared/src/main/resources/data/materia/recipes/iron_anvil/iron_file_from_rod.json)
- [Iron anvil: iron plate + (hammer + bore + tongs) → iron drawplate](../../shared/src/main/resources/data/materia/recipes/iron_anvil/iron_drawplate_from_plate.json)

## Results and cooling

Some anvil crafts automatically cool the output (small parts like wire/nails/needles/rivets/rings/scales).

Anvils also **cool items left inside them** over time (about every 2 seconds), so don’t leave hot inputs sitting in the anvil forever.

## Output behavior (important)

Anvils do **not** have an output slot.

When you click a recipe in the anvil GUI, the crafted output is delivered to you:

- prefers your **main hand** if it’s empty
- otherwise tries to go to your inventory
- otherwise drops on the ground

## Anvil durability

Anvils have durability and can break from use.

- **Stone anvil**: 50 max durability (fragile)
- **Bronze anvil**: 400 max durability
- **Iron anvil**: 800 max durability

Heavier recipes (blocks/anvils) wear anvils faster. The anvil is damaged *after* the output is granted, so you don’t lose the crafted result if the anvil breaks.

## Using tongs with anvils

Tongs can place hot metal into anvil input slot(s):

- **Right-click an anvil with tongs**: places 1 hot item
- **Shift + right-click**: places as many hot items as possible

Only items tagged `#materia:heatable_metals` will be placed (see [tag reference](../reference/tags/heat-and-hot-items.md#materiaheatable_metals)).

Related:

- [Hot metals](hot-metals.md)
- [Tongs](../content/items/tongs.md)
- [Metalworking (overview)](metalworking.md)
