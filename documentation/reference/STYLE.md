## Documentation style guide

These notes are for contributing to **`documentation/`** (public-facing). They intentionally stay short—prefer consistent patterns over long rules.

### Voice

- Aim for plain, precise English: what the player does, what unlocks next, failure modes (“why won’t my metal go on the anvil?”).
- Tag names (`#materia:basic_axes`) belong in monospace; item ids in monospace when quoting JSON.

### Linking & “sources of truth”

- Prefer a **recipe link** (`shared/src/main/resources/data/materia/recipes/...`) or **tag JSON** path when correctness matters.
- For “anything in this category” ingredient lists, cite the **[tag reference](tags/README.md)** page instead of duplicating every variant.
- Mechanics pages should cross-link relevant **blocks**, **items**, and **recipe** hubs.

### Version differences

- If behavior differs between **1.18.2**, **1.19.2**, **1.20.1**, and **1.21.1**, add a short **Version notes** callout or link **[Version differences](VERSION_DIFFERENCES.md)**—don’t bury the caveat in prose only.

### Screenshots & screenshots layout

See **[Screenshot checklist](SCREENSHOTS.md)** ( filenames, folders under `documentation/assets/screenshots/` ).

One-line guideline: screenshots are optional everywhere except where the checklist marks **high leverage** UI or multi-step flow.

### Housekeeping audits

See **[Docs gaps & audit checklist](GAPS.md)**.
