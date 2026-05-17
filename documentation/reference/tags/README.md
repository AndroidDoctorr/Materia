## Tags (reference)

This section is the “source of truth” for what `#materia:...` tags mean in docs.

Use it when writing recipe docs:

- Prefer **referencing tags** (ex: `#materia:strings`) over listing every accepted item.
- Link directly to the tag JSON when helpful.

## Quick workflow: writing recipes with tags

- When a recipe input accepts “a category” (any saw, any nail, any binding), name the tag:
  - write it as inline code (ex: `#materia:all_saws`)
  - link to the tag reference page where it’s defined
- If there are multiple valid items and there’s no tag yet, that’s a *design nudge*:
  - either add a tag, or keep the recipe strict on a single item until it’s worth generalizing

See also:

- [Recipes docs conventions](../../content/recipes/README.md)

## Pages

- [Mod compatibility tag bridges](mod-compatibility-tags.md)
- [Early crafting and woodworking](early-crafting-and-woodworking.md)
- [Natural materials](natural-materials.md)
- [Bindings and adhesives](bindings-and-adhesives.md)
- [Fuels](fuels.md)
- [Plants and farming](plants-and-farming.md)
- [Dyes](dyes.md)
- [Metalworking and construction](metalworking-and-construction.md)
- [Anvil and forging tools](anvil-and-forging-tools.md)
- [Combat and explosives](combat-and-explosives.md)
- [Tools and tool-like tags](tools-and-tool-like-tags.md)
- [Heat and hot items](heat-and-hot-items.md)
- [Liquids and containers](liquids-and-containers.md)
- [Textiles and storage materials](textiles-and-storage.md)
- [Tag-driven gap hunting](gap-hunting.md)

## Using tags to find doc gaps

Once the core tags are documented, tags become a checklist:

- If a tag contains items that have **no item page yet**, that’s a likely documentation gap.
- If a recipe uses a tag we haven’t documented here, that’s a likely reference gap.

Practical workflow:

- Pick a tag JSON in `shared/src/main/resources/data/materia/tags/items/`
- Skim its `values` list
- For each item:
  - confirm there’s a doc page under `documentation/content/items/` or `documentation/content/blocks/` (as appropriate)
  - if not, add it to `documentation/reference/GAPS.md` (or write a stub page)

## High-traffic tags (good “gap-hunting” seeds)

- Tools:
  - `#materia:all_saws` (posts and woodworking components)
  - `#materia:all_nails` (tables, joinery)
  - `#materia:all_wood_joiners` (joists)
- Textiles:
  - `#materia:strings`
  - `#materia:all_needles`
  - `#materia:wickers`
- Liquids:
  - `#materia:water`, `#materia:milk`, `#materia:vinegar`, `#materia:all_acids`
