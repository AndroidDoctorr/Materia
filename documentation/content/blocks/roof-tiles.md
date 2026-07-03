## Roof tiles & thatch roofs

Sloped roof pieces for joist-based builds: a wooden **frame**, optional **terracotta tile** covering, or **thatch** covering.

## Three separate things

| Registry id | Display name | What it is |
|---|---|---|
| `materia:roof_frame` | Roof Frame | **Item** — places an empty **`roof_tiles`** block |
| `materia:roof_tiles` | Roof Tiles | **Block + item** — frame roof with tile stages (or craft pre-tiled) |
| `materia:thatch_roof` | Thatch Roof | **Item** — places a finished **`roof_tiles`** block with full thatch (same block as manual bundle application) |
| `materia:thatch_slope` | Thatch Stairs | **Block** — vanilla **stair** geometry; unrelated to the roof-frame system |

The internal id `thatch_slope` is legacy naming for the stair block only. Player-facing text uses **Thatch Stairs** and **Thatch Roof**.

## Crafting a roof frame

- `shared/src/main/resources/data/materia/recipes/roof_frame.json`  
  Smooth planks + wood frame + wood joiner → **roof frame** item.

Right-click a solid face (typically atop joists) with a **roof frame** to place the **`roof_tiles`** block at **stage 0** (empty frame).

## Tiled roof — two methods

### 1. Craft a finished tiled roof

- `shared/src/main/resources/data/materia/recipes/roof_tiles.json`  
  Eight **terracotta roof tiles** around a **roof frame** → **`roof_tiles`** item (places fully tiled).

### 2. Tile a frame by hand

1. Place a **roof frame**.
2. Right-click with **terracotta roof tiles** up to eight times (pottery **scrape** sound).

### Tile pipeline

- `clay_roof_tile.json` — clay ball → **clay roof tile**
- `clay_roof_tile_to_terracotta.json` — kiln → **terracotta roof tile**

## Thatch roof — two methods

Both use the **`roof_tiles`** block (not Thatch Stairs).

| Method | How |
|---|---|
| **Craft in one go** | `roof_thatch.json` — **2× bundle** + **roof frame** → **`thatch_roof`** item (2×2 inventory craft) |
| **Apply on a placed frame** | Place **roof frame**, right-click **twice** with a **bundle** — first click **`roof_thatch_1`**, second plain **thatch** texture |

## Thatch Stairs (separate)

- `thatch_slope.json` — **3× bundle** + **lashing** → **`thatch_slope`** block (**Thatch Stairs**)
- For free-form stair/corner building only; not part of the roof-frame flow.

## Breaking & cannonballs

- Breaking **`roof_tiles`** drops a **roof frame**, plus tiles or bundles depending on coverage.
- Cannonballs strip tiles or thatch; tiles may drop **crushed ceramic**.

## Related recipes

- `thatch.json` — bundles + lashing → flat **thatch** block
