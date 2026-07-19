#!/usr/bin/env python3
"""Generate awning block models, blockstates, item models, recipes, and lang entries."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"
MODELS = ASSETS / "models" / "block"
TEXTURES = ASSETS / "textures" / "block"

ROOF_VARIANTS = {
    "straight": "roof_tiles_0.json",
    "inner_left": "roof_tiles_1_inner_left.json",
    "inner_right": "roof_tiles_1_inner_right.json",
    "outer_left": "roof_tiles_1_outer_left.json",
    "outer_right": "roof_tiles_1_outer_right.json",
}

SKIP_ELEMENTS = {"bottom", "back_side", "back_side_2"}

FACING_Y = {"north": 0, "east": 90, "south": 180, "west": 270}

AWNING_COLORS = [
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
    ("tan", "materia:tan_carpet"),
]

NL_COLOR_NAMES = {
    "orange": "Oranje",
    "magenta": "Magenta",
    "light_blue": "Lichtblauw",
    "yellow": "Geel",
    "lime": "Limoen",
    "pink": "Roze",
    "gray": "Grijs",
    "light_gray": "Lichtgrijs",
    "cyan": "Cyaan",
    "purple": "Paars",
    "blue": "Blauw",
    "brown": "Bruin",
    "green": "Groen",
    "red": "Rood",
    "black": "Zwart",
    "ochre": "Oker",
    "red_ochre": "Rode Oker",
    "lavender": "Lavendel",
    "indigo": "Indigo",
    "tyrian_purple": "Tyrisch Purper",
    "taupe": "Taupe",
    "olive": "Olijf",
    "charcoal_gray": "Houtskoolgrijs",
    "burgundy": "Bordeaux",
    "teal": "Teal",
    "tan": "Tan",
}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def display_name(color: str) -> str:
    return " ".join(part.capitalize() for part in color.split("_")) + " Awning"


def nl_display_name(color: str) -> str:
    return f"{NL_COLOR_NAMES[color]} Zonnescherm"


def load_roof_template(variant: str) -> dict:
    return json.loads((MODELS / ROOF_VARIANTS[variant]).read_text(encoding="utf-8"))


def corner_side(shape: str) -> str:
    return "left" if "left" in shape else "right"


def awning_corner_texture_pair(color: str, shape: str) -> tuple[str, str] | None:
    if shape == "straight":
        return None
    left = TEXTURES / f"{color}_awning_corner_left.png"
    right = TEXTURES / f"{color}_awning_corner_right.png"
    if not (left.exists() and right.exists()):
        return None
    primary = corner_side(shape)
    secondary = "right" if primary == "left" else "left"
    return (
        f"materia:block/{color}_awning_corner_{secondary}",
        f"materia:block/{color}_awning_corner_{primary}",
    )


def transform_model(template: dict, color: str, shape: str) -> dict:
    awning_tex = f"materia:block/{color}_awning"
    arm_tex = "materia:block/awning_arm"
    corner_pair = awning_corner_texture_pair(color, shape)

    textures = {
        "arm": arm_tex,
        "awning": awning_tex,
        "particle": awning_tex,
    }
    if corner_pair:
        textures["awning_x"] = corner_pair[0]
        textures["awning_z"] = corner_pair[1]

    model = {
        "ambientocclusion": template.get("ambientocclusion", False),
        "textures": textures,
        "elements": [],
    }

    for element in template["elements"]:
        name = element.get("name", "")
        if name in SKIP_ELEMENTS:
            continue
        copied = json.loads(json.dumps(element))
        for face in copied.get("faces", {}).values():
            texture = face.get("texture", "")
            if texture in ("#triangle", "#square"):
                face["texture"] = "#arm"
            elif texture == "#tiles":
                face["texture"] = "#awning"
            elif texture == "#tiles_x":
                face["texture"] = "#awning_x" if corner_pair else "#awning"
            elif texture == "#tiles_z":
                face["texture"] = "#awning_z" if corner_pair else "#awning"
        model["elements"].append(copied)

    return model


def generate_blockstate(color: str) -> dict:
    variants = {}
    for facing, y_rot in FACING_Y.items():
        for shape in ROOF_VARIANTS:
            suffix = "" if shape == "straight" else f"_{shape}"
            key = f"facing={facing},shape={shape}"
            variants[key] = {
                "model": f"materia:block/{color}_awning{suffix}",
                "y": y_rot,
            }
    return {"variants": variants}


def generate_recipe(color: str, carpet: str) -> dict:
    return {
        "type": "minecraft:crafting_shaped",
        "pattern": ["SRS", "CSW"],
        "key": {
            "S": {"item": "minecraft:stick"},
            "C": {"item": carpet},
            "W": {"item": "minecraft:white_carpet"},
            "R": {"item": "materia:rope"},
        },
        "result": {"item": f"materia:{color}_awning", "count": 1},
    }


def generated_item_model(color: str) -> dict:
    return {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"materia:item/{color}_awning"},
    }


def merge_lang(path: Path, entries: dict[str, str]) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    data.update(entries)
    path.write_text(json.dumps(data, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")


def main() -> None:
    lang_en = {}
    lang_nl = {}

    for color, carpet in AWNING_COLORS:
        block_id = f"{color}_awning"
        for shape, roof_file in ROOF_VARIANTS.items():
            suffix = "" if shape == "straight" else f"_{shape}"
            template = load_roof_template(shape)
            write_json(
                ASSETS / "models" / "block" / f"{block_id}{suffix}.json",
                transform_model(template, color, shape),
            )

        write_json(ASSETS / "blockstates" / f"{block_id}.json", generate_blockstate(color))
        write_json(ASSETS / "models" / "item" / f"{block_id}.json", generated_item_model(color))
        write_json(RECIPES / f"{block_id}.json", generate_recipe(color, carpet))

        lang_en[f"block.materia.{block_id}"] = display_name(color)
        lang_nl[f"block.materia.{block_id}"] = nl_display_name(color)
        corners = "with corner textures" if awning_corner_texture_pair(color, "inner_left") else "flat corners"
        print(f"Generated {block_id} ({corners})")

    merge_lang(LANG_EN, lang_en)
    merge_lang(LANG_NL, lang_nl)


if __name__ == "__main__":
    main()
