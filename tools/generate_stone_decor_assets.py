#!/usr/bin/env python3
"""Generate stone decorative blocks, sculpture variants, and stonecutter recipes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
TEXTURES_BLOCK = ASSETS / "textures" / "block"
TEXTURES_ITEM = ASSETS / "textures" / "item"

# (block_id_suffix, texture_name, stonecutter_ingredient)
STONE_VARIANTS = [
    ("stone_tiles", "stone_tiles", "minecraft:stone"),
    ("stone_bricks_small", "stone_bricks_small", "minecraft:stone"),
    ("marble_bricks", "marble_bricks", "materia:marble"),
    ("marble_bricks_small", "marble_bricks_small", "materia:marble"),
    ("polished_marble", "polished_marble", "materia:marble"),
    ("marble_tiles", "marble_tiles", "materia:marble"),
    ("limestone_bricks", "limestone_bricks", "materia:limestone"),
    ("limestone_bricks_small", "limestone_bricks_small", "materia:limestone"),
    ("polished_limestone", "polished_limestone", "materia:limestone"),
    ("limestone_tiles", "limestone_tiles", "materia:limestone"),
    ("limestone_chiseled", "carved_limestone", "materia:limestone"),
    ("sandstone_bricks", "sandstone_bricks", "minecraft:sandstone"),
    ("sandstone_tiles", "sandstone_tiles", "minecraft:sandstone"),
    ("blackstone_tiles", "blackstone_tiles", "minecraft:blackstone"),
]

# (material_id, stonecutter_ingredient, cap_texture)
SCULPTURE_MATERIALS = [
    ("stone", "minecraft:stone", "minecraft:block/stone"),
    ("marble", "materia:marble", "materia:block/marble"),
    ("limestone", "materia:limestone", "materia:block/limestone"),
    ("sandstone", "minecraft:sandstone", "minecraft:block/sandstone"),
    ("blackstone", "minecraft:blackstone", "minecraft:block/blackstone"),
    ("terracotta", "minecraft:terracotta", "minecraft:block/terracotta"),
]

COLUMN_MATERIALS = SCULPTURE_MATERIALS[:-1]


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def cube_blockstate(block_id: str) -> dict:
    return {"variants": {"": {"model": f"materia:block/{block_id}"}}}


def planter_blockstate(block_id: str) -> dict:
    model = f"materia:block/{block_id}"
    return {
        "variants": {
            "facing=north": {"model": model, "y": 0},
            "facing=south": {"model": model, "y": 180},
            "facing=east": {"model": model, "y": 90},
            "facing=west": {"model": model, "y": 270},
        }
    }


def cube_model(block_id: str, texture: str) -> dict:
    return {
        "parent": "minecraft:block/cube_all",
        "textures": {"all": f"materia:block/{texture}"},
    }


def generated_item(block_id: str) -> dict:
    return {
        "parent": "minecraft:item/generated",
        "textures": {"layer0": f"materia:item/{block_id}"},
    }


def block_item(block_id: str) -> dict:
    return {"parent": f"materia:block/{block_id}"}


def cube_item(block_id: str) -> dict:
    return block_item(block_id)


def stonecutter_recipe(result: str, ingredient: str, count: int = 1) -> dict:
    return {
        "type": "minecraft:stonecutting",
        "ingredient": {"item": ingredient},
        "result": f"materia:{result}",
        "count": count,
    }


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


def generate_balustrade_blockstate(stone: str) -> dict:
    base = f"materia:block/{stone}_balustrade"
    corner = f"materia:block/{stone}_balustrade_corner"
    side = f"materia:block/{stone}_balustrade_side"
    return {
        "multipart": [
            {
                "when": {"north": True, "east": True, "south": False, "west": False},
                "apply": {"model": corner, "y": 0, "uvlock": True},
            },
            {
                "when": {"north": True, "west": True, "south": False, "east": False},
                "apply": {"model": corner, "y": 270, "uvlock": True},
            },
            {
                "when": {"south": True, "east": True, "north": False, "west": False},
                "apply": {"model": corner, "y": 90, "uvlock": True},
            },
            {
                "when": {"south": True, "west": True, "north": False, "east": False},
                "apply": {"model": corner, "y": 180, "uvlock": True},
            },
            {
                "when": {
                    "OR": [
                        {"north": False, "east": False},
                        {"north": False, "west": False},
                        {"south": False, "east": False},
                        {"south": False, "west": False},
                        {"north": True, "south": True},
                        {"east": True, "west": True},
                    ]
                },
                "apply": {"model": base, "uvlock": True},
            },
            {"when": {"north": True}, "apply": {"model": side, "uvlock": True}},
            {"when": {"south": True}, "apply": {"model": side, "y": 180, "uvlock": True}},
            {"when": {"east": True}, "apply": {"model": side, "y": 90, "uvlock": True}},
            {"when": {"west": True}, "apply": {"model": side, "y": 270, "uvlock": True}},
        ]
    }


def generate_balustrade_side_model(stone: str, base_texture: str) -> dict:
    return {
        "parent": "minecraft:block/fence_side",
        "textures": {"texture": base_texture},
    }


def urn_side_texture(material: str) -> str:
    side = TEXTURES_BLOCK / f"{material}_urn_side.png"
    if side.exists():
        return f"materia:block/{material}_urn_side"
    top = TEXTURES_BLOCK / f"{material}_urn_top.png"
    if top.exists():
        return f"materia:block/{material}_urn_top"
    return f"materia:block/{material}_urn_side"


def urn_model(material: str, base_tex: str) -> dict:
    side_tex = urn_side_texture(material)
    top_tex = f"materia:block/{material}_urn_top"
    template = ASSETS / "models" / "block" / "stone_urn.json"
    data = json.loads(template.read_text(encoding="utf-8"))
    textures = data.setdefault("textures", {})
    textures["all"] = base_tex
    textures["side"] = side_tex
    textures["top"] = top_tex
    textures["dirt"] = top_tex
    textures["particle"] = base_tex
    return data


def planter_model(material: str, base_tex: str) -> dict:
    side_tex = f"materia:block/{material}_planter"
    template = ASSETS / "models" / "block" / "stone_planter.json"
    data = json.loads(template.read_text(encoding="utf-8"))
    textures = data.setdefault("textures", {})
    textures["all"] = base_tex
    textures["side"] = side_tex
    textures["particle"] = base_tex
    return data


def column_model(material: str, cap_tex: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {
            "top": cap_tex,
            "side": f"materia:block/{material}_column",
            "particle": cap_tex,
        },
        "elements": [
            {
                "from": [2, 0, 2],
                "to": [14, 16, 14],
                "faces": {
                    "north": {"uv": [0, 0, 12, 16], "texture": "#side"},
                    "east": {"uv": [0, 0, 12, 16], "texture": "#side"},
                    "south": {"uv": [0, 0, 12, 16], "texture": "#side"},
                    "west": {"uv": [0, 0, 12, 16], "texture": "#side"},
                    "up": {"uv": [0, 0, 12, 10], "texture": "#top"},
                    "down": {"uv": [0, 0, 12, 10], "texture": "#top"},
                },
            }
        ],
    }


def item_model_for(block_id: str) -> dict:
    if (TEXTURES_ITEM / f"{block_id}.png").exists():
        return generated_item(block_id)
    return block_item(block_id)


def main() -> None:
    models_block = ASSETS / "models" / "block"

    for block_id, texture, ingredient in STONE_VARIANTS:
        write_json(models_block / f"{block_id}.json", cube_model(block_id, texture))
        write_json(ASSETS / "blockstates" / f"{block_id}.json", cube_blockstate(block_id))
        write_json(ASSETS / "models" / "item" / f"{block_id}.json", cube_item(block_id))
        write_json(LOOT / f"{block_id}.json", standard_loot(block_id))
        write_json(
            RECIPES / f"{block_id}_from_stonecutting.json",
            stonecutter_recipe(block_id, ingredient),
        )
        print(f"Generated {block_id}")

    stone = "stone"
    base_tex = "minecraft:block/stone"

    balustrade_src = models_block / f"{stone}_balustrade.json"
    if balustrade_src.exists():
        data = json.loads(balustrade_src.read_text(encoding="utf-8"))
        data.setdefault("textures", {})["all"] = base_tex
        data["textures"]["particle"] = base_tex
        write_json(balustrade_src, data)

    corner_src = models_block / f"{stone}_balustrade_corner.json"
    if corner_src.exists():
        data = json.loads(corner_src.read_text(encoding="utf-8"))
        data.setdefault("textures", {})["all"] = base_tex
        data["textures"]["particle"] = base_tex
        write_json(corner_src, data)

    write_json(
        models_block / f"{stone}_balustrade_side.json",
        generate_balustrade_side_model(stone, base_tex),
    )
    write_json(ASSETS / "blockstates" / f"{stone}_balustrade.json", generate_balustrade_blockstate(stone))
    write_json(ASSETS / "models" / "item" / f"{stone}_balustrade.json", block_item(f"{stone}_balustrade"))
    write_json(LOOT / f"{stone}_balustrade.json", standard_loot(f"{stone}_balustrade"))
    write_json(
        RECIPES / f"{stone}_balustrade_from_stonecutting.json",
        stonecutter_recipe(f"{stone}_balustrade", "minecraft:stone"),
    )

    for material, ingredient, cap_tex in SCULPTURE_MATERIALS:
        urn_id = f"{material}_urn"
        planter_id = f"{material}_planter"

        write_json(models_block / f"{urn_id}.json", urn_model(material, cap_tex))
        write_json(models_block / f"{planter_id}.json", planter_model(material, cap_tex))
        write_json(ASSETS / "blockstates" / f"{urn_id}.json", cube_blockstate(urn_id))
        write_json(ASSETS / "blockstates" / f"{planter_id}.json", planter_blockstate(planter_id))
        write_json(ASSETS / "models" / "item" / f"{urn_id}.json", item_model_for(urn_id))
        write_json(ASSETS / "models" / "item" / f"{planter_id}.json", item_model_for(planter_id))
        write_json(LOOT / f"{urn_id}.json", standard_loot(urn_id))
        write_json(LOOT / f"{planter_id}.json", standard_loot(planter_id))
        write_json(
            RECIPES / f"{urn_id}_from_stonecutting.json",
            stonecutter_recipe(urn_id, ingredient),
        )
        write_json(
            RECIPES / f"{planter_id}_from_stonecutting.json",
            stonecutter_recipe(planter_id, ingredient),
        )
        print(f"Generated sculpture {urn_id}, {planter_id}")

    for material, ingredient, cap_tex in COLUMN_MATERIALS:
        column_id = f"{material}_column"
        write_json(models_block / f"{column_id}.json", column_model(material, cap_tex))
        write_json(ASSETS / "blockstates" / f"{column_id}.json", cube_blockstate(column_id))
        write_json(ASSETS / "models" / "item" / f"{column_id}.json", item_model_for(column_id))
        write_json(LOOT / f"{column_id}.json", standard_loot(column_id))
        write_json(
            RECIPES / f"{column_id}_from_{material}_stonecutting.json",
            stonecutter_recipe(column_id, ingredient),
        )
        print(f"Generated column {column_id}")


if __name__ == "__main__":
    main()
