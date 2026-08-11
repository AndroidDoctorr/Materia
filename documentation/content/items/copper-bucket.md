## Copper bucket

<img src="../../../shared/src/main/resources/assets/materia/textures/item/copper_bucket.png" alt="Copper bucket" width="64" height="64">

The copper bucket is an **early metal bucket** — the copper-age counterpart to the vanilla iron bucket. It behaves the same in the world (water, milk, pot/amphora transfers) but keeps its own empty and filled item variants instead of turning into `minecraft:bucket`.

## Crafting

- `shared/src/main/resources/data/materia/recipes/copper_bucket.json`

Shaped recipe (same layout as the Materia **iron bucket** recipe, but with soft plates):

- `materia:copper_wire`
- `#materia:soft_plates` (includes `materia:copper_plate`)
- `#materia:sealants`

## Iron bucket (later tier)

Vanilla `minecraft:bucket` is still gated behind Materia’s **hard-plate** recipe:

- `shared/src/main/resources/data/materia/recipes/bucket.json`
  - `materia:iron_wire` + `#materia:hard_plates` + `#materia:sealants` → `minecraft:bucket`

So **copper bucket** is the first craftable bucket once you have copper plate and wire; **iron bucket** comes with wrought-iron metalworking.

## Filled variants

Copper buckets mirror vanilla and Materia bucket behavior for every liquid the mod tracks in bucket form:

| Empty / filled | Item id |
|---|---|
| Empty | `materia:copper_bucket` |
| Water | `materia:copper_bucket_water` |
| Milk | `materia:copper_bucket_milk` |
| Wine | `materia:copper_bucket_wine` |
| Beer | `materia:copper_bucket_beer` |
| Tea | `materia:copper_bucket_tea` |
| Grape juice | `materia:copper_bucket_grape_juice` |
| Vinegar | `materia:copper_bucket_vinegar` |
| Olive oil | `materia:copper_bucket_olive_oil` |

Water pots, milk pots, wine/beer/tea pots, amphorae, and verdigris crafting accept **either** vanilla iron buckets **or** the matching copper bucket variant.

## Related

- [Copper nugget](copper-nugget.md)
- [Water pot (block)](../blocks/water-pot.md)
- [Liquids and containers](../../reference/tags/liquids-and-containers.md)
