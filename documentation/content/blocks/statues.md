## Statues

This page covers Materia decorative statue blocks.

## Stone statue (body + bust)

Two-block classical statues are available for six materials:

| Material | Body | Bust |
|----------|------|------|
| Marble | `materia:marble_body` | `materia:marble_bust` |
| Stone | `materia:stone_body` | `materia:stone_bust` |
| Limestone | `materia:limestone_body` | `materia:limestone_bust` |
| Sandstone | `materia:sandstone_body` | `materia:sandstone_bust` |
| Blackstone | `materia:blackstone_body` | `materia:blackstone_bust` |
| Terracotta | `materia:terracotta_body` | `materia:terracotta_bust` |

Each bust uses a material-specific `{material}_face` texture on the carved head; the body uses the base stone texture.

Crafting:

- Both parts are made from the matching base block via **stonecutting** (`{material}_body_from_{material}_stonecutting.json`, `{material}_bust_from_{material}_stonecutting.json`).

Placement / facing:

- The body faces away from the player when placed.
- If you place a bust **on top of** any statue body, the bust automatically matches the body’s facing (so the statue stays aligned).

Inventory icons render the 3D block model at a 45° angle (same as marble originally).

Asset generator: `tools/generate_statue_assets.py`

## Aquila Aurea (metal statue)

The Aquila Aurea is a decorative block, but it’s in the **metalworking** progression rather than stoneworking.

See:

- [Aquila Aurea](aquila-aurea.md)

## Related

- [Marble](marble.md)
- [Metal finials](metal-finials.md)
