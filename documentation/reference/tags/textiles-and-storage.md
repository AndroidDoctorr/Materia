## Textiles and storage materials

This page covers tags that show up in storage + textile recipes (sacks, baskets, carpets, blankets, etc.).

## `#materia:strings`

Used by: sacks and other stitched textile recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/strings.json`
- **Includes**: `minecraft:string` plus many modded dyed string variants (ochre/red ochre/tyrian purple/etc.).

## `#materia:all_strings`

Used by: recipes that accept “any string-like thing”, including lashings.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_strings.json`
- **Includes**: `#materia:strings` and `materia:lashing`

## `#materia:all_needles`

Used by: sacks and other stitched textile recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_needles.json`
- **Includes**: `materia:iron_needle`, `materia:brass_needle`, `materia:steel_needle`, `materia:bronze_needle`, `materia:copper_needle`, `materia:bone_needle`

## `#materia:wickers`

Used by: basket crafting.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/wickers.json`
- **Includes**: `minecraft:sugar_cane`, `materia:lashing`

## `#materia:linens`

Used by: poultices and other “bandage / cloth” style recipes.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/linens.json`
- **Includes (currently)**: `#materia:carpets`

Practical meaning:

- Any item that counts as a carpet can serve as a “linen” in these recipes.

## `#materia:linen_fibers`

Used by: recipes that accept “fiber that can become cloth”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/linen_fibers.json`
- **Includes (currently)**: `materia:lashing`, `minecraft:string`

## `#materia:clumps_of_wool`

Used by: wool processing (clumps → yarn/cloth chains, depending on the recipe).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/clumps_of_wool.json`
- **Includes**: all clump-of-wool variants (vanilla colors + modded colors).

## Bowstrings

These tags are used by the **bow** and **crossbow** recipe overrides, and by the **composite bow**.

## `#materia:strong_bowstrings`

Used by: crossbow and composite bow crafting.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/strong_bowstrings.json`
- **Includes**: `#materia:strings` plus metal wires (bronze / wrought iron / refined iron)

## `#materia:all_bowstrings`

Used by: bow crafting.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_bowstrings.json`
- **Includes**: `materia:lashing` plus `#materia:strong_bowstrings`
