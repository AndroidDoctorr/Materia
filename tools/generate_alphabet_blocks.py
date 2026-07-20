#!/usr/bin/env python3
"""Generate alphabet/character block models, blockstates, items, loot, and lang entries."""
from __future__ import annotations

import json
import math
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
DATA = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia"
TEXTURES = ASSETS / "textures" / "block" / "characters"
LANG = ASSETS / "lang" / "en_us.json"

FACE_ORDER = ["up", "down", "north", "south", "east", "west"]

# 60 characters → 10 blocks of 6 (letters, then numbers, then punctuation).
BLOCKS = [
    {
        "id": "character_block_1",
        "name": "Character Block 1",
        "glyphs": "A–F",
        "textures": ["a", "b", "c", "d", "e", "f"],
        "recipe_center": {"item": "minecraft:stick"},
    },
    {
        "id": "character_block_2",
        "name": "Character Block 2",
        "glyphs": "G–L",
        "textures": ["g", "h", "i", "j", "k", "l"],
        "recipe_center": {"item": "materia:pebble"},
    },
    {
        "id": "character_block_3",
        "name": "Character Block 3",
        "glyphs": "M–R",
        "textures": ["m", "n", "o", "p", "q", "r"],
        "recipe_center": {"item": "minecraft:iron_nugget"},
    },
    {
        "id": "character_block_4",
        "name": "Character Block 4",
        "glyphs": "S–X",
        "textures": ["s", "t", "u", "v", "w", "x"],
        "recipe_center": {"item": "materia:copper_nugget"},
    },
    {
        "id": "character_block_5",
        "name": "Character Block 5",
        "glyphs": "Y, Z, 0–3",
        "textures": ["y", "z", "0", "1", "2", "3"],
        "recipe_center": {"item": "materia:tin_nugget"},
    },
    {
        "id": "character_block_6",
        "name": "Character Block 6",
        "glyphs": "4–9",
        "textures": ["4", "5", "6", "7", "8", "9"],
        "recipe_center": {"item": "materia:bronze_nugget"},
    },
    {
        "id": "character_block_7",
        "name": "Character Block 7",
        "glyphs": "& ' * @ \\ ,",
        "textures": ["aa", "ap", "as", "at", "bs", "cm"],
        "recipe_center": {"item": "materia:wrought_iron_nugget"},
    },
    {
        "id": "character_block_8",
        "name": "Character Block 8",
        "glyphs": ": ) \" ^ $ !",
        "textures": ["cn", "cp", "cq", "ct", "ds", "ep"],
        "recipe_center": {"item": "minecraft:gold_nugget"},
    },
    {
        "id": "character_block_9",
        "name": "Character Block 9",
        "glyphs": "= / - × \" ( )",
        "textures": ["es", "fs", "ht", "ms", "oq", "op"],
        "recipe_center": {"item": "materia:slaked_lime"},
    },
    {
        "id": "character_block_10",
        "name": "Character Block 10",
        "glyphs": ". + % ? ; _",
        "textures": ["pd", "pl", "ps", "qm", "sc", "us"],
        "recipe_center": {"item": "minecraft:paper"},
    },
]

OLD_BLOCK_IDS = [
    "alphabet_block_ab",
    "alphabet_block_gl",
    "alphabet_block_mr",
    "alphabet_block_sx",
    "alphabet_block_y0",
    "alphabet_block_49",
    "alphabet_block_sym_aa",
    "alphabet_block_sym_cn",
    "alphabet_block_sym_es",
    "alphabet_block_sym_pd",
]

FACING_NAMES = ["down", "up", "north", "south", "west", "east"]
FACING_VEC = {
    "down": (0, -1, 0),
    "up": (0, 1, 0),
    "north": (0, 0, -1),
    "south": (0, 0, 1),
    "west": (-1, 0, 0),
    "east": (1, 0, 0),
}
FACE_NORMAL = {
    "up": (0, 1, 0),
    "down": (0, -1, 0),
    "north": (0, 0, -1),
    "south": (0, 0, 1),
    "east": (1, 0, 0),
    "west": (-1, 0, 0),
}
FACE_UP_TANGENT = {
    "up": (0, 0, -1),
    "down": (0, 0, 1),
    "north": (0, 1, 0),
    "south": (0, 1, 0),
    "east": (0, 1, 0),
    "west": (0, 1, 0),
}
STANDARD_LETTER_FACE = {
    "north": "north",
    "south": "south",
    "east": "east",
    "west": "west",
}
TILT_LETTER_FACE = {0: None, 1: "up", 2: "down"}

