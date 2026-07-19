#!/usr/bin/env python3
"""Generate wrought iron bracket and grate assets and anvil recipes."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"

BRACKET_MODEL_Y = {"north": 180, "south": 0, "east": 270, "west": 90}
GRATE_FACING_Y = {"north": 0, "south": 180, "east": 90, "west": 270}
FACINGS = ("north", "south", "east", "west")
ANVIL_TOOLS = ["materia:iron_hammers", "materia:iron_tongs", "materia:iron_tongs"]
BRONZE_ANVIL_TOOLS = ["materia:bronze_hammers", "materia:iron_tongs"]
BLOCK_ITEM_DISPLAY = {
    "thirdperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "firstperson_righthand": {"rotation": [0, 45, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
    "gui": {"rotation": [30, 45, 0], "translation": [0, 0, 0], "scale": [0.625, 0.625, 0.625]},
    "ground": {"rotation": [0, 0, 0], "translation": [0, 3, 0], "scale": [0.25, 0.25, 0.25]},
    "fixed": {"rotation": [0, 0, 0], "translation": [0, 0, 0], "scale": [0.5, 0.5, 0.5]},
}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


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


def merge_lang(entries_en: dict[str, str], entries_nl: dict[str, str]) -> None:
    lang_en = json.loads(LANG_EN.read_text(encoding="utf-8"))
    lang_nl = json.loads(LANG_NL.read_text(encoding="utf-8"))
    lang_en.update(entries_en)
    lang_nl.update(entries_nl)
    write_json(LANG_EN, lang_en)
    write_json(LANG_NL, lang_nl)


def generate_bracket() -> None:
    block_id = "wrought_iron_bracket"
    write_json(
        ASSETS / "blockstates" / f"{block_id}.json",
        {
            "variants": {
                f"facing={facing}": {"model": f"materia:block/{block_id}", "y": BRACKET_MODEL_Y[facing], "uvlock": True}
                for facing in BRACKET_MODEL_Y
            }
        },
    )
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{block_id}"}},
    )
    write_json(LOOT / f"{block_id}.json", standard_loot(block_id))
    write_json(
        RECIPES / "iron_anvil" / f"{block_id}_from_band_wire.json",
        {
            "type": "materia:iron_anvil",
            "input_a": {"item": "materia:iron_band", "count": 1},
            "input_b": {"item": "materia:iron_wire", "count": 1},
            "tool_tags": ANVIL_TOOLS,
            "result": {"item": f"materia:{block_id}", "count": 1},
        },
    )


def grate_faces() -> dict:
    return {
        face: {"uv": [0, 0, 16, 16], "texture": "#grate", "cullface": False}
        for face in ("north", "south", "east", "west", "up", "down")
    }


def grate_model(from_coords: list[int], to_coords: list[int], tex: str) -> dict:
    return {
        "ambientocclusion": False,
        "textures": {"grate": tex, "particle": tex},
        "elements": [{"from": from_coords, "to": to_coords, "faces": grate_faces()}],
    }


def generate_grate() -> None:
    block_id = "wrought_iron_grate"
    tex = "materia:block/wrought_iron_grate"
    attaches = ("up", "down", "north", "south", "east", "west")
    variants = {}
    for attach in attaches:
        for facing in FACINGS:
            variants[f"attach={attach},facing={facing}"] = {
                "model": f"materia:block/{block_id}",
                "y": GRATE_FACING_Y[facing],
                "uvlock": True,
            }
    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": variants})
    write_json(
        ASSETS / "models" / "block" / f"{block_id}.json",
        grate_model([0, 0, 8], [16, 16, 9], tex),
    )
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {
            "parent": f"materia:block/{block_id}",
            "display": BLOCK_ITEM_DISPLAY,
        },
    )
    write_json(LOOT / f"{block_id}.json", standard_loot(block_id))
    write_json(
        RECIPES / "iron_anvil" / f"{block_id}_from_wire.json",
        {
            "type": "materia:iron_anvil",
            "input_a": {"item": "materia:iron_wire", "count": 4},
            "tool_tags": ANVIL_TOOLS,
            "result": {"item": f"materia:{block_id}", "count": 1},
        },
    )
    write_json(
        RECIPES / "bronze_anvil" / f"{block_id}_from_wire.json",
        {
            "type": "materia:bronze_anvil",
            "input": {"item": "materia:iron_wire", "count": 4},
            "tool_tags": BRONZE_ANVIL_TOOLS,
            "result": {"item": f"materia:{block_id}", "count": 1},
        },
    )


def main() -> None:
    generate_bracket()
    generate_grate()
    merge_lang(
        {
            "block.materia.wrought_iron_bracket": "Wrought Iron Bracket",
            "block.materia.wrought_iron_grate": "Wrought Iron Grate",
        },
        {
            "block.materia.wrought_iron_bracket": "Smeedijzeren Console",
            "block.materia.wrought_iron_grate": "Smeedijzeren Rooster",
        },
    )
    print("Generated wrought iron bracket and grate assets")


if __name__ == "__main__":
    main()
