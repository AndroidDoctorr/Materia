#!/usr/bin/env python3
"""Generate door/trapdoor block models, blockstates, and item models for Materia wood types."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
ITEM_MODELS = ASSETS / "models" / "item"
BLOCKSTATES = ASSETS / "blockstates"

WOODS = ["fig", "cedar", "eucalyptus", "rubber_wood"]

DOOR_MODEL_SUFFIXES = [
    ("door_bottom_left", "minecraft:block/door_bottom_left"),
    ("door_bottom_left_open", "minecraft:block/door_bottom_left_open"),
    ("door_bottom_right", "minecraft:block/door_bottom_right"),
    ("door_bottom_right_open", "minecraft:block/door_bottom_right_open"),
    ("door_top_left", "minecraft:block/door_top_left"),
    ("door_top_left_open", "minecraft:block/door_top_left_open"),
    ("door_top_right", "minecraft:block/door_top_right"),
    ("door_top_right_open", "minecraft:block/door_top_right_open"),
]

TRAPDOOR_MODELS = [
    ("trapdoor_bottom", "minecraft:block/template_trapdoor_bottom"),
    ("trapdoor_top", "minecraft:block/template_trapdoor_top"),
    ("trapdoor_open", "minecraft:block/template_trapdoor_open"),
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
    import urllib.request

    url = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.20.1/assets/minecraft/blockstates/oak_door.json"
    with urllib.request.urlopen(url) as response:
        return json.loads(response.read().decode("utf-8"))


def load_oak_trapdoor_blockstate() -> dict:
    import urllib.request

    url = "https://raw.githubusercontent.com/InventivetalentDev/minecraft-assets/1.20.1/assets/minecraft/blockstates/oak_trapdoor.json"
    with urllib.request.urlopen(url) as response:
        return json.loads(response.read().decode("utf-8"))


def remap_blockstate_variants(template: dict, wood: str, kind: str) -> dict:
    variants: dict[str, dict] = {}
    for key, value in template["variants"].items():
        entry = deepcopy(value)
        model = entry["model"].replace("minecraft:block/oak_", f"materia:block/{wood}_")
        entry["model"] = model
        variants[key] = entry
    return {"variants": variants}


def generate_door_models(wood: str) -> list[Path]:
    written: list[Path] = []
    textures = door_textures(wood)
    for suffix, parent in DOOR_MODEL_SUFFIXES:
        path = MODELS / f"{wood}_{suffix}.json"
        write_json(path, {"parent": parent, "textures": textures})
        written.append(path)
    return written


def generate_trapdoor_models(wood: str) -> list[Path]:
    written: list[Path] = []
    texture = f"materia:block/{wood}_trapdoor"
    for suffix, parent in TRAPDOOR_MODELS:
        path = MODELS / f"{wood}_{suffix}.json"
        write_json(path, {"parent": parent, "textures": {"texture": texture}})
        written.append(path)
    return written


def generate_blockstates(wood: str, door_template: dict, trap_template: dict) -> list[Path]:
    door_path = BLOCKSTATES / f"{wood}_door.json"
    trap_path = BLOCKSTATES / f"{wood}_trapdoor.json"
    write_json(door_path, remap_blockstate_variants(door_template, wood, "door"))
    write_json(trap_path, remap_blockstate_variants(trap_template, wood, "trapdoor"))
    return [door_path, trap_path]


def generate_item_models(wood: str) -> list[Path]:
    written: list[Path] = []
    door_path = ITEM_MODELS / f"{wood}_door.json"
    trap_path = ITEM_MODELS / f"{wood}_trapdoor.json"
    write_json(
        door_path,
        {
            "parent": "minecraft:item/generated",
            "textures": {"layer0": f"materia:item/{wood}_door"},
        },
    )
    write_json(
        trap_path,
        {"parent": f"materia:block/{wood}_trapdoor_bottom"},
    )
    written.extend([door_path, trap_path])
    return written


def main() -> None:
    door_template = load_oak_door_blockstate()
    trap_template = load_oak_trapdoor_blockstate()
    created: list[Path] = []
    for wood in WOODS:
        created.extend(generate_door_models(wood))
        created.extend(generate_trapdoor_models(wood))
        created.extend(generate_blockstates(wood, door_template, trap_template))
        created.extend(generate_item_models(wood))
    print(f"Wrote {len(created)} files:")
    for path in created:
        print(f"  {path.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
