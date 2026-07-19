#!/usr/bin/env python3
"""Generate blockstates, models, loot, and iron-anvil recipes for wrought iron fence, gate, and door."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes" / "iron_anvil"
MODELS = ASSETS / "models" / "block"

PREFIX = "wrought_iron"
FENCE_TEX = "wrought_iron_fence"
GATE_TEX = "wrought_iron_fence_gate"
DOOR_LOWER = "wrought_iron_door_lower"
DOOR_UPPER = "wrought_iron_door_upper"

HAMMER_TONGS = ["materia:iron_hammers", "materia:iron_tongs", "materia:iron_tongs"]

DOOR_SUFFIXES = [
    "bottom_left",
    "bottom_left_open",
    "bottom_right",
    "bottom_right_open",
    "top_left",
    "top_left_open",
    "top_right",
    "top_right_open",
]

FACING_Y = {"north": 0, "south": 180, "east": 90, "west": 270}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def copy_replace(src: Path, dst: Path, replacements: dict[str, str]) -> None:
    text = src.read_text(encoding="utf-8")
    for old, new in replacements.items():
        text = text.replace(old, new)
    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_text(text, encoding="utf-8")


def fence_faces() -> dict:
    """Full-width panel: broad N/S faces, thin E/W edges."""
    return {
        "north": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "south": {"uv": [0, 0, 16, 16], "texture": "#all"},
        "east": {"uv": [7, 0, 9, 16], "texture": "#all"},
        "west": {"uv": [7, 0, 9, 16], "texture": "#all"},
    }


def fence_thin_faces() -> dict:
    """2px-wide geometry on every face (center post)."""
    strip = [7, 0, 9, 16]
    return {
        "north": {"uv": strip, "texture": "#all"},
        "south": {"uv": strip, "texture": "#all"},
        "east": {"uv": strip, "texture": "#all"},
        "west": {"uv": strip, "texture": "#all"},
    }


def fence_side_faces() -> dict:
    """Connection arm: broad E/W sides, thin N/S end caps."""
    wide = [0, 0, 16, 16]
    thin = [7, 0, 9, 16]
    return {
        "north": {"uv": thin, "texture": "#all"},
        "south": {"uv": thin, "texture": "#all"},
        "east": {"uv": wide, "texture": "#all"},
        "west": {"uv": wide, "texture": "#all"},
    }


def thin_panel_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [{"from": [0, 0, 7], "to": [16, 16, 9], "faces": fence_faces()}],
    }


def thin_post_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [{"from": [7, 0, 7], "to": [9, 16, 9], "faces": fence_thin_faces()}],
    }


def thin_side_model(texture: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [{"from": [7, 0, 0], "to": [9, 16, 8], "faces": fence_side_faces()}],
    }


def fence_multipart_blockstate(block_id: str) -> dict:
    model = f"materia:block/{block_id}"
    side = f"materia:block/{block_id}_side"
    post = f"materia:block/{block_id}_post"
    parts = []
    for facing, y in FACING_Y.items():
        parts.append(
            {
                "when": {
                    "north": False,
                    "south": False,
                    "east": False,
                    "west": False,
                    "facing": facing,
                },
                "apply": {"model": model, "y": y},
            }
        )
    parts.extend(
        [
            {
                "when": {
                    "OR": [
                        {"north": True},
                        {"south": True},
                        {"east": True},
                        {"west": True},
                    ]
                },
                "apply": {"model": post, "uvlock": True},
            },
            {"when": {"north": True}, "apply": {"model": side, "uvlock": True}},
            {"when": {"south": True}, "apply": {"model": side, "y": 180, "uvlock": True}},
            {"when": {"east": True}, "apply": {"model": side, "y": 90, "uvlock": True}},
            {"when": {"west": True}, "apply": {"model": side, "y": 270, "uvlock": True}},
        ]
    )
    return {"multipart": parts}


def open_gate_model(texture: str) -> dict:
    wing_a = {
        "from": [0, 0, 0],
        "to": [2, 16, 8],
        "faces": {
            "north": {"uv": [0, 0, 7, 16], "texture": "#all"},
            "south": {"uv": [0, 0, 7, 16], "texture": "#all"},
            "east": {"uv": [8, 0, 16, 16], "texture": "#all"},
            "west": {"uv": [0, 0, 8, 16], "texture": "#all"},
        },
    }
    wing_b = {
        "from": [14, 0, 0],
        "to": [16, 16, 8],
        "faces": {
            "north": {"uv": [0, 0, 2, 16], "texture": "#all"},
            "south": {"uv": [0, 0, 2, 16], "texture": "#all"},
            "east": {"uv": [8, 0, 16, 16], "texture": "#all"},
            "west": {"uv": [0, 0, 8, 16], "texture": "#all"},
        },
    }
    return {
        "ambientocclusion": False,
        "textures": {"all": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [wing_a, wing_b],
    }


def facing_blockstate(model_closed: str, model_open: str) -> dict:
    variants = {}
    for facing, y in FACING_Y.items():
        variants[f"facing={facing},open=false"] = {"model": model_closed, "y": y}
        variants[f"facing={facing},open=true"] = {"model": model_open, "y": y}
    return {"variants": variants}


def generate_fence() -> None:
    block_id = f"{PREFIX}_fence"
    write_json(MODELS / f"{block_id}.json", thin_panel_model(FENCE_TEX))
    write_json(MODELS / f"{block_id}_post.json", thin_post_model(FENCE_TEX))
    write_json(MODELS / f"{block_id}_side.json", thin_side_model(FENCE_TEX))
    write_json(ASSETS / "blockstates" / f"{block_id}.json", fence_multipart_blockstate(block_id))
    write_json(ASSETS / "models" / "item" / f"{block_id}.json", {"parent": f"materia:block/{block_id}"})
    write_json(
        LOOT / f"{block_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                    "conditions": [{"condition": "minecraft:survives_explosion"}],
                }
            ],
        },
    )


def generate_gate() -> None:
    block_id = f"{PREFIX}_fence_gate"
    write_json(MODELS / f"{block_id}.json", thin_panel_model(GATE_TEX))
    write_json(MODELS / f"{block_id}_open.json", open_gate_model(GATE_TEX))
    write_json(
        ASSETS / "blockstates" / f"{block_id}.json",
        facing_blockstate(f"materia:block/{block_id}", f"materia:block/{block_id}_open"),
    )
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{block_id}"}},
    )
    write_json(
        LOOT / f"{block_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                    "conditions": [{"condition": "minecraft:survives_explosion"}],
                }
            ],
        },
    )


def generate_door() -> None:
    block_id = f"{PREFIX}_door"
    for suffix in DOOR_SUFFIXES:
        write_json(
            MODELS / f"{block_id}_{suffix}.json",
            {
                "parent": f"minecraft:block/door_{suffix}",
                "textures": {"bottom": f"materia:block/{DOOR_LOWER}", "top": f"materia:block/{DOOR_UPPER}"},
            },
        )
    copy_replace(
        ASSETS / "blockstates" / "fig_door.json",
        ASSETS / "blockstates" / f"{block_id}.json",
        {"fig_door": block_id},
    )
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{block_id}"}},
    )
    write_json(
        LOOT / f"{block_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                    "conditions": [{"condition": "minecraft:survives_explosion"}],
                }
            ],
        },
    )


def anvil_recipe(name: str, input_a: tuple[str, int], input_b: tuple[str, int], result: str) -> None:
    write_json(
        RECIPES / f"{name}.json",
        {
            "type": "materia:iron_anvil",
            "input_a": {"item": input_a[0], "count": input_a[1]},
            "input_b": {"item": input_b[0], "count": input_b[1]},
            "tool_tags": HAMMER_TONGS,
            "result": {"item": result, "count": 1},
        },
    )


def generate_recipes() -> None:
    anvil_recipe(
        f"{PREFIX}_fence_from_rod_band",
        ("materia:iron_rod", 4),
        ("materia:iron_band", 2),
        f"materia:{PREFIX}_fence",
    )
    anvil_recipe(
        f"{PREFIX}_fence_gate_from_rod_band",
        ("materia:iron_rod", 3),
        ("materia:iron_band", 3),
        f"materia:{PREFIX}_fence_gate",
    )
    anvil_recipe(
        f"{PREFIX}_door_from_plate_rivets",
        ("materia:iron_plate", 8),
        ("materia:iron_rivets", 4),
        f"materia:{PREFIX}_door",
    )


def cleanup_obsolete() -> None:
    obsolete = [
        f"{PREFIX}_fence_inventory.json",
        f"{PREFIX}_fence_gate_wall.json",
        f"{PREFIX}_fence_gate_wall_open.json",
        f"{PREFIX}_fence_gate_open.json",
    ]
    for name in obsolete:
        path = MODELS / name
        if path.exists():
            path.unlink()
    for path in MODELS.glob(f"{PREFIX}_door_door_*.json"):
        path.unlink()


def main() -> None:
    cleanup_obsolete()
    generate_fence()
    generate_recipes()
    print("Generated wrought iron fence assets and iron-anvil recipes.")


if __name__ == "__main__":
    main()
