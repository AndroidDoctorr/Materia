#!/usr/bin/env python3
"""Generate blockstates, models, loot, and split block textures for rug variants."""
import json
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    Image = None

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
MODELS = ASSETS / "models" / "block"
TEXTURES = ASSETS / "textures" / "block"

RUG_COLORS = ("red", "blue", "green", "purple")
RUG_PATTERNS = ("1", "2")
RUG_PATTERN_ITEMS = ("rug_base", "rug_1_pattern", "rug_2_pattern")


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def split_block_textures(pattern: str, color: str) -> tuple[str, str]:
    """Crop a 16x32 rug texture into separate 16x16 foot/head block textures."""
    source = TEXTURES / f"rug_{pattern}_{color}.png"
    foot_id = f"rug_{pattern}_{color}_foot"
    head_id = f"rug_{pattern}_{color}_head"
    foot_path = TEXTURES / f"{foot_id}.png"
    head_path = TEXTURES / f"{head_id}.png"

    if Image is None:
        raise SystemExit("Pillow is required: pip install Pillow")

    if not source.exists():
        raise SystemExit(f"Missing rug source texture: {source}")

    img = Image.open(source).convert("RGBA")
    if img.size != (16, 32):
        raise SystemExit(f"Expected 16x32 rug texture at {source}, got {img.size}")

    img.crop((0, 0, 16, 16)).save(foot_path)
    img.crop((0, 16, 16, 32)).save(head_path)
    return head_id, foot_id


def rug_half_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"rug": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [
            {
                "from": [0, 0, 0],
                "to": [16, 1, 16],
                "faces": {
                    "up": {"uv": [0, 0, 16, 16], "texture": "#rug"},
                    "down": {"uv": [0, 0, 16, 16], "texture": "#rug", "cullface": "down"},
                    "north": {"uv": [0, 0, 16, 1], "texture": "#rug", "cullface": "north"},
                    "south": {"uv": [0, 0, 16, 1], "texture": "#rug", "cullface": "south"},
                    "west": {"uv": [0, 0, 16, 1], "texture": "#rug", "cullface": "west"},
                    "east": {"uv": [0, 0, 16, 1], "texture": "#rug", "cullface": "east"},
                },
            }
        ],
    }


def generate_rug(pattern: str, color: str) -> None:
    block_id = f"rug_{pattern}_{color}"
    foot_tex, head_tex = split_block_textures(pattern, color)
    write_json(MODELS / f"{block_id}_foot.json", rug_half_model(foot_tex))
    write_json(MODELS / f"{block_id}_head.json", rug_half_model(head_tex))
    variants = {}
    for facing, y in [("north", 0), ("south", 180), ("east", 90), ("west", 270)]:
        variants[f"facing={facing},part=foot"] = {"model": f"materia:block/{block_id}_foot", "y": y}
        variants[f"facing={facing},part=head"] = {"model": f"materia:block/{block_id}_head", "y": y}
    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": variants})
    write_json(ASSETS / "models" / "item" / f"{block_id}.json", {"parent": f"materia:block/{block_id}_foot"})
    write_json(
        LOOT / f"{block_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                    "conditions": [
                        {"condition": "minecraft:survives_explosion"},
                        {
                            "condition": "minecraft:block_state_property",
                            "block": f"materia:{block_id}",
                            "properties": {"part": "foot"},
                        },
                    ],
                }
            ],
        },
    )


def generate_items() -> None:
    for name in RUG_PATTERN_ITEMS:
        write_json(
            ASSETS / "models" / "item" / f"{name}.json",
            {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{name}"}},
        )
    for pattern in RUG_PATTERNS:
        for color in RUG_COLORS:
            write_json(
                ASSETS / "models" / "item" / f"rug_{pattern}_{color}.json",
                {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/rug_{pattern}_{color}"}},
            )


def main() -> None:
    for pattern in RUG_PATTERNS:
        for color in RUG_COLORS:
            generate_rug(pattern, color)
    generate_items()
    print("Generated rug block assets and split foot/head textures for patterns:", ", ".join(RUG_PATTERNS))


if __name__ == "__main__":
    main()
