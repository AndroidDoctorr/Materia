#!/usr/bin/env python3
"""Generate balustrade models, blockstates, items, loot, recipes, and lang from stone templates."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
DATA = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia"
MODELS = ASSETS / "models" / "block"
ITEM_MODELS = ASSETS / "models" / "item"
BLOCKSTATES = ASSETS / "blockstates"
RECIPES = DATA / "recipes"
LOOT = DATA / "loot_tables" / "blocks"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"

TEMPLATE_STRAIGHT = MODELS / "stone_balustrade.json"
TEMPLATE_CORNER = MODELS / "stone_balustrade_corner.json"

BALUSTRADES = [
    {
        "id": "stone_balustrade",
        "texture": "minecraft:block/stone",
        "ingredient": "minecraft:stone",
        "display_en": "Stone Balustrade",
        "display_nl": "Stenen Balustrade",
    },
    {
        "id": "limestone_balustrade",
        "texture": "materia:block/limestone",
        "ingredient": "materia:limestone",
        "display_en": "Limestone Balustrade",
        "display_nl": "Kalksteen Balustrade",
    },
    {
        "id": "marble_balustrade",
        "texture": "materia:block/marble",
        "ingredient": "materia:marble",
        "display_en": "Marble Balustrade",
        "display_nl": "Marmeren Balustrade",
    },
    {
        "id": "terracotta_balustrade",
        "texture": "minecraft:block/terracotta",
        "ingredient": "minecraft:terracotta",
        "display_en": "Terracotta Balustrade",
        "display_nl": "Terracotta Balustrade",
    },
    {
        "id": "blackstone_balustrade",
        "texture": "minecraft:block/blackstone",
        "ingredient": "minecraft:blackstone",
        "display_en": "Blackstone Balustrade",
        "display_nl": "Blackstone Balustrade",
    },
    {
        "id": "sandstone_balustrade",
        "texture": "minecraft:block/sandstone",
        "ingredient": "minecraft:sandstone",
        "display_en": "Sandstone Balustrade",
        "display_nl": "Zandsteen Balustrade",
    },
]

FACING_Y = {"north": 0, "south": 0, "east": 90, "west": 90}


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

    return {
        "straight": straight_elements,
        "corner": corner_elements,
        "t": t_elements,
        "cross": t_elements + south_leg,
    }


def model_for(texture: str, elements: list[dict]) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"all": texture, "particle": texture},
        "elements": elements,
    }


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


def item_model(block_id: str) -> dict:
    return {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"materia:item/{block_id}"},
    }


def loot_table(block_id: str) -> dict:
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


def stonecutting_recipe(block_id: str, ingredient: str) -> dict:
    return {
        "type": "minecraft:stonecutting",
        "ingredient": {"item": ingredient},
        "result": f"materia:{block_id}",
        "count": 1,
    }


def merge_lang(path: Path, entries: dict[str, str]) -> None:
    data = json.loads(path.read_text(encoding="utf-8"))
    data.update(entries)
    path.write_text(json.dumps(data, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")


def generate_balustrade(entry: dict, parts: dict[str, list[dict]]) -> None:
    block_id = entry["id"]
    texture = entry["texture"]

    write_json(MODELS / f"{block_id}.json", model_for(texture, parts["straight"]))
    write_json(MODELS / f"{block_id}_corner.json", model_for(texture, parts["corner"]))
    write_json(MODELS / f"{block_id}_t.json", model_for(texture, parts["t"]))
    write_json(MODELS / f"{block_id}_cross.json", model_for(texture, parts["cross"]))
    write_json(BLOCKSTATES / f"{block_id}.json", blockstate_for(block_id))
    write_json(ITEM_MODELS / f"{block_id}.json", item_model(block_id))
    write_json(LOOT / f"{block_id}.json", loot_table(block_id))
    write_json(RECIPES / f"{block_id}_from_stonecutting.json", stonecutting_recipe(block_id, entry["ingredient"]))
    print(f"Generated {block_id}")


def main() -> None:
    if not TEMPLATE_STRAIGHT.exists() or not TEMPLATE_CORNER.exists():
        raise FileNotFoundError("Missing stone_balustrade.json or stone_balustrade_corner.json templates")

    parts = build_models(load_model(TEMPLATE_STRAIGHT), load_model(TEMPLATE_CORNER))

    lang_en = {}
    lang_nl = {}
    for entry in BALUSTRADES:
        generate_balustrade(entry, parts)
        lang_en[f"block.materia.{entry['id']}"] = entry["display_en"]
        lang_nl[f"block.materia.{entry['id']}"] = entry["display_nl"]

    merge_lang(LANG_EN, lang_en)
    merge_lang(LANG_NL, lang_nl)

    side_model = MODELS / "stone_balustrade_side.json"
    if side_model.exists():
        side_model.unlink()


if __name__ == "__main__":
    main()
