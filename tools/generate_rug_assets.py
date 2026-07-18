#!/usr/bin/env python3
"""Generate blockstates, models, loot, and split block textures for rug variants."""
import json
from pathlib import Path

try:
    from PIL import Image
except ImportError:
    Image = None

ROOT = Path(__file__).resolve().parents[1]
ASSETS = ROOT / "shared" / "src" / "main" / "resources" / "assets" / "materia"
DATA = ROOT / "shared" / "src" / "main" / "resources" / "data" / "materia"
LOOT = DATA / "loot_tables" / "blocks"
CHESTS = DATA / "loot_tables" / "chests"
RECIPES = DATA / "recipes"
BANNER = DATA / "banner_patterns"
LANG_EN = ASSETS / "lang" / "en_us.json"
LANG_NL = ASSETS / "lang" / "nl_be.json"
MODELS = ASSETS / "models" / "block"
TEXTURES = ASSETS / "textures" / "block"

RUG_COLORS = ("red", "blue", "green", "purple")
CRAFTABLE_PATTERNS = ("1", "2", "3", "4")
NEW_CRAFTABLE_PATTERNS = ("3", "4")
SPECIAL_RUGS = ("5", "6", "7", "8", "9", "10")
RUG_PATTERN_ITEMS = ("rug_base", "rug_1_pattern", "rug_2_pattern", "rug_3_pattern", "rug_4_pattern")

PATTERN_NAMES = {
    "1": ("Medallion", "Medallion"),
    "2": ("Ornate", "Ornaat"),
    "3": ("Rosette", "Rozet"),
    "4": ("Lattice", "Ruit"),
}

SPECIAL_RUG_NAMES = {
    "5": ("Dragon", "Draak"),
    "6": ("Diamonds", "Diamanten"),
    "7": ("Navajo", "Navajo"),
    "8": ("Welcome", "Welkom"),
    "9": ("Agrabah", "Agrabah"),
    "10": ("Rainbow", "Regenboog"),
}

PATTERN_RECIPE_DYE = {
    "3": "materia:lavender",
    "4": "materia:olive",
}

COLOR_LANG = {
    "red": ("Red", "Rood"),
    "blue": ("Blue", "Blauw"),
    "green": ("Green", "Groen"),
    "purple": ("Purple", "Paars"),
}

RARE_RUG_CHEST_POOLS = {
    "simple_dungeon": ("tier1", 0.12),
    "abandoned_mineshaft": ("tier1", 0.10),
    "igloo": ("tier1", 0.10),
    "shipwreck_supply": ("tier1", 0.08),
    "desert_pyramid": ("tier2", 0.10),
    "jungle_temple": ("tier2", 0.10),
    "shipwreck": ("tier2", 0.08),
    "stronghold": ("tier3", 0.08),
    "woodland_mansion": ("tier3", 0.07),
    "buried_treasure": ("tier3", 0.06),
}

RARE_RUG_TIERS = {
    "tier1": [("5", 50), ("6", 35), ("7", 15)],
    "tier2": [("6", 20), ("7", 35), ("8", 45)],
    "tier3": [("8", 40), ("9", 35), ("10", 25)],
}


def write_json(path: Path, data: dict) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(data, indent=4) + "\n", encoding="utf-8")


def split_block_textures(source_name: str, foot_id: str, head_id: str) -> tuple[str, str]:
    """Crop a 16x32 rug texture into separate 16x16 foot/head block textures."""
    source = TEXTURES / f"{source_name}.png"
    foot_path = TEXTURES / f"{foot_id}.png"
    head_path = TEXTURES / f"{head_id}.png"

    if Image is None:
        raise SystemExit("Pillow is required: pip install Pillow")

    if not source.exists():
        raise SystemExit(f"Missing rug source texture: {source}")

    img = Image.open(source).convert("RGBA")
    if img.size != (16, 32):
        raise SystemExit(f"Expected 16x32 rug texture at {source}, got {img.size}")

    img.crop((0, 0, 16, 16)).save(foot_path)
    img.crop((0, 16, 16, 32)).save(head_path)
    return foot_id, head_id


