#!/usr/bin/env python3
"""Generate slab and stair assets for marble and limestone."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"

STONES = ("marble", "limestone")


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def copy_replace(src: Path, dst: Path, replacements: dict[str, str]) -> None:
    text = src.read_text(encoding="utf-8")
    for old, new in replacements.items():
        text = text.replace(old, new)
    dst.parent.mkdir(parents=True, exist_ok=True)
    dst.write_text(text, encoding="utf-8")


def stone_textures(stone: str) -> dict[str, str]:
    tex = f"materia:block/{stone}"
    return {"bottom": tex, "top": tex, "side": tex}


def generate_slab(stone: str) -> None:
    slab_id = f"{stone}_slab"
    write_json(ASSETS / "models" / "block" / f"{slab_id}.json", {"parent": "minecraft:block/slab", "textures": stone_textures(stone)})
    write_json(ASSETS / "models" / "block" / f"{slab_id}_top.json", {"parent": "minecraft:block/slab_top", "textures": stone_textures(stone)})
    write_json(
        ASSETS / "blockstates" / f"{slab_id}.json",
        {
            "variants": {
                "type=bottom": {"model": f"materia:block/{slab_id}"},
                "type=top": {"model": f"materia:block/{slab_id}_top"},
                "type=double": {"model": f"materia:block/{stone}"},
            }
        },
    )
    write_json(ASSETS / "models" / "item" / f"{slab_id}.json", {"parent": f"materia:block/{slab_id}"})
    write_json(
        LOOT / f"{slab_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{slab_id}"}],
                    "conditions": [{"condition": "minecraft:survives_explosion"}],
                }
            ],
        },
    )
    write_json(
        RECIPES / f"{slab_id}_from_stonecutting.json",
        {"type": "minecraft:stonecutting", "ingredient": {"item": f"materia:{stone}"}, "result": f"materia:{slab_id}", "count": 2},
    )


def generate_stairs(stone: str) -> None:
    stair_id = f"{stone}_stairs"
    for suffix, parent in [("", "stairs"), ("_inner", "inner_stairs"), ("_outer", "outer_stairs")]:
        write_json(
            ASSETS / "models" / "block" / f"{stair_id}{suffix}.json",
            {"parent": f"minecraft:block/{parent}", "textures": stone_textures(stone)},
        )
    copy_replace(
        ASSETS / "blockstates" / "cedar_stairs.json",
        ASSETS / "blockstates" / f"{stair_id}.json",
        {"cedar_stairs": stair_id},
    )
    write_json(ASSETS / "models" / "item" / f"{stair_id}.json", {"parent": f"materia:block/{stair_id}"})
    write_json(
        LOOT / f"{stair_id}.json",
        {
            "type": "minecraft:block",
            "pools": [
                {
                    "rolls": 1,
                    "entries": [{"type": "minecraft:item", "name": f"materia:{stair_id}"}],
                    "conditions": [{"condition": "minecraft:survives_explosion"}],
                }
            ],
        },
    )
    write_json(
        RECIPES / f"{stair_id}_from_stonecutting.json",
        {"type": "minecraft:stonecutting", "ingredient": {"item": f"materia:{stone}"}, "result": f"materia:{stair_id}", "count": 1},
    )


def main() -> None:
    for stone in STONES:
        generate_slab(stone)
        generate_stairs(stone)
    print("Generated marble and limestone slab/stair assets and stonecutting recipes.")


if __name__ == "__main__":
    main()
