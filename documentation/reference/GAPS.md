## Docs gaps & audit checklist

Use this when you want systematic coverage—not for players.

Workflow that scales best:

1. **[Tag-driven gap hunting](tags/gap-hunting.md)** — pick a `#materia:...` tag from `shared/src/main/resources/data/materia/tags/items/`, enumerate `values`, and check each item/block for a doc page under `documentation/content/`.

2. **Mechanics parity** — for large systems (kilns, anvils, amphora), skim [Version differences](VERSION_DIFFERENCES.md) and confirm the prose page matches quirks on each Forge port.

3. **Recipes** — if JEI/player confusion keeps coming back, trace the recipe JSON in `shared/...` and ensure linked item/block docs exist.

Track outcomes here while you audit (short bullets—no guilt if you prune later):

- **Tag families to audit next:** (pick from [Tags (reference)](tags/README.md))

- **Confirmed gaps (“no page yet”)**

- **Open questions / suspected code-vs-doc mismatches**

## Related public docs

- [Tags quick reference](tags.md)
- [Docs style guide](STYLE.md)
- [Screenshot checklist](SCREENSHOTS.md)
