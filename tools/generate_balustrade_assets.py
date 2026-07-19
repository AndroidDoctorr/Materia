#!/usr/bin/env python3
"""Generate balustrade multipart models and blockstates from shared element geometry."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
BLOCKSTATES = ASSETS / "blockstates"

BALUSTRADES = [
    {
        "id": "stone_balustrade",
        "texture": "minecraft:block/stone",
    },
]


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def load_model(path: Path) -> dict:
    return json.loads(path.read_text(encoding="utf-8"))


def elements_by_name(model: dict) -> dict[str, dict]:
    return {element["name"]: element for element in model["elements"]}


def pick(model: dict, names: list[str]) -> list[dict]:
    by_name = elements_by_name(model)
    return [deepcopy(by_name[name]) for name in names]


def rename(element: dict, name: str) -> dict:
    copied = deepcopy(element)
    copied["name"] = name
    return copied


def post_element(name: str, from_coords: list[int], to_coords: list[int]) -> dict:
    return {
        "name": name,
        "from": from_coords,
        "to": to_coords,
        "faces": {
            "north": {"uv": [0, 0, 2, 9], "texture": "#all"},
            "east": {"uv": [0, 0, 2, 9], "texture": "#all"},
            "south": {"uv": [0, 0, 2, 9], "texture": "#all"},
            "west": {"uv": [0, 0, 2, 9], "texture": "#all"},
            "down": {"uv": [0, 0, 2, 9], "texture": "#all"},
        },
    }


def thick_element(name: str, from_coords: list[int], to_coords: list[int]) -> dict:
    return {
        "name": name,
        "from": from_coords,
        "to": to_coords,
        "faces": {
            "north": {"uv": [0, 0, 4, 5], "texture": "#all"},
            "east": {"uv": [0, 0, 4, 5], "texture": "#all"},
            "south": {"uv": [0, 0, 4, 5], "texture": "#all"},
            "west": {"uv": [0, 0, 4, 5], "texture": "#all"},
            "up": {"uv": [0, 0, 4, 4], "texture": "#all"},
            "down": {"uv": [0, 0, 4, 4], "texture": "#all"},
        },
    }


def top_element(name: str, from_coords: list[int], to_coords: list[int], uv: dict) -> dict:
    return {"name": name, "from": from_coords, "to": to_coords, "faces": uv}


def south_top_faces() -> dict:
    return {
        "north": {"uv": [0, 0, 6, 2], "texture": "#all"},
        "east": {"uv": [0, 0, 5, 2], "texture": "#all"},
        "south": {"uv": [0, 0, 6, 2], "texture": "#all"},
        "west": {"uv": [0, 0, 5, 2], "texture": "#all"},
        "up": {"uv": [0, 0, 6, 5], "texture": "#all"},
        "down": {"uv": [0, 0, 6, 5], "texture": "#all"},
    }


def build_models(straight: dict, corner: dict) -> dict[str, list[dict]]:
    straight_elements = pick(straight, [
        "left_post",
        "left_post_thick_part",
        "right_post",
        "right_post_thick_part",
        "top",
    ])

    corner_elements = pick(corner, [
        "left_post",
        "left_post_thick_part",
        "right_post",
        "right_post_thick_part",
        "top",
        "top_2",
    ])

    north_leg = pick(corner, ["right_post", "right_post_thick_part", "top_2"])
    for element in north_leg:
        if element["name"] == "right_post":
            element["name"] = "north_post"
        elif element["name"] == "right_post_thick_part":
            element["name"] = "north_post_thick"
        elif element["name"] == "top_2":
            element["name"] = "top_north"

    t_elements = straight_elements + north_leg

    south_leg = [
        post_element("south_post", [7, 0, 11], [9, 9, 13]),
        thick_element("south_post_thick", [6, 1, 10], [10, 6, 14]),
        top_element("top_south", [5, 9, 11], [11, 11, 16], south_top_faces()),
    ]

    cross_elements = t_elements + south_leg

    return {
        "straight": straight_elements,
        "corner": corner_elements,
        "t": t_elements,
        "cross": cross_elements,
    }


def model_for(texture: str, elements: list[dict]) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": texture, "particle": texture},
        "elements": elements,
    }


FACING_Y = {"north": 0, "south": 0, "east": 90, "west": 90}


def blockstate_for(block_id: str) -> dict:
    base = f"materia:block/{block_id}"
    parts = [
        {
            "when": {"north": True, "south": True, "east": True, "west": True},
            "apply": {"model": f"{base}_cross", "uvlock": True},
        },
        {
            "when": {"north": True, "east": True, "west": True, "south": False},
            "apply": {"model": f"{base}_t", "y": 0, "uvlock": True},
        },
        {
            "when": {"north": True, "south": True, "east": True, "west": False},
            "apply": {"model": f"{base}_t", "y": 90, "uvlock": True},
        },
        {
            "when": {"north": False, "south": True, "east": True, "west": True},
            "apply": {"model": f"{base}_t", "y": 180, "uvlock": True},
        },
        {
            "when": {"north": True, "south": True, "west": True, "east": False},
            "apply": {"model": f"{base}_t", "y": 270, "uvlock": True},
        },
        {
            "when": {"north": True, "west": True, "south": False, "east": False},
            "apply": {"model": f"{base}_corner", "y": 0, "uvlock": True},
        },
        {
            "when": {"north": True, "east": True, "south": False, "west": False},
            "apply": {"model": f"{base}_corner", "y": 90, "uvlock": True},
        },
        {
            "when": {"south": True, "east": True, "north": False, "west": False},
            "apply": {"model": f"{base}_corner", "y": 180, "uvlock": True},
        },
        {
            "when": {"south": True, "west": True, "north": False, "east": False},
            "apply": {"model": f"{base}_corner", "y": 270, "uvlock": True},
        },
        {
            "when": {"north": True, "south": True, "east": False, "west": False},
            "apply": {"model": base, "y": 90, "uvlock": True},
        },
        {
            "when": {"east": True, "west": True, "north": False, "south": False},
            "apply": {"model": base, "y": 0, "uvlock": True},
        },
        {
            "when": {"north": True, "south": False, "east": False, "west": False},
            "apply": {"model": base, "y": 90, "uvlock": True},
        },
        {
            "when": {"south": True, "north": False, "east": False, "west": False},
            "apply": {"model": base, "y": 90, "uvlock": True},
        },
        {
            "when": {"east": True, "north": False, "south": False, "west": False},
            "apply": {"model": base, "y": 0, "uvlock": True},
        },
        {
            "when": {"west": True, "north": False, "south": False, "east": False},
            "apply": {"model": base, "y": 0, "uvlock": True},
        },
    ]

    for facing, y_rot in FACING_Y.items():
        parts.append(
            {
                "when": {
                    "north": False,
                    "south": False,
                    "east": False,
                    "west": False,
                    "facing": facing,
                },
                "apply": {"model": base, "y": y_rot, "uvlock": True},
            }
        )

    return {"multipart": parts}


def generate_balustrade(entry: dict) -> None:
    block_id = entry["id"]
    texture = entry["texture"]
    straight_path = MODELS / f"{block_id}.json"
    corner_path = MODELS / f"{block_id}_corner.json"

    if not straight_path.exists() or not corner_path.exists():
        raise FileNotFoundError(f"Missing hand-authored straight/corner models for {block_id}")

    parts = build_models(load_model(straight_path), load_model(corner_path))
    write_json(straight_path, model_for(texture, parts["straight"]))
    write_json(corner_path, model_for(texture, parts["corner"]))
    write_json(MODELS / f"{block_id}_t.json", model_for(texture, parts["t"]))
    write_json(MODELS / f"{block_id}_cross.json", model_for(texture, parts["cross"]))
    write_json(BLOCKSTATES / f"{block_id}.json", blockstate_for(block_id))
    print(f"Generated {block_id} corner/t/cross models and blockstate")


def main() -> None:
    for entry in BALUSTRADES:
        generate_balustrade(entry)


if __name__ == "__main__":
    main()
