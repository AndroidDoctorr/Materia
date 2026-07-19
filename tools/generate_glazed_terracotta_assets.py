#!/usr/bin/env python3
"""Generate glazed terracotta blocks, assets, and smelting recipes for Materia dye colors."""
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
RECIPES = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "recipes"
LOOT = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia" / "loot_tables" / "blocks"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"
MODBLOCKS_VERSIONS = [
    ROOT / "1.18.2" / "src" / "main" / "java" / "com" / "torr" / "materia" / "ModBlocks.java",
    ROOT / "1.19.2" / "src" / "main" / "java" / "com" / "torr" / "materia" / "ModBlocks.java",
    ROOT / "1.20.1" / "src" / "main" / "java" / "com" / "torr" / "materia" / "ModBlocks.java",
    ROOT / "1.21.1" / "src" / "main" / "java" / "com" / "torr" / "materia" / "ModBlocks.java",
]

# color_id, vanilla glazed terracotta enum suffix, English name, Dutch name
COLORS = [
    ("ochre", "ORANGE", "Ochre", "Oker"),
    ("red_ochre", "RED", "Red Ochre", "Rode Oker"),
    ("olive", "GREEN", "Olive", "Olijf"),
    ("burgundy", "RED", "Burgundy", "Bordeaux"),
    ("tan", "BROWN", "Tan", "Tan"),
    ("teal", "CYAN", "Teal", "Teal"),
    ("indigo", "BLUE", "Indigo", "Indigo"),
    ("tyrian_purple", "PURPLE", "Tyrian Purple", "Tyrisch Paars"),
    ("charcoal_gray", "BLACK", "Charcoal Gray", "Houtskoolgrijs"),
    ("taupe", "GRAY", "Taupe", "Taupe"),
]

MARKER = "        // BUILDING BLOCKS"
REGISTRATION_MARKER = "        // GLAZED TERRACOTTA"


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def glazed_blockstate(block_id: str) -> dict:
    model = f"materia:block/{block_id}"
    return {
        "variants": {
            "facing=south": {"model": model},
            "facing=west": {"model": model, "y": 270},
            "facing=north": {"model": model, "y": 180},
            "facing=east": {"model": model, "y": 90},
        }
    }


def registration_lines() -> list[str]:
    lines = ["        // GLAZED TERRACOTTA"]
    for color_id, vanilla_suffix, _, _ in COLORS:
        const = color_id.upper() + "_GLAZED_TERRACOTTA"
        lines.append(
            f'        public static final RegistryObject<Block> {const} = registerBlock("{color_id}_glazed_terracotta",'
        )
        lines.append(
            f"                        () -> new net.minecraft.world.level.block.GlazedTerracottaBlock("
            f"BlockBehaviour.Properties.copy(Blocks.{vanilla_suffix}_GLAZED_TERRACOTTA)));"
        )
    lines.append("")
    return lines


def patch_modblocks() -> None:
    reg_block = "\n".join(registration_lines())
    for path in MODBLOCKS_VERSIONS:
        text = path.read_text(encoding="utf-8")
        if REGISTRATION_MARKER in text:
            start = text.index(REGISTRATION_MARKER)
            end = text.index(MARKER, start)
            text = text[:start] + reg_block + text[end:]
        else:
            text = text.replace(MARKER, reg_block + MARKER, 1)
        path.write_text(text, encoding="utf-8")


def generate_assets() -> None:
    lang_en = {}
    lang_nl = {}

    for color_id, _, en_name, nl_name in COLORS:
        block_id = f"{color_id}_glazed_terracotta"
        terracotta_id = f"materia:{color_id}_terracotta"

        write_json(ASSETS / "blockstates" / f"{block_id}.json", glazed_blockstate(block_id))
        write_json(
            ASSETS / "models" / "block" / f"{block_id}.json",
            {
                "parent": "minecraft:block/cube_all",
                "textures": {"all": f"materia:block/{block_id}"},
            },
        )
        write_json(
            ASSETS / "models" / "item" / f"{block_id}.json",
            {"parent": f"materia:block/{block_id}"},
        )
        write_json(
            LOOT / f"{block_id}.json",
            {
                "type": "minecraft:block",
                "pools": [
                    {
                        "rolls": 1,
                        "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                        "conditions": [{"condition": "minecraft:survives_explosion"}],
                    }
                ],
            },
        )
        write_json(
            RECIPES / f"{block_id}_from_{color_id}_terracotta.json",
            {
                "type": "materia:kiln",
                "ingredient": {"item": terracotta_id},
                "result": {"item": f"materia:{block_id}", "count": 1},
                "cookingtime": 200,
                "experience": 0.1,
            },
        )
        write_json(
            RECIPES / f"{block_id}_from_{color_id}_terracotta_smelting.json",
            {
                "type": "minecraft:smelting",
                "ingredient": {"item": terracotta_id},
                "result": f"materia:{block_id}",
                "experience": 0.1,
                "cookingtime": 200,
            },
        )

        lang_en[f"block.materia.{block_id}"] = f"{en_name} Glazed Terracotta"
        lang_nl[f"block.materia.{block_id}"] = f"{nl_name} Glazuur Terracotta"

    for path, entries in ((LANG_EN, lang_en), (LANG_NL, lang_nl)):
        lang = json.loads(path.read_text(encoding="utf-8"))
        lang.update(entries)
        write_json(path, lang)


def main() -> None:
    generate_assets()
    patch_modblocks()
    print(f"Generated {len(COLORS)} glazed terracotta colors and patched ModBlocks")


if __name__ == "__main__":
    main()
