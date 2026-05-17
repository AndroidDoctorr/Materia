## Minecraft versions & documentation

This repository contains multiple Minecraft-version subprojects (e.g. `1.20.1/`, `1.21.1/`).

## Version folders in this repo

- `1.18.2/`
- `1.19.2/`
- `1.20.1/`
- `1.21.1/` (in progress)

## Recommended approach

- **Gameplay docs live in `documentation/`** and describe behavior “conceptually”.
- When behavior differs by Minecraft version:
  - add a short **Version notes** section to the page, or
  - split into `documentation/versions/1.21.1/...` only if it’s truly different.

## Linking to source of truth

Where possible, link to the actual data files:

- Recipes: `shared/src/main/resources/data/materia/recipes/...`
- Tags: `shared/src/main/resources/data/materia/tags/...`
- Loot tables: `shared/src/main/resources/data/materia/loot_tables/...`

## Cheat sheets

- `documentation/reference/CHEATSHEETS.md`
