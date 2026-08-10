## Roof tiles & thatch roofs

Sloped roof pieces for joist-based builds: a wooden **frame**, optional **terracotta tile** covering, or **thatch** covering.

## Three separate things

| Registry id | Display name | What it is |
|---|---|---|
| `materia:roof_frame` | Roof Frame | **Item** — places an empty **`roof_tiles`** block |
| `materia:roof_tiles` | Roof Tiles | **Block + item** — terracotta tile stages on a frame |
| `materia:roof_copper` | Copper Roof | **Item** — places a **`roof_tiles`** block with full copper sheeting |
| `materia:shingle` | Shingle | **Item** — one stage of shingle covering (4 = full roof) |
| `materia:shingle_roof` | Shingle Roof | **Item** — places a fully shingled **`roof_tiles`** block |
| `materia:thatch_roof` | Thatch Roof | **Item** — places a finished **`roof_tiles`** block with full thatch |
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

- `clay_roof_tile.json` — **2 clay balls** in a row → **2 clay roof tiles** (shaped; avoids collision with **clay bowl**)
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

## Copper roofs

Uses the same **`materia:roof_tiles`** block with **`cover_type=copper`** and **`oxidation=0–3`**.

| Method | How |
|---|---|
| **Craft** | `roof_copper.json` — shapeless **`roof_frame` + `copper_plate`** → **`roof_copper`** item |
| **By hand** | Place **`roof_frame`**, right-click with **`copper_plate`** (plays vanilla **anvil use** sound) |

- Main slope uses vanilla **`copper_block` → `exposed_copper` → `weathered_copper` → `oxidized_copper`** textures.
- Corner joins use Materia **`roof_copper_corner_*`** triangle textures (four oxidation stages).
- Placed copper roofs **oxidize over time** via random tick (same chance as vanilla copper blocks).
- Break (player): **`roof_frame` + `copper_plate`**. Cannonballs always obliterate the whole piece.

## Shingle roofs

Uses **`cover_type=shingle`** with **`stage=1–4`** (one shingle per right-click stage).

| Item / recipe | Notes |
|---|---|
| **`shingle`** | `shingle.json` — 2× **`tar`** + any **`#materia:smooth_planks`** → **4× shingle** |
| | `shingle_from_pitch.json` — same with 2× **`pitch`** instead of tar |
| **`shingle_roof`** | `shingle_roof.json` — shapeless **4× shingle + roof frame** → full shingle roof item |
| **By hand** | Place **`roof_frame`**, right-click up to **four times** with **`shingle`** ( **`block.wood.scrape`** sound) |

Main slope uses **`shingles_1` → `shingles_2` → `shingles_3` → `shingles`** per stage (suffix = tile count; no suffix = full roof). Corner joins use **`roof_shingles_corner_*`** with the same numbering.

Player break drops **`roof_frame`** plus **`stage`** shingles (full roof = 4).

## Corners (90° joins)

When two **`roof_tiles`** blocks meet at a right angle, they auto-connect like stairs:

- **Inner corner** — two slopes meet with their backs together (typical L-shaped roof).
- **Outer corner** — two slopes meet at the open edge of the roof.

Corners only connect to other **`roof_tiles`** blocks (any stage or covering — a bare frame can corner with a thatched one; each block keeps its own texture). Mixed facings on adjacent blocks still stay straight until they meet at 90°.

**Awnings** use the same inner/outer corner shape rules; see [Awnings](awnings.md).

Models reuse the existing frame/tile/thatch textures with a second slope element; dedicated corner textures can refine the look later.

## Breaking & cannonballs

- Breaking **`roof_tiles`** drops a **roof frame**, plus tiles or bundles depending on coverage.
- Cannonballs strip tiles or thatch; tiles may drop **crushed ceramic**.

## Related recipes

- `thatch.json` — bundles + lashing → flat **thatch** block
