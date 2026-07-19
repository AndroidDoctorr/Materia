#!/usr/bin/env python3
"""Generate copper/shingle roof block models, roof_tiles blockstates, and item models."""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
ITEM_MODELS = ASSETS / "models" / "item"
BLOCKSTATES = ASSETS / "blockstates"
BLOCKSTATE_PATH = BLOCKSTATES / "roof_tiles.json"

FACINGS = ["north", "east", "south", "west"]
Y_ROT = {"north": 0, "east": 90, "south": 180, "west": 270}
SHAPES = ["straight", "inner_left", "inner_right", "outer_left", "outer_right"]
CORNER_SHAPES = SHAPES[1:]

COPPER_TILES = [
    "minecraft:block/copper_block",
    "minecraft:block/exposed_copper",
    "minecraft:block/weathered_copper",
    "minecraft:block/oxidized_copper",
]

SHINGLE_TILES = {
    1: "materia:block/shingles_1",
    2: "materia:block/shingles_2",
    3: "materia:block/shingles_3",
    4: "materia:block/shingles",
}

VARIANT_PROP_ORDER = ["cover_type", "facing", "oxidation", "shape", "stage", "thatch"]


def load_json(path: Path) -> dict:
    return json.loads(path.read_text(encoding="utf-8"))


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def copper_corner_suffix(oxidation: int) -> str:
    return "" if oxidation == 0 else f"_{oxidation}"


def copper_corner_texture(side: str, oxidation: int) -> str:
    return f"materia:block/roof_copper_corner_{side}{copper_corner_suffix(oxidation)}"


def corner_side(shape: str) -> str:
    return "left" if "left" in shape else "right"


def copper_corner_texture_pair(shape: str, oxidation: int) -> tuple[str, str]:
    primary = corner_side(shape)
    secondary = "right" if primary == "left" else "left"
    return (
        copper_corner_texture(secondary, oxidation),
        copper_corner_texture(primary, oxidation),
    )


def shingle_corner_suffix(stage: int) -> str:
    return "" if stage == 4 else f"_{stage}"


def shingle_corner_texture(side: str, stage: int) -> str:
    return f"materia:block/roof_shingles_corner_{side}{shingle_corner_suffix(stage)}"


def shingle_corner_texture_pair(shape: str, stage: int) -> tuple[str, str]:
    primary = corner_side(shape)
    secondary = "right" if primary == "left" else "left"
    return (
        shingle_corner_texture(secondary, stage),
        shingle_corner_texture(primary, stage),
    )


def variant_key(props: dict[str, str]) -> str:
    return ",".join(f"{name}={props[name]}" for name in VARIANT_PROP_ORDER if name in props)


def parse_variant_key(key: str) -> dict[str, str]:
    props: dict[str, str] = {}
    for part in key.split(","):
        name, value = part.split("=", 1)
        props[name] = value
    return props


def straight_template() -> dict:
    return load_json(MODELS / "roof_tiles_0.json")


def corner_template(shape: str) -> dict:
    return load_json(MODELS / f"roof_tiles_1_{shape}.json")


def build_straight_model(tiles_texture: str, particle: str) -> dict:
    model = deepcopy(straight_template())
    model["textures"]["tiles"] = tiles_texture
    model["textures"]["particle"] = particle
    return model


def build_corner_model(shape: str, tiles_x: str, tiles_z: str, particle: str) -> dict:
    model = deepcopy(corner_template(shape))
    model["textures"]["tiles_x"] = tiles_x
    model["textures"]["tiles_z"] = tiles_z
    model["textures"]["particle"] = particle
    return model


def copper_model_name(oxidation: int, shape: str) -> str:
    if shape == "straight":
        return f"roof_copper_{oxidation}"
    return f"roof_copper_{oxidation}_{shape}"


def shingle_model_name(stage: int, shape: str) -> str:
    if shape == "straight":
        return f"roof_shingle_{stage}"
    return f"roof_shingle_{stage}_{shape}"


def generate_copper_models() -> list[Path]:
    written: list[Path] = []
    for oxidation in range(4):
        tiles = COPPER_TILES[oxidation]
        particle = tiles
        for shape in SHAPES:
            name = copper_model_name(oxidation, shape)
            path = MODELS / f"{name}.json"
            if shape == "straight":
                model = build_straight_model(tiles, particle)
            else:
                tex_x, tex_z = copper_corner_texture_pair(shape, oxidation)
                model = build_corner_model(shape, tex_x, tex_z, particle)
            write_json(path, model)
            written.append(path)
    return written


def generate_shingle_models() -> list[Path]:
    written: list[Path] = []
    for stage in range(1, 5):
        tiles = SHINGLE_TILES[stage]
        for shape in SHAPES:
            name = shingle_model_name(stage, shape)
            path = MODELS / f"{name}.json"
            if shape == "straight":
                model = build_straight_model(tiles, tiles)
            else:
                tex_x, tex_z = shingle_corner_texture_pair(shape, stage)
                model = build_corner_model(shape, tex_x, tex_z, tiles)
            write_json(path, model)
            written.append(path)
    return written


def legacy_variant_key(key: str) -> str:
    props = parse_variant_key(key)
    props.setdefault("cover_type", "0")
    props.setdefault("oxidation", "0")
    return variant_key(props)


def generate_blockstate() -> Path:
    existing = load_json(BLOCKSTATE_PATH)
    variants: dict[str, dict] = {}

    for key, value in existing["variants"].items():
        variants[legacy_variant_key(key)] = value

    for facing in FACINGS:
        y = Y_ROT[facing]
        for oxidation in range(4):
            for shape in SHAPES:
                model = f"materia:block/{copper_model_name(oxidation, shape)}"
                props = {
                    "cover_type": "1",
                    "facing": facing,
                    "oxidation": str(oxidation),
                    "shape": shape,
                    "stage": "0",
                    "thatch": "false",
                }
                variants[variant_key(props)] = {"model": model, "y": y}

        for stage in range(1, 5):
            for shape in SHAPES:
                model = f"materia:block/{shingle_model_name(stage, shape)}"
                props = {
                    "cover_type": "2",
                    "facing": facing,
                    "oxidation": "0",
                    "shape": shape,
                    "stage": str(stage),
                    "thatch": "false",
                }
                variants[variant_key(props)] = {"model": model, "y": y}

    write_json(BLOCKSTATE_PATH, {"variants": variants})
    return BLOCKSTATE_PATH


def generate_item_models() -> list[Path]:
    specs = [
        ("shingle.json", "materia:item/shingle"),
        ("roof_copper.json", "materia:item/roof_copper"),
        ("shingle_roof.json", "materia:item/roof_shingles"),
    ]
    written: list[Path] = []
    for filename, texture in specs:
        path = ITEM_MODELS / filename
        write_json(
            path,
            {
                "parent": "minecraft:item/generated",
                "textures": {"layer0": texture},
            },
        )
        written.append(path)
    return written


def main() -> None:
    created: list[Path] = []
    created.extend(generate_copper_models())
    created.extend(generate_shingle_models())
    created.append(generate_blockstate())
    created.extend(generate_item_models())

    print(f"Wrote {len(created)} files:")
    for path in created:
        print(f"  {path.relative_to(ROOT)}")


if __name__ == "__main__":
    main()
