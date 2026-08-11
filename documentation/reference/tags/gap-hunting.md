## Tag-driven gap hunting

Tags are your best “coverage tool” for docs, because they encode *sets* of items that recipes depend on.

This page is a workflow for using tag JSON as a checklist.

## Workflow

### 1) Pick a tag family

Good starting points (highly reused):

- **Tools**: `#materia:all_saws`, `#materia:all_knives`, `#materia:all_tongs`
- **Woodworking**: `#materia:all_nails`, `#materia:all_wood_joiners`
- **Textiles**: `#materia:strings`, `#materia:all_needles`, `#materia:wickers`
- **Liquids**: `#materia:water`, `#materia:milk`, `#materia:vinegar`, `#materia:all_acids`

### 2) Open the tag JSON

Tag JSON lives in:

- `shared/src/main/resources/data/materia/tags/items/`

Each tag has a `values` list that may contain:

- direct items (prefer `#materia:all_saws` over `"materia:bronze_saw"`)
- other tags (`"#materia:all_nails"`)
- optional items (objects with `"required": false`) for cross-version compatibility

### 3) Turn the values into a checklist

For each item in the tag:

- **Docs check**: does it have a page in `documentation/content/items/` or `documentation/content/blocks/`?
- **Behavior check** (optional): is the recipe behavior intuitive in-game?

### 4) Record the outcome

- If it’s missing docs: add a stub page or add an entry to `documentation/reference/GAPS.md`
- If it’s missing code/behavior: add it to “Open questions / likely code gaps” in `GAPS.md`

## Practical example

If you open:

- `shared/src/main/resources/data/materia/tags/items/all_saws.json`

…you can immediately ask:

- “Do I have a page for each saw tier?”
- “Do my woodworking recipes consistently say ‘any `#materia:all_saws`’ and link to the tag?”
