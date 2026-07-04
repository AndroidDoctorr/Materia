#!/usr/bin/env python3
"""Generate roof_tiles corner models and blockstates.

Plain straight slope models (roof_tiles_0..8, thatch variants) are hand-maintained.
Corner geometry/UVs are copied from the corrected roof_tiles_thatch_full_* models.
"""
import json
from copy import deepcopy
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
BLOCKSTATES = ASSETS / "blockstates"

FRAME_TEMPLATE = json.loads((MODELS / "roof_frame.json").read_text(encoding="utf-8"))

FACINGS = ["north", "east", "south", "west"]
Y_ROT = {"north": 0, "east": 90, "south": 180, "west": 270}
SHAPES = ["straight", "inner_left", "inner_right", "outer_left", "outer_right"]
TRIANGLE_TEXTURE = "materia:block/roof_frame_triangle"


def element_by_name(model: dict, name: str) -> dict:
    for element in model["elements"]:
        if element["name"] == name:
            return element
    raise KeyError(name)


def back_side_2_west() -> dict:
    return {
        "name": "back_side_2",
        "from": [0, 0, 0],
        "to": [0, 16, 16],
        "faces": {
            "east": {"uv": [0, 0, 16, 16], "texture": "#square"},
            "west": {"uv": [0, 0, 16, 16], "texture": "#square"},
        },
    }


def back_side_2_east() -> dict:
    return {
        "name": "back_side_2",
        "from": [16, 0, 0],
        "to": [16, 16, 16],
        "faces": {
            "east": {"uv": [0, 0, 16, 16], "texture": "#square"},
            "west": {"uv": [0, 0, 16, 16], "texture": "#square"},
        },
    }


def slope_x_inner(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_x",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [8, 0, 0],
            "axis": "x",
            "angle": -45,
            "rescale": True,
        },
        "faces": {
            "up": {"uv": [0, 0, 16, 16], "texture": texture_ref, "cullface": False},
            "down": {"uv": [0, 16, 16, 0], "texture": texture_ref, "cullface": False},
        },
    }


def slope_x_outer(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_x",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [8, 0, 0],
            "axis": "x",
            "angle": -45,
            "rescale": True,
        },
        "faces": {
            "up": {"uv": [16, 16, 0, 0], "texture": texture_ref, "cullface": False},
            "down": {"uv": [16, 0, 0, 16], "texture": texture_ref, "cullface": False},
        },
    }


def slope_z_inner_left(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_z",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [16, 0, 8],
            "axis": "z",
            "angle": -45,
            "rescale": True,
        },
        "faces": {
            "up": {
                "rotation": 270,
                "uv": [16, 16, 0, 0],
                "texture": texture_ref,
                "cullface": False,
            },
            "down": {
                "rotation": 270,
                "uv": [0, 16, 16, 0],
                "texture": texture_ref,
                "cullface": False,
            },
        },
    }


def slope_z_inner_right(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_z",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [0, 0, 8],
            "axis": "z",
            "angle": 45,
            "rescale": True,
        },
        "faces": {
            "up": {
                "rotation": 90,
                "uv": [16, 16, 0, 0],
                "texture": texture_ref,
                "cullface": False,
            },
            "down": {
                "rotation": 90,
                "uv": [0, 16, 16, 0],
                "texture": texture_ref,
                "cullface": False,
            },
        },
    }


def slope_z_outer_left(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_z",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [16, 0, 8],
            "axis": "z",
            "angle": -45,
            "rescale": True,
        },
        "faces": {
            "up": {
                "rotation": 90,
                "uv": [16, 16, 0, 0],
                "texture": texture_ref,
                "cullface": False,
            },
            "down": {
                "rotation": 90,
                "uv": [0, 16, 16, 0],
                "texture": texture_ref,
                "cullface": False,
            },
        },
    }


def slope_z_outer_right(texture_ref: str) -> dict:
    return {
        "name": "roof_slope_z",
        "from": [0, 0, 0],
        "to": [16, 0, 16],
        "shade": False,
        "rotation": {
            "origin": [0, 0, 8],
            "axis": "z",
            "angle": 45,
            "rescale": True,
        },
        "faces": {
            "up": {
                "rotation": 90,
                "uv": [0, 0, 16, 16],
                "texture": texture_ref,
                "cullface": False,
            },
            "down": {
                "rotation": 90,
                "uv": [16, 0, 0, 16],
                "texture": texture_ref,
                "cullface": False,
            },
        },
    }


def corner_side(shape: str) -> str:
    return "left" if "left" in shape else "right"