ITEM_DISPLAY = {
    "thirdperson_righthand": {
        "rotation": [0, 45, 0],
        "translation": [0, 0, 0],
        "scale": [0.5, 0.5, 0.5],
    },
    "firstperson_righthand": {
        "rotation": [0, 45, 0],
        "translation": [0, 0, 0],
        "scale": [0.5, 0.5, 0.5],
    },
    "gui": {
        "rotation": [30, 45, 0],
        "translation": [0, 0, 0],
        "scale": [0.625, 0.625, 0.625],
    },
    "ground": {
        "rotation": [0, 0, 0],
        "translation": [0, 3, 0],
        "scale": [0.25, 0.25, 0.25],
    },
    "fixed": {
        "rotation": [0, 0, 0],
        "translation": [0, 0, 0],
        "scale": [0.5, 0.5, 0.5],
    },
}


def normalize_texture_names() -> None:
    if not TEXTURES.is_dir():
        return
    for path in sorted(TEXTURES.glob("*.png")):
        lower = path.name.lower()
        if path.name == lower:
            continue
        temp = path.with_name(f"__tmp__{lower}")
        if temp.exists():
            temp.unlink()
        path.rename(temp)
        temp.rename(path.with_name(lower))


def rotate_x(vec, deg):
    x, y, z = vec
    deg %= 360
    if deg == 90:
        return (x, -z, y)
    if deg == 180:
        return (x, -y, -z)
    if deg == 270:
        return (x, z, -y)
    return (x, y, z)


def rotate_y(vec, deg):
    x, y, z = vec
    deg %= 360
    if deg == 90:
        return (z, y, -x)
    if deg == 180:
        return (-x, y, -z)
    if deg == 270:
        return (-z, y, x)
    return (x, y, z)


def apply_rot(vec, x_deg, y_deg):
    return rotate_y(rotate_x(vec, x_deg), y_deg)


def rotate_around_axis(vec, axis, deg):
    x, y, z = vec
    ax, ay, az = axis
    length = math.sqrt(ax * ax + ay * ay + az * az)
    if length == 0:
        return vec
    ax, ay, az = ax / length, ay / length, az / length
    rad = math.radians(deg)
    cos_a = math.cos(rad)
    sin_a = math.sin(rad)
    dot = x * ax + y * ay + z * az
    cx, cy, cz = ay * z - az * y, az * x - ax * z, ax * y - ay * x
    return (
        round(x * cos_a + cx * sin_a + ax * dot * (1 - cos_a), 5),
        round(y * cos_a + cy * sin_a + ay * dot * (1 - cos_a), 5),
        round(z * cos_a + cz * sin_a + az * dot * (1 - cos_a), 5),
    )


def model_face_for_tilt(outward: str, tilt: int) -> str:
    if tilt == 0:
        if outward in STANDARD_LETTER_FACE:
            return STANDARD_LETTER_FACE[outward]
        return "up" if outward == "up" else "down"
    return TILT_LETTER_FACE[tilt]


def expected_face_up(outward: str, spin: int) -> tuple[float, float, float]:
    letter_face = model_face_for_tilt(outward, 0)
    base = FACE_UP_TANGENT[letter_face]
    axis = FACING_VEC[outward]
    return rotate_around_axis(base, axis, spin * 90)


def score_rotation(outward: str, spin: int, tilt: int, x_deg: int, y_deg: int) -> float:
    target = FACING_VEC[outward]
    letter_face = model_face_for_tilt(outward, tilt)
    rotated_normal = apply_rot(FACE_NORMAL[letter_face], x_deg, y_deg)
    if rotated_normal != target:
        return -1.0

    actual_up = apply_rot(FACE_UP_TANGENT[letter_face], x_deg, y_deg)
    if outward in ("up", "down"):
        desired = rotate_y((0, 0, -1), spin * 90)
    else:
        desired = expected_face_up(outward, spin)

    return sum(a * d for a, d in zip(actual_up, desired))


def pick_rotation(outward: str, spin: int, tilt: int) -> tuple[int, int]:
    best = (-999.0, 0, 0)
    for x_deg in (0, 90, 180, 270):
        for y_deg in (0, 90, 180, 270):
            score = score_rotation(outward, spin, tilt, x_deg, y_deg)
            if score > best[0]:
                best = (score, x_deg, y_deg)
    if best[0] < 0:
        return 0, spin * 90
    return best[1], best[2]


