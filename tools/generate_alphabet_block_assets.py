#!/usr/bin/env python3
"""Generate alphabet block models and blockstates from characters/ textures.

Each block packs six glyphs (one per cube face). Blockstates use attach_face +
rotation (24 orientations) so any face can point any direction upright.

Run after editing CHARACTER_SETS or adding textures under:
  shared/src/main/resources/assets/materia/textures/block/characters/

This script is scaffolding for the 1.2.x experiment — not wired to registration yet.
"""
from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
CHAR_DIR = ASSETS / "textures" / "block" / "characters"
MODELS = ASSETS / "models" / "block"
BLOCKSTATES = ASSETS / "blockstates"

# Six texture stems per block item (no .png). Order: up, down, north, south, east, west.
CHARACTER_SETS: list[tuple[str, list[str]]] = [
    ("alphabet_block_ab", ["A", "B", "C", "D", "E", "F"]),
    ("alphabet_block_gh", ["G", "H", "I", "J", "K", "L"]),
    ("alphabet_block_mn", ["M", "N", "O", "P", "Q", "R"]),
    ("alphabet_block_st", ["S", "T", "U", "V", "W", "X"]),
    ("alphabet_block_yz0", ["Y", "Z", "0", "1", "2", "3"]),
    ("alphabet_block_456", ["4", "5", "6", "7", "8", "9"]),
    ("alphabet_block_punct1", ["cm", "pd", "qm", "ep", "ap", "ht"]),
    ("alphabet_block_punct2", ["bs", "as", "ds", "ps", "pl", "sc"]),
]

FACE_KEYS = ("up", "down", "north", "south", "east", "west")

# attach_face: floor | ceiling | wall + facing + rotation (0-3)
# y/x rotations applied to base cube model (see vanilla sign / skull patterns).
ORIENTATIONS: list[tuple[int, int, str]] = [
    (0, 0, "floor"),
    (0, 90, "floor"),
    (0, 180, "floor"),
    (0, 270, "floor"),
    (180, 0, "ceiling"),
    (180, 90, "ceiling"),
    (180, 180, "ceiling"),
    (180, 270, "ceiling"),
    (90, 0, "north"),
    (90, 90, "east"),
    (90, 180, "south"),
    (90, 270, "west"),
]


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def block_model(block_id: str, stems: list[str]) -> dict:
    textures = {
        face: f"materia:block/characters/{stem}"
        for face, stem in zip(FACE_KEYS, stems)
    }
    textures["particle"] = "minecraft:block/white_concrete"
    return {
        "parent": "minecraft:block/cube",
        "textures": textures,
    }


def blockstate(block_id: str) -> dict:
    variants: dict[str, dict] = {}
    for rot_index, (x, y, attach) in enumerate(ORIENTATIONS):
        key = f"attach={attach},rotation={rot_index % 4}"
        model = {"model": f"materia:block/{block_id}"}
        if x or y:
            model["x"] = x
            model["y"] = y
        variants[key] = model
    return {"variants": variants}


def main() -> None:
    missing: list[str] = []
    for block_id, stems in CHARACTER_SETS:
        for stem in stems:
            if not (CHAR_DIR / f"{stem}.png").exists():
                missing.append(stem)
        write_json(MODELS / f"{block_id}.json", block_model(block_id, stems))
        write_json(BLOCKSTATES / f"{block_id}.json", blockstate(block_id))
        print(f"Generated {block_id}")

    if missing:
        print("Missing textures:", ", ".join(sorted(set(missing))))
    else:
        print("All referenced character textures exist.")


if __name__ == "__main__":
    main()
