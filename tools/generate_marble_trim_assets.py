#!/usr/bin/env python3
"""Generate blockstates, models, loot, and recipes for stone cornice and bracket trim."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
MODELS = ASSETS / "models" / "block"
ITEM_TEXTURES = ASSETS / "textures" / "item"

FACINGS = ("north", "south", "east", "west")
SHAPES = ("outer_right", "inner_right", "outer_left", "inner_left")
MODEL_Y = {"north": 180, "south": 0, "east": 270, "west": 90}
INNER_Y_EXTRA = {"north": 180, "south": 0, "east": 0, "west": 0}
OUTER_Y_EXTRA = {"north": 0, "south": 180, "east": 180, "west": 0}
INNER_Y_OFFSET = 90
OUTER_Y_OFFSET = 90

TRIM_MATERIALS = (
    {"id": "marble", "base_tex": "materia:block/marble", "bracket_tex": "materia:block/marble_bracket", "ingredient": "materia:marble"},
    {"id": "limestone", "base_tex": "materia:block/limestone", "bracket_tex": "materia:block/limestone_bracket", "ingredient": "materia:limestone"},
    {"id": "stone", "base_tex": "minecraft:block/stone", "bracket_tex": "materia:block/stone_bracket", "ingredient": "minecraft:stone"},
    {"id": "sandstone", "base_tex": "minecraft:block/sandstone", "bracket_tex": "materia:block/sandstone_bracket", "ingredient": "minecraft:sandstone"},
    {"id": "blackstone", "base_tex": "minecraft:block/blackstone", "bracket_tex": "materia:block/blackstone_bracket", "ingredient": "minecraft:blackstone"},
)


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def corner_y(facing: str, left: bool, extra: dict[str, int], offset: int = 0) -> int:
    base = MODEL_Y[facing]
    y = (base + 270) % 360 if left else base
    return (y + extra[facing] + offset) % 360


def shape_corner_y(facing: str, shape: str, extra: dict[str, int], offset: int) -> int:
    left = "left" in shape
    if "inner" in shape:
        left = not left
    y = corner_y(facing, left, extra, offset)
    if shape == "outer_left" and facing == "south":
        y = (y + 180) % 360
    return y


def cornice_blockstate(material: str) -> dict:
    base = f"materia:block/{material}_cornice"
    outer = f"materia:block/{material}_cornice_corner_outer"
    inner = f"materia:block/{material}_cornice_corner_inner"
    variants = {}
    for facing in FACINGS:
        variants[f"facing={facing},shape=straight"] = {"model": base, "y": MODEL_Y[facing], "uvlock": True}
        for shape in SHAPES:
            model = inner if "inner" in shape else outer
            extra = INNER_Y_EXTRA if "inner" in shape else OUTER_Y_EXTRA
            offset = INNER_Y_OFFSET if "inner" in shape else OUTER_Y_OFFSET
            variants[f"facing={facing},shape={shape}"] = {
                "model": model,
                "y": shape_corner_y(facing, shape, extra, offset),
                "uvlock": True,
            }
    return {"variants": variants}


def bracket_blockstate(material: str) -> dict:
    model = f"materia:block/{material}_bracket"
    variants = {f"facing={facing}": {"model": model, "y": MODEL_Y[facing], "uvlock": True} for facing in FACINGS}
    return {"variants": variants}


def retexture_model(template: dict, base_tex: str, bracket_tex: str) -> dict:
    model = deepcopy(template)
    model["textures"]["all"] = base_tex
    model["textures"]["side"] = bracket_tex
    model["textures"]["particle"] = base_tex
    return model


def item_model(block_id: str) -> dict:
    if (ITEM_TEXTURES / f"{block_id}.png").exists():
        return {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{block_id}"}}
    return {"parent": f"materia:block/{block_id}"}


def standard_loot(block_id: str) -> dict:
    return {
        "type": "minecraft:block",
        "pools": [{"rolls": 1, "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}], "conditions": [{"condition": "minecraft:survives_explosion"}]}],
    }


def stonecutter_recipe(result: str, ingredient: str) -> dict:
    return {"type": "minecraft:stonecutting", "ingredient": {"item": ingredient}, "result": f"materia:{result}", "count": 1}


def generate_material(material: dict) -> None:
    material_id = material["id"]
    cornice_id = f"{material_id}_cornice"
    bracket_id = f"{material_id}_bracket"
    base_tex = material["base_tex"]
    bracket_tex = material["bracket_tex"]

    for suffix in ("cornice", "cornice_corner_inner", "cornice_corner_outer", "bracket"):
        template = json.loads((MODELS / f"marble_{suffix}.json").read_text(encoding="utf-8"))
        write_json(MODELS / f"{material_id}_{suffix}.json", retexture_model(template, base_tex, bracket_tex))

    write_json(ASSETS / "blockstates" / f"{cornice_id}.json", cornice_blockstate(material_id))
    write_json(ASSETS / "blockstates" / f"{bracket_id}.json", bracket_blockstate(material_id))
    write_json(ASSETS / "models" / "item" / f"{cornice_id}.json", item_model(cornice_id))
    write_json(ASSETS / "models" / "item" / f"{bracket_id}.json", item_model(bracket_id))
    write_json(LOOT / f"{cornice_id}.json", standard_loot(cornice_id))
    write_json(LOOT / f"{bracket_id}.json", standard_loot(bracket_id))
    write_json(RECIPES / f"{cornice_id}_from_{material_id}_stonecutting.json", stonecutter_recipe(cornice_id, material["ingredient"]))
    write_json(RECIPES / f"{bracket_id}_from_{material_id}_stonecutting.json", stonecutter_recipe(bracket_id, material["ingredient"]))


def main() -> None:
    for material in TRIM_MATERIALS:
        generate_material(material)
        print(f"Generated {material['id']}_cornice and {material['id']}_bracket")


if __name__ == "__main__":
    main()
