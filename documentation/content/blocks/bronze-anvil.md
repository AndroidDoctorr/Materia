## Bronze anvil

The bronze anvil is the **second anvil tier**. It adds a second tool slot and enables more complex recipes (especially “processing” chains like wire / rivets / small parts).

See also: [Anvils (mechanic)](../../mechanics/anvils.md)

## Obtaining

The main progression route is crafting it on a stone anvil:

- [Stone anvil: 3× bronze blocks → bronze anvil](../../../shared/src/main/resources/data/materia/recipes/stone_anvil/bronze_anvil_from_blocks.json)

There are also bronze-anvil-tier recipes that output another bronze anvil (useful for “make one once you already have one” / recovery paths):

- [Bronze anvil: 3× bronze blocks → bronze anvil](../../../shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_anvil_from_blocks.json)

## How it works

- **Slots**: 2 tool slots + 1 hot input slot
- **Required input**: the metal input must be **hot**, or no recipes will appear
- **Tool rules**:
  - some recipes require *two different tools* (any order)
  - if both required tool tags are the same, **one tool in slot 1 is enough**

## Durability (bronze anvils break eventually)

- **Max durability**: 400
- **Damage rules (roughly)**:
  - most recipes are minimal wear
  - making an iron anvil is heavier wear

## Cooling

Items left in the anvil will cool over time (every ~2 seconds).

## Upgrade recipe (iron anvil)

The bronze anvil is the gateway to the iron anvil:

- [Bronze anvil: 3× wrought iron blocks → iron anvil](../../../shared/src/main/resources/data/materia/recipes/bronze_anvil/iron_anvil_from_blocks.json)

## Screenshots

Add screenshots here when helpful:

- `documentation/assets/screenshots/anvils/bronze/`
