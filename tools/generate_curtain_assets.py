#!/usr/bin/env python3
"""Generate curtain block models, blockstates, item models, and recipes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"

CURTAIN_COLORS = [
    ("white", "minecraft:white_carpet"),
    ("orange", "minecraft:orange_carpet"),
    ("magenta", "minecraft:magenta_carpet"),
    ("light_blue", "minecraft:light_blue_carpet"),
    ("yellow", "minecraft:yellow_carpet"),
    ("lime", "minecraft:lime_carpet"),
    ("pink", "minecraft:pink_carpet"),
    ("gray", "minecraft:gray_carpet"),
    ("light_gray", "minecraft:light_gray_carpet"),
    ("cyan", "minecraft:cyan_carpet"),
    ("purple", "minecraft:purple_carpet"),
    ("blue", "minecraft:blue_carpet"),
    ("brown", "minecraft:brown_carpet"),
    ("green", "minecraft:green_carpet"),
    ("red", "minecraft:red_carpet"),
    ("black", "minecraft:black_carpet"),
    ("ochre", "materia:ochre_carpet"),
    ("red_ochre", "materia:red_ochre_carpet"),
    ("lavender", "materia:lavender_carpet"),
    ("indigo", "materia:indigo_carpet"),
    ("tyrian_purple", "materia:tyrian_purple_carpet"),
    ("taupe", "materia:taupe_carpet"),
    ("olive", "materia:olive_carpet"),
    ("charcoal_gray", "materia:charcoal_gray_carpet"),
    ("burgundy", "materia:burgundy_carpet"),
    ("teal", "materia:teal_carpet"),
]

PANEL_Y = {"south": 0, "north": 180, "east": 270, "west": 90}
OPPOSITE = {"north": "south", "south": "north", "east": "west", "west": "east"}


def panel_side(facing: str, outset: bool) -> str:
    return facing if outset else OPPOSITE[facing]


def curtain_transform(facing: str, outset: bool) -> dict:
    panel = panel_side(facing, outset)
    return {"y": PANEL_Y[panel]}


def generate_blockstate(color: str) -> dict:
    variants = {}
    for facing in ["north", "south", "east", "west"]:
        for open_state in ["false", "true"]:
            for outset in ["false", "true"]:
                suffix = "_open" if open_state == "true" else ""
                key = f"facing={facing},open={open_state},outset={outset}"
                variants[key] = {
                    "model": f"materia:block/{color}_curtains{suffix}",
                    **curtain_transform(facing, outset == "true"),
                }
    return {"variants": variants}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def generated_item_model(item_id: str) -> dict:
    return {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"materia:item/{item_id}"},
    }


def curtain_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": texture, "particle": texture},
        "elements": [
            {
                "from": [0, 0, 14],
                "to": [16, 16, 16],
                "faces": {
                    "north": {"uv": [0, 0, 16, 16], "texture": "#all"},
                    "south": {"uv": [0, 0, 16, 16], "texture": "#all"},
                },
            }
        ],
    }


def generate_recipe(color: str, carpet: str) -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["CSC"],
        "key": {
            "C": {"item": carpet},
            "S": {"item": "minecraft:stick"},
        },
        "result": {"item": f"materia:{color}_curtains", "count": 2},
    }


def main() -> None:
    for color, carpet in CURTAIN_COLORS:
        closed_tex = f"materia:block/{color}_curtains"
        open_tex = f"materia:block/{color}_curtains_open"
        write_json(ASSETS / "models" / "block" / f"{color}_curtains.json", curtain_model(closed_tex))
        write_json(ASSETS / "models" / "block" / f"{color}_curtains_open.json", curtain_model(open_tex))
        write_json(ASSETS / "blockstates" / f"{color}_curtains.json", generate_blockstate(color))
        write_json(ASSETS / "models" / "item" / f"{color}_curtains.json", generated_item_model(f"{color}_curtains"))
        write_json(RECIPES / f"{color}_curtains.json", generate_recipe(color, carpet))
        print(f"Generated {color}_curtains")


if __name__ == "__main__":
    main()