def rug_foot_model(texture: str) -> dict:
    side_uv = [0, 15, 16, 16]
    return {
        "ambientocclusion": False,
        "textures": {"rug": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [
            {
                "from": [0, 0, 0],
                "to": [16, 1, 16],
                "faces": {
                    "up": {"uv": [0, 0, 16, 16], "texture": "#rug"},
                    "down": {"uv": [0, 0, 16, 16], "texture": "#rug", "cullface": "down"},
                    "north": {"uv": side_uv, "texture": "#rug", "cullface": "north"},
                    "south": {"uv": side_uv, "texture": "#rug", "cullface": "south"},
                    "west": {"uv": side_uv, "texture": "#rug", "cullface": "west"},
                    "east": {"uv": side_uv, "texture": "#rug", "cullface": "east"},
                },
            }
        ],
    }


def rug_head_model(texture: str) -> dict:
    side_uv = [0, 0, 16, 1]
    return {
        "ambientocclusion": False,
        "textures": {"rug": f"materia:block/{texture}", "particle": f"materia:block/{texture}"},
        "elements": [
            {
                "from": [0, 0, 0],
                "to": [16, 1, 16],
                "faces": {
                    "up": {"uv": [0, 0, 16, 16], "texture": "#rug"},
                    "down": {"uv": [0, 0, 16, 16], "texture": "#rug", "cullface": "down"},
                    "north": {"uv": side_uv, "texture": "#rug", "cullface": "north"},
                    "south": {"uv": side_uv, "texture": "#rug", "cullface": "south"},
                    "west": {"uv": side_uv, "texture": "#rug", "cullface": "west"},
                    "east": {"uv": side_uv, "texture": "#rug", "cullface": "east"},
                },
            }
        ],
    }


def blockstate_variants(block_id: str) -> dict:
    variants = {}
    for facing, y in [("north", 0), ("south", 180), ("east", 90), ("west", 270)]:
        variants[f"facing={facing},part=foot"] = {"model": f"materia:block/{block_id}_foot", "y": y}
        variants[f"facing={facing},part=head"] = {"model": f"materia:block/{block_id}_head", "y": y}
    return variants


def block_loot(block_id: str) -> dict:
    return {
        "type": "minecraft:block",
        "pools": [
            {
                "rolls": 1,
                "entries": [{"type": "minecraft:item", "name": f"materia:{block_id}"}],
                "conditions": [
                    {"condition": "minecraft:survives_explosion"},
                    {
                        "condition": "minecraft:block_state_property",
                        "block": f"materia:{block_id}",
                        "properties": {"part": "foot"},
                    },
                ],
            }
        ],
    }


def generate_colored_rug(pattern: str, color: str) -> None:
    block_id = f"rug_{pattern}_{color}"
    foot_tex, head_tex = split_block_textures(
        block_id,
        f"{block_id}_foot",
        f"{block_id}_head",
    )
    write_json(MODELS / f"{block_id}_foot.json", rug_foot_model(head_tex))
    write_json(MODELS / f"{block_id}_head.json", rug_head_model(foot_tex))
    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": blockstate_variants(block_id)})
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {
            "parent": "minecraft:item/generated",
            "textures": {"layer0": f"materia:item/rug_{pattern}_{color}"},
        },
    )
    write_json(LOOT / f"{block_id}.json", block_loot(block_id))


