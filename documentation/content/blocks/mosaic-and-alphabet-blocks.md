## Mosaic & character blocks (experimental)

1.20.1 prototype decorative signage.

## Mosaic block

A terracotta tile with **one lime-washed canvas face** (white concrete). The other five faces are plain terracotta.

See [mosaic rules](#mosaic-block-1) below — painting, pickup, crafting, and whitewash.

## Character blocks

Ten blocks × six faces = **60 characters** (A–Z, 0–9, basic punctuation). Search in JEI/creative as **Character Block 1** … **Character Block 10**.

| Block | Glyphs (up → down → N → S → E → W) |
| --- | --- |
| `character_block_1` | A B C D E F |
| `character_block_2` | G H I J K L |
| `character_block_3` | M N O P Q R |
| `character_block_4` | S T U V W X |
| `character_block_5` | Y Z 0 1 2 3 |
| `character_block_6` | 4 5 6 7 8 9 |
| `character_block_7` | & ' * @ \\ , |
| `character_block_8` | : ) " ^ $ ! |
| `character_block_9` | = / - × " ( ) |
| `character_block_10` | . + % ? ; _ |

### Crafting (all blocks)

Same shaped pattern, different center ingredient — **2 blocks per craft**:

```
C
M
C
```

- **C** = `minecraft:white_concrete`
- **M** = center ingredient (below)

| Block | Center (M) | Progression |
| --- | --- | --- |
| Character Block 1 | stick | early wood |
| Character Block 2 | pebble | stone age |
| Character Block 3 | iron nugget | vanilla iron |
| Character Block 4 | copper nugget | copper age |
| Character Block 5 | tin nugget | bronze precursors |
| Character Block 6 | bronze nugget | bronze age |
| Character Block 7 | wrought iron nugget | iron working |
| Character Block 8 | gold nugget | precious metal |
| Character Block 9 | slaked lime | ties to mosaic whitewash |
| Character Block 10 | paper | printing / ink |

Recipes: `shared/data/materia/recipes/character_block_{1–10}.json`

### Orientation (requires Mosaic Stylus in hand)

| Action | Effect |
| --- | --- |
| **Normal place** | Standard layout (wall letters on walls) |
| **Sneak + place** | Top face letter on the outward wall face |
| **Right-click** (stylus held) | Cycle tilt: standard → top letter on face → bottom letter on face |
| **Shift + right-click** (stylus held) | Rotate glyphs (4 steps on floor/ceiling; upright/upside-down flip on walls) |

Without a stylus, right-click does nothing.

For arbitrary text, use **painted mosaic tiles** in rows; character blocks are a convenience shortcut.

## Mosaic block (detail)

### Placement

The **canvas faces you** when the block is placed.

### Painting

| Action | Effect |
| --- | --- |
| **Mosaic Stylus** on canvas face | Cycle pixel color forward |
| **Shift + stylus** on canvas face | Cycle pixel color backward |
| **Slaked lime** on canvas face | Clear all paint (blank, stackable); consumes 1 slaked lime |

### Picking up painted designs

| Tool | Result |
| --- | --- |
| **Any pickaxe** | Drops mosaic **with** canvas data |
| **Chisel + hammer** (offhand) | Same |
| **Other tools / bare hand** | Drops **blank** mosaic |

Painted mosaics store canvas data in item NBT. **Only identical designs stack.**

### Crafting

| Recipe | Ingredients | Notes |
| --- | --- | --- |
| **Blank mosaic** | terracotta + slaked lime + mosaic stylus | Shapeless; stylus survives |
| **Copy design** | blank mosaic + painted mosaic | Template kept; banner-style |

### Mosaic stylus

Shapeless: stick + smooth plank + CMYKW dyes. Durability 128. Required to craft mosaic blocks and to orient character blocks.

## Asset generator

`tools/generate_alphabet_blocks.py` — models, blockstates, loot, lang, and recipes for all ten character blocks.

## Related

- [Floor rugs](floor-rugs.md) (banner-style pattern copy in crafting)
