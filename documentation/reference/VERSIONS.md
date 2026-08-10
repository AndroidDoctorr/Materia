## Minecraft versions & documentation

This repository contains multiple Minecraft-version subprojects (e.g. `1.20.1/`, `1.21.1/`).

Each supported Minecraft version ships as its **own jar**. Install the jar that matches your game version exactly.

## Supported Minecraft & Forge ranges

Declared in each port’s `gradle.properties` (expanded into `META-INF/mods.toml` at build time). **Build / dev targets** are the Forge versions we compile against in CI and local dev.

| Minecraft | Forge range (declared) | Built / tested against |
|-----------|------------------------|-------------------------|
| **1.18.2** | `[40,)` | **40.3.0** |
| **1.19.2** | `[43,)` | **43.5.2** |
| **1.20.1** | `[47,)` | **47.4.15** |
| **1.21.1** | `[0,)` * | **52.1.9** |

\* The 1.21.1 jar currently declares Forge `[0,)`, which is overly broad. See **1.21.1 practical notes** below for what we actually support.

### 1.21.1 Forge notes (1.3.6+)

- **Minimum to load:** Forge **52.1.0+** on Minecraft **1.21.1**.
- **Recommended:** Forge **52.1.2+** — Forge added `AddGuiOverlayLayersEvent` in **52.1.2** (see Forge changelog). On **52.1.0–52.1.1**, the mod loads but the **tongs hotbar item-count overlay** is skipped (tongs themselves still work).
- **Test target:** **52.1.9** (see `1.21.1/gradle.properties`).

Older 1.21.1 builds that imported `AddGuiOverlayLayersEvent` directly could crash on Forge **52.1.0** during mod loading; **1.3.6+** registers that overlay via reflection when the API is present.

## Version folders in this repo

- `1.18.2/`
- `1.19.2/`
- `1.20.1/`
- `1.21.1/`

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
