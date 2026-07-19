#!/usr/bin/env python3
"""Generate blockstates, models, loot, and item models for metal finials."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes" / "iron_anvil"
ITEM_TEXTURES = ASSETS / "textures" / "item"

BLOCK_ITEM_DISPLAY = {
    "thirdperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "firstperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "gui": {"rotation": [30, 45, 0], "translation": [0, 0, 0], "scale": [0.625, 0.625, 0.625]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 3, 0], "scale": [0.25, 0.25, 0.25]},
    "fixed": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
}

ANVIL_TOOLS = ["materia:iron_hammers", "materia:iron_tongs", "materia:iron_chisels"]

METAL_INPUTS = {
    "bronze": ("materia:bronze_plate", "materia:bronze_rod"),
    "gold": ("materia:gold_plate", "materia:gold_rod"),
    "wrought_iron": ("materia:iron_plate", "materia:iron_rod"),
}

# block_id -> item texture stem when it differs
ITEM_ICON = {
    "bronze_ball_finial": "bronze_balls",
    "gold_ball_finial": "gold_ball_finial",
    "gold_spire": "gold_spire",
    "gold_acorn_finial": "gold_acorn",
    "wrought_iron_spire": "wrought_iron_spire",
    "wrought_iron_ball_finial": "wrought_iron_balls",
    "bronze_acorn_finial": "bronze_acorn",
    "wrought_iron_acorn_finial": "wrought_iron_acorn",
}

# block_id -> (lower_tex, upper_tex) under materia:block/ — wrought iron uses iron_* textures
TALL_FINIALS = (
    ("bronze_spire", "bronze_spire_lower", "bronze_spire_upper"),
    ("gold_spire", "gold_spire_lower", "gold_spire_upper"),
    ("wrought_iron_spire", "iron_spire_lower", "iron_spire_upper"),
    ("bronze_ball_finial", "bronze_ball_finial_lower", "bronze_ball_finial_upper"),
    ("gold_ball_finial", "gold_ball_finial_lower", "gold_ball_finial_upper"),
    ("wrought_iron_ball_finial", "iron_ball_finial_lower", "iron_ball_finial_upper"),
)

SINGLE_FINIALS = (
    ("bronze_acorn_finial", "bronze_acorn_finial"),
    ("gold_acorn_finial", "gold_acorn_finial"),
    ("wrought_iron_acorn_finial", "iron_acorn_finial"),
)


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def cross_model(texture: str) -> dict:
    return {"parent": "minecraft:block/cross", "textures": {"cross": f"materia:block/{texture}"}}


def tall_blockstate(block_id: str) -> dict:
    return {
        "variants": {
            "half=lower": {"model": f"materia:block/{block_id}_lower"},
            "half=upper": {"model": f"materia:block/{block_id}_upper"},
        }
    }


def item_model(block_id: str) -> dict:
    icon = ITEM_ICON.get(block_id, block_id)
    if (ITEM_TEXTURES / f"{icon}.png").exists():
        return {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{icon}"}}
    return {"parent": f"materia:block/{block_id}_lower", "display": BLOCK_ITEM_DISPLAY}


def acorn_item_model(block_id: str) -> dict:
    icon = ITEM_ICON.get(block_id, block_id)
    if (ITEM_TEXTURES / f"{icon}.png").exists():
        return {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{icon}"}}
    return {"parent": f"materia:block/{block_id}", "display": BLOCK_ITEM_DISPLAY}


def standard_loot(block_id: str) -> dict:
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                "conditions": [{"condition": "minecraft:survives_explosion"}],
            }
        ],
    }


def anvil_recipe(result: str, plate: str, rod: str, rod_count: int) -> dict:
    return {
        "type": "materia:iron_anvil",
        "input_a": {"item": plate, "count": 1},
        "input_b": {"item": rod, "count": rod_count},
        "tool_tags": ANVIL_TOOLS,
        "result": {"item": f"materia:{result}", "count": 1},
    }


def write_recipe(name: str, data: dict) -> None:
    write_json(RECIPES / f"{name}.json", data)


def generate_tall(block_id: str, lower_tex: str, upper_tex: str) -> None:
    write_json(ASSETS / "blockstates" / f"{block_id}.json", tall_blockstate(block_id))
    write_json(ASSETS / "models" / "block" / f"{block_id}_lower.json", cross_model(lower_tex))
    write_json(ASSETS / "models" / "block" / f"{block_id}_upper.json", cross_model(upper_tex))
    write_json(ASSETS / "models" / "item" / f"{block_id}.json", item_model(block_id))
    write_json(LOOT / f"{block_id}.json", standard_loot(block_id))


def generate_acorn(block_id: str, texture: str) -> None:
    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": {"": {"model": f"materia:block/{block_id}"}}})
    write_json(ASSETS / "models" / "block" / f"{block_id}.json", cross_model(texture))
    write_json(ASSETS / "models" / "item" / f"{block_id}.json", acorn_item_model(block_id))
    write_json(LOOT / f"{block_id}.json", standard_loot(block_id))


def main() -> None:
    for block_id, lower_tex, upper_tex in TALL_FINIALS:
        generate_tall(block_id, lower_tex, upper_tex)
        print(f"Generated tall finial {block_id}")
    for block_id, texture in SINGLE_FINIALS:
        generate_acorn(block_id, texture)
        print(f"Generated acorn finial {block_id}")

    for metal, (plate, rod) in METAL_INPUTS.items():
        prefix = metal if metal != "wrought_iron" else "wrought_iron"
        write_recipe(
            f"{prefix}_spire_from_plate_rod",
            anvil_recipe(f"{prefix}_spire", plate, rod, 2),
        )
        write_recipe(
            f"{prefix}_ball_finial_from_plate_rod",
            anvil_recipe(f"{prefix}_ball_finial", plate, rod, 1),
        )
        write_recipe(
            f"{prefix}_acorn_finial_from_plate_rod",
            anvil_recipe(f"{prefix}_acorn_finial", plate, rod, 1),
        )
        print(f"Generated {metal} finial anvil recipes")


if __name__ == "__main__":
    main()
