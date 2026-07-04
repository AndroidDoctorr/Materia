#!/usr/bin/env python3
"""Generate roof_tiles corner models and blockstates.

Plain straight slope models (roof_tiles_0..8, thatch variants) are hand-maintained.
This script only emits corner shape variants and blockstates, using the corrected
slope element from roof_tiles_8.json as the geometry reference.
"""
import json
from copy import deepcopy
from pathlib import Path
from typing import Callable

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
BLOCKSTATES = ASSETS / "blockstates"

FRAME_TEMPLATE = json.loads((MODELS / "roof_frame.json").read_text(encoding="utf-8"))
SLOPE_REFERENCE = json.loads((MODELS / "roof_tiles_8.json").read_text(encoding="utf-8"))
SLOPE_ELEMENT = next(element for element in SLOPE_REFERENCE["elements"] if element["name"] == "roof_tiles")

FACINGS = ["north", "east", "south", "west"]
Y_ROT = {"north": 0, "east": 90, "south": 180, "west": 270}
SHAPES = ["straight", "inner_left", "inner_right", "outer_left", "outer_right"]
TRIANGLE_TEXTURE = "materia:block/roof_frame_triangle"

UvTransform = Callable[[dict], None]


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
        corner_texture_path(stage, thatch, primary),
        corner_texture_path(stage, thatch, secondary),
    )


def element_by_name(model: dict, name: str) -> dict:
    for element in model["elements"]:
        if element["name"] == name:
            return element
    raise KeyError(name)


def set_face_texture(element: dict, texture_ref: str) -> None:
    for face in element["faces"].values():
        face["texture"] = texture_ref


def rotate_uv_180(element: dict) -> None:
    for face in element["faces"].values():
        u1, v1, u2, v2 = face["uv"]
        face["uv"] = [u2, v2, u1, v1]


def rotate_uv_90_ccw(element: dict) -> None:
    for face in element["faces"].values():
        u1, v1, u2, v2 = face["uv"]
        face["uv"] = [v2, u1, v1, u2]


def flip_uv_across_z(element: dict) -> None:
    for face in element["faces"].values():
        u1, v1, u2, v2 = face["uv"]
        face["uv"] = [u2, v1, u1, v2]


def compose_uv(*transforms: UvTransform) -> UvTransform:
    def apply(element: dict) -> None:
        for transform in transforms:
            transform(element)

    return apply


INNER_Z_UV = compose_uv(rotate_uv_90_ccw, flip_uv_across_z)
OUTER_Z_UV = flip_uv_across_z
EMPTY_OUTER_X_UV = compose_uv(rotate_uv_90_ccw, flip_uv_across_z)


def slope_along_x(texture_ref: str, uv_transform: UvTransform | None = None) -> dict:
    slope = deepcopy(SLOPE_ELEMENT)
    slope["name"] = "roof_slope_x"
    set_face_texture(slope, texture_ref)
    if uv_transform:
        uv_transform(slope)
    return slope


def slope_along_z(
    origin_x: int,
    angle: float,
    texture_ref: str,
    uv_transform: UvTransform | None = None,
) -> dict:
    slope = deepcopy(SLOPE_ELEMENT)
    slope["name"] = "roof_slope_z"
    slope["rotation"] = {
        "origin": [origin_x, 0, 8],
        "axis": "z",
        "angle": angle,
        "rescale": True,
    }
    set_face_texture(slope, texture_ref)
    if uv_transform:
        uv_transform(slope)
    return slope


def build_corner_model(
    frame_names: list[str],
    tex_x: str,
    tex_z: str,
    z_origin: int,
    z_angle: float,
    x_uv: UvTransform | None = None,
    z_uv: UvTransform | None = None,
) -> dict:
    model = deepcopy(FRAME_TEMPLATE)
    model["textures"]["tiles_x"] = tex_x
    model["textures"]["tiles_z"] = tex_z
    elements = [deepcopy(element_by_name(FRAME_TEMPLATE, name)) for name in frame_names]
    elements.append(slope_along_x("#tiles_x", x_uv))
    elements.append(slope_along_z(z_origin, z_angle, "#tiles_z", z_uv))
    model["elements"] = elements
    return model


def corner_model(stage: int, thatch: bool, shape: str) -> dict:
    tex_primary, tex_secondary = corner_texture_pair(stage, thatch, shape)
    tex_x = tex_secondary
    tex_z = tex_primary

    if shape == "inner_left":
        return build_corner_model(
            ["bottom", "back_side", "left_side"],
            tex_x,
            tex_z,
            16,
            -45,
            rotate_uv_180,
            INNER_Z_UV,
        )
    if shape == "inner_right":
        return build_corner_model(
            ["bottom", "back_side", "right_side"],
            tex_x,
            tex_z,
            0,
            45,
            rotate_uv_180,
            INNER_Z_UV,
        )
    if shape == "outer_left":
        empty_frame = stage == 0 and not thatch
        return build_corner_model(
            ["bottom"],
            tex_x,
            tex_z,
            16,
            -45,
            EMPTY_OUTER_X_UV if empty_frame else None,
            OUTER_Z_UV,
        )
    if shape == "outer_right":
        return build_corner_model(
            ["bottom"],
            tex_x,
            tex_z,
            0,
            45,
            None,
            OUTER_Z_UV,
        )
    raise ValueError(shape)


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
                corner_model(stage, False, shape),
            )

    for shape in SHAPES[1:]:
        write_model(
            MODELS / f"roof_tiles_thatch_1_{shape}.json",
            corner_model(0, True, shape),
        )
        write_model(
            MODELS / f"roof_tiles_thatch_full_{shape}.json",
            corner_model(8, True, shape),
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
