#!/usr/bin/env python3
"""Generate blockstates, models, loot, recipes, and item models for statue body/bust blocks."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
MODELS = ASSETS / "models" / "block"

FACINGS = ("north", "south", "east", "west")
FACING_Y = {"north": 0, "south": 180, "east": 90, "west": 270}

BLOCK_ITEM_DISPLAY = {
    "thirdperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "firstperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "gui": {"rotation": [30, 45, 0], "translation": [0, 0, 0], "scale": [0.625, 0.625, 0.625]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 3, 0], "scale": [0.25, 0.25, 0.25]},
    "fixed": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
}

STATUE_MATERIALS = (
    {"id": "marble", "base_tex": "materia:block/marble", "face_tex": "materia:block/marble_face", "ingredient": "materia:marble"},
    {"id": "stone", "base_tex": "minecraft:block/stone", "face_tex": "materia:block/stone_face", "ingredient": "minecraft:stone"},
    {"id": "limestone", "base_tex": "materia:block/limestone", "face_tex": "materia:block/limestone_face", "ingredient": "materia:limestone"},
    {"id": "sandstone", "base_tex": "minecraft:block/sandstone", "face_tex": "materia:block/sandstone_face", "ingredient": "minecraft:sandstone"},
    {"id": "blackstone", "base_tex": "minecraft:block/blackstone", "face_tex": "materia:block/blackstone_face", "ingredient": "minecraft:blackstone"},
    {"id": "terracotta", "base_tex": "minecraft:block/terracotta", "face_tex": "materia:block/terracotta_face", "ingredient": "minecraft:terracotta"},
)


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def facing_blockstate(block_id: str) -> dict:
    variants = {}
    for facing in FACINGS:
        variant = {"model": f"materia:block/{block_id}"}
        y = FACING_Y[facing]
        if y:
            variant["y"] = y
        variants[f"facing={facing}"] = variant
    return {"variants": variants}


def retexture_bust(template: dict, base_tex: str, face_tex: str) -> dict:
    model = deepcopy(template)
    model["textures"]["all"] = base_tex
    model["textures"]["face"] = face_tex
    model["textures"]["particle"] = base_tex
    return model


def retexture_body(template: dict, base_tex: str) -> dict:
    model = deepcopy(template)
    model["textures"]["all"] = base_tex
    model["textures"]["particle"] = base_tex
    return model


def block_item_model(block_id: str) -> dict:
    return {"parent": f"materia:block/{block_id}", "display": BLOCK_ITEM_DISPLAY}


def standard_loot(block_id: str) -> dict:
    return {
        "type": "minecraft:block",
        "pools": [{"rolls": 1, "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}], "conditions": [{"condition": "minecraft:survives_explosion"}]}],
    }


def stonecutter_recipe(result: str, ingredient: str) -> dict:
    return {"type": "minecraft:stonecutting", "ingredient": {"item": ingredient}, "result": f"materia:{result}", "count": 1}


def generate_material(material: dict) -> None:
    material_id = material["id"]
    bust_id = f"{material_id}_bust"
    body_id = f"{material_id}_body"
    bust_template = json.loads((MODELS / "marble_bust.json").read_text(encoding="utf-8"))
    body_template = json.loads((MODELS / "marble_body.json").read_text(encoding="utf-8"))

    write_json(MODELS / f"{bust_id}.json", retexture_bust(bust_template, material["base_tex"], material["face_tex"]))
    write_json(MODELS / f"{body_id}.json", retexture_body(body_template, material["base_tex"]))
    write_json(ASSETS / "blockstates" / f"{bust_id}.json", facing_blockstate(bust_id))
    write_json(ASSETS / "blockstates" / f"{body_id}.json", facing_blockstate(body_id))
    write_json(ASSETS / "models" / "item" / f"{bust_id}.json", block_item_model(bust_id))
    write_json(ASSETS / "models" / "item" / f"{body_id}.json", block_item_model(body_id))
    write_json(LOOT / f"{bust_id}.json", standard_loot(bust_id))
    write_json(LOOT / f"{body_id}.json", standard_loot(body_id))
    write_json(RECIPES / f"{bust_id}_from_{material_id}_stonecutting.json", stonecutter_recipe(bust_id, material["ingredient"]))
    write_json(RECIPES / f"{body_id}_from_{material_id}_stonecutting.json", stonecutter_recipe(body_id, material["ingredient"]))


def main() -> None:
    for material in STATUE_MATERIALS:
        generate_material(material)
        print(f"Generated {material['id']}_body and {material['id']}_bust")


if __name__ == "__main__":
    main()
