#!/usr/bin/env python3
"""Generate stone acorn finial block assets and stonecutter recipes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"

STONE_ACORN_FINIALS = (
    ("stone", "minecraft:stone", "Stone", "Steen"),
    ("limestone", "materia:limestone", "Limestone", "Kalksteen"),
    ("marble", "materia:marble", "Marble", "Marmer"),
    ("sandstone", "minecraft:sandstone", "Sandstone", "Zandsteen"),
    ("terracotta", "minecraft:terracotta", "Terracotta", "Terracotta"),
)


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def generate(material_id: str, ingredient: str, en_name: str, nl_name: str) -> None:
    block_id = f"{material_id}_acorn_finial"
    block_tex = f"materia:block/{material_id}_acorn_finial"
    item_tex = f"materia:item/{material_id}_acorn"

    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": {"": {"model": f"materia:block/{block_id}"}}})
    write_json(
        ASSETS / "models" / "block" / f"{block_id}.json",
        {"parent": "minecraft:block/cross", "textures": {"cross": block_tex}},
    )
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {"parent": "minecraft:item/generated", "textures": {"layer0": item_tex}},
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
    write_json(
        RECIPES / f"{block_id}_from_{material_id}_stonecutting.json",
        {
            "type": "minecraft:stonecutting",
            "ingredient": {"item": ingredient},
            "result": f"materia:{block_id}",
            "count": 1,
        },
    )

    lang_en = json.loads(LANG_EN.read_text(encoding="utf-8"))
    lang_nl = json.loads(LANG_NL.read_text(encoding="utf-8"))
    lang_en[f"block.materia.{block_id}"] = f"{en_name} Acorn Finial"
    lang_nl[f"block.materia.{block_id}"] = f"{nl_name} Eikel Finial"
    write_json(LANG_EN, lang_en)
    write_json(LANG_NL, lang_nl)
    print(f"Generated {block_id}")


def main() -> None:
    for material_id, ingredient, en_name, nl_name in STONE_ACORN_FINIALS:
        generate(material_id, ingredient, en_name, nl_name)


if __name__ == "__main__":
    main()
