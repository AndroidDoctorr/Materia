#!/usr/bin/env python3
"""Generate 1.18.2-specific door and planter assets (overrides shared 1.20.1-format files)."""
import json
import urllib.request
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "1.18.2" / "src" / "main" / "resources" / "assets" / "materia"

WOODS = ["fig", "cedar", "eucalyptus", "rubber_wood", "wrought_iron"]

PLANTERS = [
    "stone_planter",
    "marble_planter",
    "limestone_planter",
    "sandstone_planter",
    "blackstone_planter",
    "terracotta_planter",
]

DOOR_MODEL_SUFFIXES = [
    ("door_bottom", "minecraft:block/door_bottom"),
    ("door_bottom_hinge", "minecraft:block/door_bottom_rh"),
    ("door_top", "minecraft:block/door_top"),
    ("door_top_hinge", "minecraft:block/door_top_rh"),
]


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def door_textures(wood: str) -> dict[str, str]:
    return {
        "bottom": f"materia:block/{wood}_door_lower",
        "top": f"materia:block/{wood}_door_upper",
    }


def load_oak_door_blockstate() -> dict:
    url = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.18.2/assets/minecraft/blockstates/oak_door.json"
    with urllib.request.urlopen(url) as response:
        return json.loads(response.read().decode("utf-8"))


def remap_door_blockstate(template: dict, wood: str) -> dict:
    variants: dict[str, dict] = {}
    for key, value in template["variants"].items():
        entry = deepcopy(value)
        entry["model"] = entry["model"].replace("minecraft:block/oak_door_", f"materia:block/{wood}_door_")
        variants[key] = entry
    return {"variants": variants}


def generate_door_models(wood: str) -> list[Path]:
    written: list[Path] = []
    textures = door_textures(wood)
    for suffix, parent in DOOR_MODEL_SUFFIXES:
        path = OUT / "models" / "block" / f"{wood}_{suffix}.json"
        write_json(path, {"parent": parent, "textures": textures})
        written.append(path)
    return written


def generate_door_blockstate(wood: str, template: dict) -> Path:
    path = OUT / "blockstates" / f"{wood}_door.json"
    write_json(path, remap_door_blockstate(template, wood))
    return path


def generate_planter_blockstate(block_id: str) -> Path:
    path = OUT / "blockstates" / f"{block_id}.json"
    write_json(path, {"variants": {"": {"model": f"materia:block/{block_id}"}}})
    return path


def main() -> None:
    door_template = load_oak_door_blockstate()
    created: list[Path] = []

    for planter_id in PLANTERS:
        created.append(generate_planter_blockstate(planter_id))

    for wood in WOODS:
        created.extend(generate_door_models(wood))
        created.append(generate_door_blockstate(wood, door_template))

    print(f"Wrote {len(created)} 1.18.2 asset overrides:")
    for path in created:
        print(f"  {path.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