def corner_texture_path(stage: int, thatch: bool, side: str) -> str:
    if thatch:
        if stage >= 8:
            return f"materia:block/roof_thatch_corner_{side}"
        return f"materia:block/roof_thatch_corner_{side}_1"

    if stage >= 8:
        return f"materia:block/roof_tiles_corner_{side}"
    if stage <= 0:
        return f"materia:block/roof_tiles_corner_{side}"
    return f"materia:block/roof_tiles_corner_{side}_{stage}"


def corner_texture_pair(stage: int, thatch: bool, shape: str) -> tuple[str, str]:
    primary = corner_side(shape)
    secondary = "right" if primary == "left" else "left"

    if stage == 0 and not thatch:
        return TRIANGLE_TEXTURE, TRIANGLE_TEXTURE

    return (
        corner_texture_path(stage, thatch, secondary),
        corner_texture_path(stage, thatch, primary),
    )


def build_corner_model(stage: int, thatch: bool, shape: str) -> dict:
    tex_x_path, tex_z_path = corner_texture_pair(stage, thatch, shape)
    model = deepcopy(FRAME_TEMPLATE)
    model["textures"]["tiles_x"] = tex_x_path
    model["textures"]["tiles_z"] = tex_z_path

    elements = [deepcopy(element_by_name(FRAME_TEMPLATE, "bottom"))]

    if shape == "inner_left":
        elements.extend([
            deepcopy(element_by_name(FRAME_TEMPLATE, "back_side")),
            back_side_2_west(),
            deepcopy(element_by_name(FRAME_TEMPLATE, "left_side")),
            slope_x_inner("#tiles_x"),
            slope_z_inner_left("#tiles_z"),
        ])
    elif shape == "inner_right":
        elements.extend([
            deepcopy(element_by_name(FRAME_TEMPLATE, "back_side")),
            back_side_2_east(),
            deepcopy(element_by_name(FRAME_TEMPLATE, "right_side")),
            slope_x_inner("#tiles_x"),
            slope_z_inner_right("#tiles_z"),
        ])
    elif shape == "outer_left":
        elements.extend([
            slope_x_outer("#tiles_x"),
            slope_z_outer_left("#tiles_z"),
        ])
    elif shape == "outer_right":
        elements.extend([
            slope_x_outer("#tiles_x"),
            slope_z_outer_right("#tiles_z"),
        ])
    else:
        raise ValueError(shape)

    model["elements"] = elements
    return model


def write_model(path: Path, model: dict) -> None:
    path.write_text(json.dumps(model, indent=4) + "\n", encoding="utf-8")


def model_id_for_state(thatch: bool, stage: int, shape: str) -> str:
    if shape == "straight":
        if not thatch:
            return f"materia:block/roof_tiles_{stage}"
        if stage == 0:
            return "materia:block/roof_tiles_thatch_1"
        if stage >= 8:
            return "materia:block/roof_tiles_thatch_full"
        return f"materia:block/roof_tiles_{stage}"

    suffix = shape
    if not thatch:
        return f"materia:block/roof_tiles_{stage}_{suffix}"
    if stage == 0:
        return f"materia:block/roof_tiles_thatch_1_{suffix}"
    if stage >= 8:
        return f"materia:block/roof_tiles_thatch_full_{suffix}"
    return f"materia:block/roof_tiles_{stage}_{suffix}"


def generate_corner_models() -> None:
    for stage in range(9):
        for shape in SHAPES[1:]:
            write_model(
                MODELS / f"roof_tiles_{stage}_{shape}.json",
                build_corner_model(stage, False, shape),
            )

    for shape in SHAPES[1:]:
        write_model(
            MODELS / f"roof_tiles_thatch_1_{shape}.json",
            build_corner_model(0, True, shape),
        )
        write_model(
            MODELS / f"roof_tiles_thatch_full_{shape}.json",
            build_corner_model(8, True, shape),
        )


def generate_blockstates() -> None:
    variants = {}
    for facing in FACINGS:
        for stage in range(9):
            for thatch in (False, True):
                for shape in SHAPES:
                    key = (
                        f"facing={facing},stage={stage},thatch={str(thatch).lower()},"
                        f"shape={shape}"
                    )
                    variants[key] = {
                        "model": model_id_for_state(thatch, stage, shape),
                        "y": Y_ROT[facing],
                    }

    (BLOCKSTATES / "roof_tiles.json").write_text(
        json.dumps({"variants": variants}, indent=4) + "\n",
        encoding="utf-8",
    )


def main() -> None:
    generate_corner_models()
    generate_blockstates()
    print("Generated roof_tiles corner models and blockstates (straight models untouched)")


if __name__ == "__main__":
    main()
