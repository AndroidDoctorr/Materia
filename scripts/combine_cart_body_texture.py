#!/usr/bin/env python3
"""Build the 49x32 oak_cart entity atlas from 16x16 block tiles.

Layout (49x32 — 48px art + 1px wrap gutter):

    Top row (y=0..15):
      cols 0-15, 16-31: oak planks (hull / sides / front wrap)
      cols 32-47: back wall (planks + tools)
      col 48: duplicate of col 47 (prevents u=48 wrapping to column 0)

    Bottom row (y=16..31):
      cols 0-47: plain planks in all three slots (UV wrap margin)
      col 48: duplicate of col 47

Model UV anchors (CartBodyModel, atlas 49x32):
  - hull / sides / draft / floor: texOffs(0, 16)
  - front wall face:              texOffs(6, 16)
  - back wall face:                 texOffs(32, 0)

Usage:
  python scripts/combine_cart_body_texture.py oak

Output (generated variant):
  shared/.../textures/entity/cart_body_{wood}.png

Hand-authored oak reference:
  shared/.../textures/entity/oak_cart.png
"""

from __future__ import annotations

import argparse
from pathlib import Path

try:
    from PIL import Image
except ImportError as exc:
    raise SystemExit(
        "Pillow is required: pip install pillow"
    ) from exc

REPO_ROOT = Path(__file__).resolve().parents[1]
BLOCK_DIR = REPO_ROOT / "shared/src/main/resources/assets/materia/textures/block"
ENTITY_DIR = REPO_ROOT / "shared/src/main/resources/assets/materia/textures/entity"
TILE = 16
TEX_W = TILE * 3 + 2
TEX_H = TILE * 2
HULL_V = TILE
FRONT_U = 6
BACK_U = TILE


def default_sources(wood: str) -> tuple[Path, Path]:
    if wood == "oak":
        return (
            BLOCK_DIR / "cart.png",
            BLOCK_DIR / "cart_back.png",
        )
    return (
        BLOCK_DIR / f"cart_{wood}.png",
        BLOCK_DIR / f"cart_{wood}_back.png",
    )


def build_atlas(plank: Image.Image, back: Image.Image) -> Image.Image:
    atlas = Image.new("RGBA", (TEX_W, TEX_H), (0, 0, 0, 0))
    top = Image.new("RGBA", (TILE * 3, TILE), (0, 0, 0, 0))
    top.paste(plank, (0, 0))
    top.paste(plank, (TILE, 0))
    top.paste(back, (TILE * 2, 0))
    atlas.paste(top, (0, 0))
    for y in range(TILE):
        atlas.putpixel((TEX_W - 2, y), top.getpixel((TILE * 3 - 1, y)))
        atlas.putpixel((TEX_W - 1, y), top.getpixel((TILE * 3 - 1, y)))
    for ox in range(0, TILE * 3, TILE):
        atlas.paste(plank, (ox, TILE))
    for y in range(TILE, TEX_H):
        atlas.putpixel((TEX_W - 2, y), atlas.getpixel((TILE * 3 - 1, y)))
        atlas.putpixel((TEX_W - 1, y), atlas.getpixel((TILE * 3 - 1, y)))
    return atlas


def combine(plank_path: Path, back_path: Path, out: Path) -> None:
    plank = Image.open(plank_path).convert("RGBA")
    back = Image.open(back_path).convert("RGBA")
    for image, path, name in (
        (plank, plank_path, "plank"),
        (back, back_path, "back"),
    ):
        if image.size != (TILE, TILE):
            raise ValueError(f"{name} must be {TILE}x{TILE}, got {image.size} ({path})")
    atlas = build_atlas(plank, back)
    out.parent.mkdir(parents=True, exist_ok=True)
    atlas.save(out)
    print(f"Wrote {out} ({atlas.size[0]}x{atlas.size[1]})")
    print("Model UV anchors:")
    print(f"  hull / sides / draft: texOffs(0, {HULL_V})")
    print(f"  front wall face:      texOffs({FRONT_U}, {HULL_V})")
    print(f"  back wall face:       texOffs({BACK_U}, 0)")


def main() -> None:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("wood", help="Wood id for output name, e.g. oak or birch")
    parser.add_argument("--plank", type=Path, help="16x16 plank tile")
    parser.add_argument("--back", type=Path, help="16x16 back wall tile")
    parser.add_argument(
        "--out",
        type=Path,
        help="Output PNG (default: textures/entity/cart_body_{wood}.png)",
    )
    args = parser.parse_args()

    if args.plank or args.back:
        if not (args.plank and args.back):
            parser.error("Pass both --plank and --back together")
        sources = (args.plank, args.back)
    else:
        sources = default_sources(args.wood)

    for path in sources:
        if not path.is_file():
            raise SystemExit(f"Missing source texture: {path}")

    out = args.out or (ENTITY_DIR / f"cart_body_{args.wood}.png")
    combine(*sources, out)


if __name__ == "__main__":
    main()
