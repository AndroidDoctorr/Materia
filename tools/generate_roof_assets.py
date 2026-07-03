#!/usr/bin/env python3
"""Generate roof_tiles block models and blockstates from the roof_frame template.

Block model rotations only accept -45, -22.5, 0, 22.5, or 45 degrees.
Flip slope direction by moving the rotation origin along the ridge (z=0 vs z=16), not with invalid angles like 315 or -45 on the back pivot.
"""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
MODELS = ASSETS / "models" / "block"
BLOCKSTATES = ASSETS / "blockstates"

TEMPLATE = json.loads((MODELS / "roof_frame.json").read_text(encoding="utf-8"))

STAGE_TEXTURES = {
    0: "materia:block/roof_tiles_0",
    1: "materia:block/roof_tiles_1",
    2: "materia:block/roof_tiles_2",
    3: "materia:block/roof_tiles_3",
    4: "materia:block/roof_tiles_4",
    5: "materia:block/roof_tiles_5",
    6: "materia:block/roof_tiles_6",
    7: "materia:block/roof_tiles_7",
    8: "materia:block/roof_tiles",
}

THATCH_PARTIAL_TEXTURE = "materia:block/roof_thatch_1"
THATCH_FULL_TEXTURE = "materia:block/thatch"

FACINGS = ["north", "east", "south", "west"]
Y_ROT = {"north": 0, "east": 90, "south": 180, "west": 270}


def model_for_stage(stage: int) -> dict:
    model = json.loads(json.dumps(TEMPLATE))
    model["textures"]["tiles"] = STAGE_TEXTURES[stage]
    return model


def model_for_thatch_texture(texture: str) -> dict:
    model = json.loads(json.dumps(TEMPLATE))
    model["textures"]["tiles"] = texture
    return model


def model_for_thatch_state(thatch: bool, stage: int) -> str:
    if not thatch:
        return f"materia:block/roof_tiles_{stage}"
    if stage == 0:
        return "materia:block/roof_tiles_thatch_1"
    if stage >= 8:
        return "materia:block/roof_tiles_thatch_full"
    return f"materia:block/roof_tiles_{stage}"


def main() -> None:
    for stage in range(9):
        path = MODELS / f"roof_tiles_{stage}.json"
        path.write_text(json.dumps(model_for_stage(stage), indent=4) + "\n", encoding="utf-8")

    (MODELS / "roof_tiles_thatch_1.json").write_text(
        json.dumps(model_for_thatch_texture(THATCH_PARTIAL_TEXTURE), indent=4) + "\n",
        encoding="utf-8",
    )
    (MODELS / "roof_tiles_thatch_full.json").write_text(
        json.dumps(model_for_thatch_texture(THATCH_FULL_TEXTURE), indent=4) + "\n",
        encoding="utf-8",
    )

    legacy_thatch_model = MODELS / "roof_tiles_thatch.json"
    if legacy_thatch_model.exists():
        legacy_thatch_model.unlink()

    variants = {}
    for facing in FACINGS:
        for stage in range(9):
            for thatch in (False, True):
                key = f"facing={facing},stage={stage},thatch={str(thatch).lower()}"
                variants[key] = {
                    "model": model_for_thatch_state(thatch, stage),
                    "y": Y_ROT[facing],
                }

    blockstate = {"variants": variants}
    (BLOCKSTATES / "roof_tiles.json").write_text(json.dumps(blockstate, indent=4) + "\n", encoding="utf-8")
    print("Generated roof_tiles models (0-8), thatch models, and blockstates")


if __name__ == "__main__":
    main()