def generate_special_rug(rug_id: str) -> None:
    block_id = f"rug_{rug_id}"
    foot_tex, head_tex = split_block_textures(block_id, f"{block_id}_foot", f"{block_id}_head")
    write_json(MODELS / f"{block_id}_foot.json", rug_foot_model(head_tex))
    write_json(MODELS / f"{block_id}_head.json", rug_head_model(foot_tex))
    write_json(ASSETS / "blockstates" / f"{block_id}.json", {"variants": blockstate_variants(block_id)})
    write_json(
        ASSETS / "models" / "item" / f"{block_id}.json",
        {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{block_id}"}},
    )
    write_json(LOOT / f"{block_id}.json", block_loot(block_id))


def generate_pattern_recipe(pattern: str) -> None:
    write_json(
        RECIPES / f"rug_{pattern}_pattern.json",
        {
            "type": "minecraft:crafting_shapeless",
            "ingredients": [
                {"item": "minecraft:paper"},
                {"item": "minecraft:gold_nugget"},
                {"item": PATTERN_RECIPE_DYE[pattern]},
            ],
            "result": {"item": f"materia:rug_{pattern}_pattern", "count": 1},
        },
    )


def generate_banner_pattern(pattern: str) -> None:
    en_name, _ = PATTERN_NAMES[pattern]
    write_json(
        BANNER / f"rug_{pattern}.json",
        {
            "asset_id": f"materia:rug_{pattern}",
            "translation_key": f"banner_pattern.materia.rug_{pattern}",
        },
    )


def rare_rug_pool(tier: str, chance: float) -> dict:
    entries = []
    for rug_id, weight in RARE_RUG_TIERS[tier]:
        entries.append({"type": "minecraft:item", "name": f"materia:rug_{rug_id}", "weight": weight})
    return {
        "rolls": 1,
        "conditions": [{"condition": "minecraft:random_chance", "chance": chance}],
        "entries": entries,
    }


def patch_chest_loot() -> None:
    for chest_name, (tier, chance) in RARE_RUG_CHEST_POOLS.items():
        path = CHESTS / f"{chest_name}.json"
        if not path.exists():
            print(f"Skipping missing chest loot table: {path}")
            continue
        data = json.loads(path.read_text(encoding="utf-8"))
        pool = rare_rug_pool(tier, chance)
        pools = data.setdefault("pools", [])
        pools = [p for p in pools if not _is_rare_rug_pool(p)]
        pools.append(pool)
        data["pools"] = pools
        write_json(path, data)
        print(f"Patched rare rug loot into {chest_name}.json ({tier}, chance={chance})")


def _is_rare_rug_pool(pool: dict) -> bool:
    for entry in pool.get("entries", []):
        name = entry.get("name", "")
        if name.startswith("materia:rug_") and name.replace("materia:rug_", "").isdigit():
            rug_num = int(name.replace("materia:rug_", ""))
            if rug_num >= 5:
                return True
    return False


def generate_items() -> None:
    for name in RUG_PATTERN_ITEMS:
        write_json(
            ASSETS / "models" / "item" / f"{name}.json",
            {"parent": "minecraft:item/generated", "textures": {"layer0": f"materia:item/{name}"}},
        )


def merge_lang() -> None:
    lang_en = json.loads(LANG_EN.read_text(encoding="utf-8"))
    lang_nl = json.loads(LANG_NL.read_text(encoding="utf-8"))

    for pattern in CRAFTABLE_PATTERNS:
        en_name, nl_name = PATTERN_NAMES[pattern]
        lang_en[f"item.materia.rug_{pattern}_pattern"] = f"{en_name} Rug Pattern"
        lang_nl[f"item.materia.rug_{pattern}_pattern"] = f"{nl_name.strip()} Tapijtpatroon"
        lang_en[f"banner_pattern.materia.rug_{pattern}"] = f"{en_name} Rug"
        lang_nl[f"banner_pattern.materia.rug_{pattern}"] = f"{nl_name.strip()} Tapijt"
        for color in RUG_COLORS:
            color_en, color_nl = COLOR_LANG[color]
            block_id = f"rug_{pattern}_{color}"
            lang_en[f"block.materia.{block_id}"] = f"{color_en} {en_name} Rug"
            lang_nl[f"block.materia.{block_id}"] = f"{color_nl} {nl_name.strip()} Tapijt"

    for rug_id in SPECIAL_RUGS:
        en_name, nl_name = SPECIAL_RUG_NAMES[rug_id]
        block_id = f"rug_{rug_id}"
        lang_en[f"block.materia.{block_id}"] = f"{en_name} Rug"
        lang_nl[f"block.materia.{block_id}"] = f"{nl_name} Tapijt"

    write_json(LANG_EN, lang_en)
    write_json(LANG_NL, lang_nl)


def main() -> None:
    for pattern in NEW_CRAFTABLE_PATTERNS:
        for color in RUG_COLORS:
            generate_colored_rug(pattern, color)
            print(f"Generated rug_{pattern}_{color}")
        generate_pattern_recipe(pattern)
        generate_banner_pattern(pattern)

    for rug_id in SPECIAL_RUGS:
        generate_special_rug(rug_id)
        print(f"Generated rug_{rug_id}")

    generate_items()
    merge_lang()
    print("Done.")


if __name__ == "__main__":
    main()
