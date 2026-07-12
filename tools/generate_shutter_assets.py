#!/usr/bin/env python3
"""Generate shutter block models, blockstates, item models, and recipes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"

SHUTTER_WOODS = {
    "oak": "materia:smooth_oak_planks",
    "spruce": "materia:smooth_spruce_planks",
    "birch": "materia:smooth_birch_planks",
    "jungle": "materia:smooth_jungle_planks",
    "acacia": "materia:smooth_acacia_planks",
    "dark_oak": "materia:smooth_dark_oak_planks",
    "cherry": "materia:smooth_cherry_planks",
    "mangrove": "materia:smooth_mangrove_planks",
    "rubber_wood": "materia:smooth_rubber_wood_planks",
    "fig": "materia:smooth_fig_plank",
    "cedar": "materia:smooth_cedar_plank",
    "eucalyptus": "materia:smooth_eucalyptus_plank",
}

# Default model panel sits on the south face (+Z). Blockstate Y rotation maps panel side -> model.
PANEL_Y = {"south": 0, "north": 180, "east": 270, "west": 90}
OPPOSITE = {"north": "south", "south": "north", "east": "west", "west": "east"}


def panel_side(facing: str, outset: bool) -> str:
    return facing if outset else OPPOSITE[facing]


def generate_shutter_blockstate(wood: str) -> dict:
    variants = {}
    for facing in ["north", "south", "east", "west"]:
        for open_state in ["false", "true"]:
            for outset in ["false", "true"]:
                suffix = "_open" if open_state == "true" else ""
                key = f"facing={facing},open={open_state},outset={outset}"
                panel = panel_side(facing, outset == "true")
                variants[key] = {
                    "model": f"materia:block/{wood}_shutters{suffix}",
                    "y": PANEL_Y[panel],
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


def shutter_element(from_coord, to_coord, front_uv, side_u, side_v, edge_u, edge_v):
    return {
        "from": from_coord,
        "to": to_coord,
        "faces": {
            "north": {"uv": front_uv, "texture": "#all"},
            "south": {"uv": front_uv, "texture": "#all"},
            "east": {"uv": side_u, "texture": "#all"},
            "west": {"uv": side_v, "texture": "#all"},
            "up": {"uv": edge_u, "texture": "#all"},
            "down": {"uv": edge_v, "texture": "#all"},
        },
    }


def closed_shutter_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": texture, "particle": texture},
        "elements": [
            shutter_element(
                [0, 0, 14],
                [16, 16, 16],
                [0, 0, 16, 16],
                [15, 0, 16, 16],
                [0, 0, 1, 16],
                [0, 15, 16, 16],
                [0, 0, 16, 1],
            )
        ],
    }


def open_shutter_element(from_coord, to_coord, wide_uv, thin_east_uv, thin_west_uv, edge_u, edge_v):
    """Open halves: N/S are the 2px-thick edges; E/W are the 8px-wide panel faces."""
    return {
        "from": from_coord,
        "to": to_coord,
        "faces": {
            "north": {"uv": thin_east_uv, "texture": "#all"},
            "south": {"uv": thin_west_uv, "texture": "#all"},
            "east": {"uv": wide_uv, "texture": "#all"},
            "west": {"uv": wide_uv, "texture": "#all"},
            "up": {"uv": edge_u, "texture": "#all"},
            "down": {"uv": edge_v, "texture": "#all"},
        },
    }


def open_shutter_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": texture, "particle": texture},
        "elements": [
            open_shutter_element(
                [0, 0, 8],
                [2, 16, 16],
                [0, 0, 8, 16],
                [1, 0, 2, 16],
                [0, 0, 1, 16],
                [0, 15, 8, 16],
                [0, 0, 8, 1],
            ),
            open_shutter_element(
                [14, 0, 8],
                [16, 16, 16],
                [8, 0, 16, 16],
                [14, 0, 16, 16],
                [15, 0, 16, 16],
                [8, 15, 16, 16],
                [8, 0, 16, 1],
            ),
        ],
    }


def generate_recipe(wood: str, smooth_plank: str) -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["JSJ", "PSP", "JSJ"],
        "key": {
            "J": {"tag": "materia:all_wood_joiners"},
            "S": {"item": "minecraft:stick"},
            "P": {"item": smooth_plank},
        },
        "result": {"item": f"materia:{wood}_shutters", "count": 2},
    }


def main() -> None:
    for wood, smooth_plank in SHUTTER_WOODS.items():
        texture = f"materia:block/{wood}_shutters"
        write_json(ASSETS / "models" / "block" / f"{wood}_shutters.json", closed_shutter_model(texture))
        write_json(ASSETS / "models" / "block" / f"{wood}_shutters_open.json", open_shutter_model(texture))
        write_json(ASSETS / "blockstates" / f"{wood}_shutters.json", generate_shutter_blockstate(wood))
        write_json(ASSETS / "models" / "item" / f"{wood}_shutters.json", generated_item_model(f"{wood}_shutters"))
        write_json(RECIPES / f"{wood}_shutters.json", generate_recipe(wood, smooth_plank))
        print(f"Generated {wood}_shutters")


if __name__ == "__main__":
    main()
