## Arrows (flint + metal)

This page documents Materia’s arrow progression:

- **Flint arrows** (vanilla `minecraft:arrow`, recipe override)
- **Metal arrows** (bronze / iron / steel)

## Flint arrows (vanilla `minecraft:arrow`)

Materia treats the vanilla arrow item as a “flint arrow”:

- Display name override: `item.minecraft.arrow` → “Flint Arrow” (`shared/src/main/resources/assets/materia/lang/en_us.json`)

Recipe override (source of truth):

- `shared/src/main/resources/data/minecraft/recipes/arrow.json`

It crafts `minecraft:arrow` from:

- `materia:flint_arrow_head`
- `minecraft:stick`
- 2× `materia:fletching`
- 1× any `#materia:basic_bindings` (see [tag reference](../../reference/tags/bindings-and-adhesives.md#materiabasic_bindings))

Flint arrow head:

- [Recipe JSON](../../../shared/src/main/resources/data/materia/recipes/flint_arrow_head.json)

## Metal arrows (bronze / iron / steel)

Metal arrows are distinct arrow items, meant as a “slow but worth it” upgrade path.

### Damage (1.21.1)

In the 1.21.1 code, the arrow items set the projectile base damage to:

- **Bronze arrow**: 2.5
- **Iron arrow**: 3.0
- **Steel arrow**: 3.5

These are implemented via `MetalArrowItem` (a custom `ArrowItem`) and a custom arrow entity so pickup returns the correct item.

### Crafting: arrows

All metal arrow recipes are shapeless and follow the same pattern:

- 1× `*_arrow_head`
- 1× stick
- 2× `materia:fletching`
- 1× any `#materia:basic_bindings`

Recipe JSONs:

- [Bronze arrow](../../../shared/src/main/resources/data/materia/recipes/bronze_arrow.json)
- [Iron arrow](../../../shared/src/main/resources/data/materia/recipes/iron_arrow.json)
- [Steel arrow](../../../shared/src/main/resources/data/materia/recipes/steel_arrow.json)

### Crafting: arrow heads (anvils)

Metal arrow heads are made on anvils from nuggets (example set shown below).

Bronze-tier anvil example:

- `materia:bronze_anvil` → [Bronze arrow head from bronze nugget](../../../shared/src/main/resources/data/materia/recipes/bronze_anvil/bronze_arrow_head_from_nugget.json)

Iron-tier anvil examples:

- `materia:iron_anvil` → [Steel arrow head from iron nugget](../../../shared/src/main/resources/data/materia/recipes/iron_anvil/steel_arrow_head_from_nugget.json)

Reminder:

- Anvil recipes generally require **hot metal** inputs. See [Anvils (mechanics)](../../mechanics/anvils.md).

## What fires these?

- Any bow that can fire arrows (including the vanilla bow and the [Composite bow](composite-bow.md))
- The vanilla crossbow (see [Crossbow](crossbow.md))