def write_block_model(block_id: str, textures: list[str]) -> None:
    tex_map = {
        f"face_{face}": f"materia:block/characters/{name}"
        for face, name in zip(FACE_ORDER, textures)
    }
    tex_map["particle"] = "minecraft:block/white_concrete"
    faces_json = {
        face: {"texture": f"#face_{face}", "uv": [0, 0, 16, 16]} for face in FACE_ORDER
    }
    model = {
        "textures": tex_map,
        "elements": [{"from": [0, 0, 0], "to": [16, 16, 16], "faces": faces_json}],
    }
    out = ASSETS / "models" / "block" / f"{block_id}.json"
    out.write_text(json.dumps(model, indent=4) + "\n", encoding="utf-8")


def write_blockstate(block_id: str) -> None:
    variants = {}
    model_ref = f"materia:block/{block_id}"
    for facing in FACING_NAMES:
        for rotation in range(4):
            for tilt in range(3):
                x_deg, y_deg = pick_rotation(facing, rotation, tilt)
                key = f"facing={facing},rotation={rotation},tilt={tilt}"
                entry = {"model": model_ref}
                if x_deg:
                    entry["x"] = x_deg
                if y_deg:
                    entry["y"] = y_deg
                variants[key] = entry
    out = ASSETS / "blockstates" / f"{block_id}.json"
    out.write_text(json.dumps({"variants": variants}, indent=4) + "\n", encoding="utf-8")


def write_item_model(block_id: str) -> None:
    out = ASSETS / "models" / "item" / f"{block_id}.json"
    model = {
        "parent": f"materia:block/{block_id}",
        "display": ITEM_DISPLAY,
    }
    out.write_text(json.dumps(model, indent=4) + "\n", encoding="utf-8")


def write_loot(block_id: str) -> None:
    out = DATA / "loot_tables" / "blocks" / f"{block_id}.json"
    loot = {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                "conditions": [{"condition": "minecraft:survives_explosion"}],
            }
        ],
    }
    out.write_text(json.dumps(loot, indent=4) + "\n", encoding="utf-8")


def write_recipe(block_id: str, center: dict) -> None:
    out = DATA / "recipes" / f"{block_id}.json"
    recipe = {
        "type": "minecraft:crafting_shaped",
        "pattern": ["C", "M", "C"],
        "key": {
            "C": {"item": "minecraft:white_concrete"},
            "M": center,
        },
        "result": {"item": f"materia:{block_id}", "count": 2},
    }
    out.write_text(json.dumps(recipe, indent=4) + "\n", encoding="utf-8")


def remove_legacy_assets() -> None:
    for old_id in OLD_BLOCK_IDS:
        for folder, suffix in (
            (ASSETS / "blockstates", ".json"),
            (ASSETS / "models" / "block", ".json"),
            (ASSETS / "models" / "item", ".json"),
            (DATA / "loot_tables" / "blocks", ".json"),
            (DATA / "recipes", ".json"),
        ):
            path = folder / f"{old_id}{suffix}"
            if path.exists():
                path.unlink()
                print(f"Removed legacy {path.relative_to(ROOT)}")


def update_lang() -> None:
    lang = json.loads(LANG.read_text(encoding="utf-8"))
    for key in list(lang.keys()):
        if key.startswith("block.materia.alphabet_block"):
            del lang[key]
    for block in BLOCKS:
        key = f"block.materia.{block['id']}"
        lang[key] = block["name"]
    lang["item.materia.character_block.description"] = (
        "Hold Mosaic Stylus: click cycles letter on face, shift+click rotates. "
        "Sneak while placing puts top letter on the wall."
    )
    lang.pop("item.materia.alphabet_block.description", None)
    LANG.write_text(json.dumps(lang, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")


def main() -> None:
    normalize_texture_names()
    remove_legacy_assets()
    for block in BLOCKS:
        block_id = block["id"]
        write_block_model(block_id, block["textures"])
        write_blockstate(block_id)
        write_item_model(block_id)
        write_loot(block_id)
        write_recipe(block_id, block["recipe_center"])
        print(f"Generated {block_id}")
    update_lang()
    print(f"Updated lang with {len(BLOCKS)} block names")


if __name__ == "__main__":
    main()
