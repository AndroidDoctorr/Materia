## Bindings and adhesives

These tags show up in “binding” slots in recipes: lashings, glues, and other things that hold parts together.

## `#materia:adhesives`

Used by: early tool crafting, general “sticky” ingredient slots.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/adhesives.json`
- **Includes**: pitch, resin, glue, strong glue, slime ball

See also: [Glue](../../content/items/glue.md), [Chemistry → Adhesives](../../chemistry/adhesives.md)

## `#materia:strong_adhesives`

Used by: advanced bindings.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/strong_adhesives.json`
- **Includes**: resin, strong glue

## `#materia:all_glues`

Used by: recipes that want “a glue-like sticky thing” (including pitch/resin style ingredients).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/all_glues.json`
- **Includes**: glue, strong glue, resin, pitch

## `#materia:basic_lashings`

Used by: early tool crafting and textile-ish bindings.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/basic_lashings.json`
- **Includes**:
  - `materia:leather_strap`
  - `materia:lashing`
  - `#materia:strings`

See also: [Lashing](../../content/items/lashing.md), [Strings tag](textiles-and-storage.md#materiastrings)

## `#materia:basic_bindings`

Used by: early recipes that accept “any basic binding”.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/basic_bindings.json`
- **Includes**: `#materia:adhesives` + `#materia:basic_lashings`

## `#materia:advanced_bindings`

Used by: later recipes that want stronger bindings.

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/advanced_bindings.json`
- **Includes**: `#materia:strong_adhesives` + `materia:leather_strap`

## `#materia:leather_finishes`

Used by: leatherworking (finishing/sealing leather, hardening adjuncts, etc.).

- **Tag JSON**: `shared/src/main/resources/data/materia/tags/items/leather_finishes.json`
- **Includes**: animal fat, honeycomb, olive oil
