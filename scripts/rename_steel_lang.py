import json
from pathlib import Path

root = Path(r"c:\MCMods\Materia\shared\src\main\resources\assets\materia\lang")

minecraft_iron_overrides = [
    "item.minecraft.iron_pickaxe",
    "item.minecraft.iron_ingot",
    "item.minecraft.iron_nugget",
    "item.minecraft.iron_shovel",
    "item.minecraft.iron_axe",
    "item.minecraft.iron_sword",
    "item.minecraft.iron_hoe",
    "item.minecraft.iron_helmet",
    "item.minecraft.iron_chestplate",
    "item.minecraft.iron_leggings",
    "item.minecraft.iron_boots",
]


def update_en():
    en_path = root / "en_us.json"
    en = json.loads(en_path.read_text(encoding="utf-8"))

    for key in minecraft_iron_overrides:
        en.pop(key, None)

    for key, value in list(en.items()):
        if key.startswith("item.materia.steel_") and value.startswith("Steel "):
            en[key] = "Iron " + value[len("Steel ") :]

    wrought_keys = [
        k for k in en if k.startswith("item.materia.iron_")
    ] + [
        "block.materia.iron_anvil",
        "container.iron_anvil",
        "jei.materia.iron_anvil",
        "item.materia.thick_iron_plate",
    ]
    for key in wrought_keys:
        if key not in en:
            continue
        value = en[key]
        if value.startswith("Wrought Iron "):
            continue
        if key == "item.materia.thick_iron_plate":
            en[key] = "Thick Wrought Iron Plate"
        elif key in {
            "block.materia.iron_anvil",
            "container.iron_anvil",
            "jei.materia.iron_anvil",
        }:
            en[key] = "Wrought Iron Anvil"
        elif value.startswith("Iron "):
            en[key] = "Wrought Iron " + value[len("Iron ") :]

    en["advancements.materia.carbon_temper.description"] = (
        "Smelt refined iron—hold a vanilla iron ingot from Materia's coke-fuelled advanced kiln recipe."
    )

    en_path.write_text(json.dumps(en, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")


def update_nl():
    nl_path = root / "nl_be.json"
    nl = json.loads(nl_path.read_text(encoding="utf-8"))

    for key in minecraft_iron_overrides:
        nl.pop(key, None)

    for key, value in list(nl.items()):
        if key.startswith("item.materia.steel_"):
            if value.startswith("Stalen "):
                nl[key] = "IJzeren " + value[len("Stalen ") :]
            elif value == "Staaldraad":
                nl[key] = "IJzerdraad"

    wrought_keys = [
        k for k in nl if k.startswith("item.materia.iron_")
    ] + [
        "block.materia.iron_anvil",
        "container.iron_anvil",
        "jei.materia.iron_anvil",
        "item.materia.thick_iron_plate",
    ]
    for key in wrought_keys:
        if key not in nl:
            continue
        value = nl[key]
        if value.startswith("Smeedijzer"):
            continue
        if key == "item.materia.thick_iron_plate":
            nl[key] = "Dikke Smeedijzerplaat"
        elif key in {
            "block.materia.iron_anvil",
            "container.iron_anvil",
            "jei.materia.iron_anvil",
        }:
            nl[key] = "Smeedijzeren Aambeeld"
        elif value.startswith("IJzeren "):
            nl[key] = "Smeedijzeren " + value[len("IJzeren ") :]
        elif value == "IJzerplaat":
            nl[key] = "Smeedijzerplaat"
        elif value.startswith("IJzer"):
            nl[key] = "Smeedijzer" + value[len("IJzer") :]

    if "advancements.materia.carbon_temper.description" in nl:
        nl["advancements.materia.carbon_temper.description"] = (
            "Smelt geraffineerd ijzer—houd een vanilla ijzerstaaf uit Materia's koks-gestookte ovenrecept."
        )

    nl_path.write_text(json.dumps(nl, indent=4, ensure_ascii=False) + "\n", encoding="utf-8")


if __name__ == "__main__":
    update_en()
    update_nl()
    print("lang updated")
