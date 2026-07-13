#!/usr/bin/env python3
"""Append lang entries for decorative blocks (en_us + nl_be)."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
LANG_DIR = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia" / "lang"

SHUTTER_WOODS = [
    ("oak", "Oak", "Eiken"),
    ("spruce", "Spruce", "Spar"),
    ("birch", "Birch", "Berken"),
    ("jungle", "Jungle", "Oerwoud"),
    ("acacia", "Acacia", "Acacia"),
    ("dark_oak", "Dark Oak", "Donkere Eiken"),
    ("cherry", "Cherry", "Cherry"),
    ("mangrove", "Mangrove", "Mangrove"),
    ("rubber_wood", "Rubber Wood", "Rubberhout"),
    ("fig", "Fig", "Vijgen"),
    ("cedar", "Cedar", "Ceder"),
    ("eucalyptus", "Eucalyptus", "Eucalyptus"),
]

CURTAIN_COLORS = [
    ("white", "White", "Witte"),
    ("orange", "Orange", "Oranje"),
    ("magenta", "Magenta", "Magenta"),
    ("light_blue", "Light Blue", "Lichtblauwe"),
    ("yellow", "Yellow", "Gele"),
    ("lime", "Lime", "Limoengroene"),
    ("pink", "Pink", "Roze"),
    ("gray", "Gray", "Grijze"),
    ("light_gray", "Light Gray", "Lichtgrijze"),
    ("cyan", "Cyan", "Cyaan"),
    ("purple", "Purple", "Paarse"),
    ("blue", "Blue", "Blauwe"),
    ("brown", "Brown", "Bruine"),
    ("green", "Green", "Groene"),
    ("red", "Red", "Rode"),
    ("black", "Black", "Zwarte"),
    ("ochre", "Ochre", "Oker"),
    ("red_ochre", "Red Ochre", "Rode Oker"),
    ("lavender", "Lavender", "Lavendel"),
    ("indigo", "Indigo", "Indigo"),
    ("tyrian_purple", "Tyrian Purple", "Tyrisch Paarse"),
    ("taupe", "Taupe", "Taupe"),
    ("olive", "Olive", "Olijf"),
    ("charcoal_gray", "Charcoal Gray", "Houtskoolgrijze"),
    ("burgundy", "Burgundy", "Bordeaux"),
    ("teal", "Teal", "Teal"),
]

STONE_BLOCKS = [
    ("stone_tiles", "Stone Tiles", "Stenen Tegels"),
    ("stone_bricks_small", "Small Stone Bricks", "Kleine Stenen Bakstenen"),
    ("marble_bricks", "Marble Bricks", "Marmeren Bakstenen"),
    ("marble_bricks_small", "Small Marble Bricks", "Kleine Marmeren Bakstenen"),
    ("polished_marble", "Polished Marble", "Gepolijst Marmer"),
    ("marble_tiles", "Marble Tiles", "Marmeren Tegels"),
    ("limestone_bricks", "Limestone Bricks", "Kalksteen Bakstenen"),
    ("limestone_bricks_small", "Small Limestone Bricks", "Kleine Kalksteen Bakstenen"),
    ("polished_limestone", "Polished Limestone", "Gepolijst Kalksteen"),
    ("limestone_tiles", "Limestone Tiles", "Kalksteen Tegels"),
    ("limestone_chiseled", "Chiseled Limestone", "Gebeitelde Kalksteen"),
    ("sandstone_bricks", "Sandstone Bricks", "Zandsteen Bakstenen"),
    ("sandstone_tiles", "Sandstone Tiles", "Zandsteen Tegels"),
    ("blackstone_tiles", "Blackstone Tiles", "Blackstone Tegels"),
    ("stone_urn", "Stone Urn", "Stenen Urn"),
    ("stone_planter", "Stone Planter", "Stenen Plantenbak"),
    ("stone_balustrade", "Stone Balustrade", "Stenen Balustrade"),
    ("marble_urn", "Marble Urn", "Marmeren Urn"),
    ("marble_planter", "Marble Planter", "Marmeren Plantenbak"),
    ("marble_column", "Marble Column", "Marmeren Kolom"),
    ("limestone_urn", "Limestone Urn", "Kalksteen Urn"),
    ("limestone_planter", "Limestone Planter", "Kalksteen Plantenbak"),
    ("limestone_column", "Limestone Column", "Kalksteen Kolom"),
    ("sandstone_urn", "Sandstone Urn", "Zandsteen Urn"),
    ("sandstone_planter", "Sandstone Planter", "Zandsteen Plantenbak"),
    ("sandstone_column", "Sandstone Column", "Zandsteen Kolom"),
    ("blackstone_urn", "Blackstone Urn", "Blackstone Urn"),
    ("blackstone_planter", "Blackstone Planter", "Blackstone Plantenbak"),
    ("blackstone_column", "Blackstone Column", "Blackstone Kolom"),
    ("terracotta_urn", "Terracotta Urn", "Terracotta Urn"),
    ("terracotta_planter", "Terracotta Planter", "Terracotta Plantenbak"),
    ("stone_column", "Stone Column", "Stenen Kolom"),
    ("marble_cornice", "Marble Cornice", "Marmeren Corniche"),
    ("marble_bracket", "Marble Bracket", "Marmeren Console"),
]


def apply_entries(data: dict, lang: str) -> None:
    for wood_id, en_name, nl_name in SHUTTER_WOODS:
        label = f"{en_name} Shutters" if lang == "en" else f"{nl_name} Luiken"
        data[f"block.materia.{wood_id}_shutters"] = label
    for color_id, en_name, nl_name in CURTAIN_COLORS:
        label = f"{en_name} Curtains" if lang == "en" else f"{nl_name} Gordijnen"
        data[f"block.materia.{color_id}_curtains"] = label
    for block_id, en_label, nl_label in STONE_BLOCKS:
        data[f"block.materia.{block_id}"] = en_label if lang == "en" else nl_label


def main() -> None:
    for filename, lang in [("en_us.json", "en"), ("nl_be.json", "nl")]:
        path = LANG_DIR / filename
        data = json.loads(path.read_text(encoding="utf-8"))
        apply_entries(data, lang)
        path.write_text(json.dumps(data, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")
        print(f"Updated {path}")


if __name__ == "__main__":
    main()
