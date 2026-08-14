"""Bulk-replace steel terminology in Materia documentation."""
from pathlib import Path
import re

DOC = Path(r"c:\MCMods\Materia\documentation")
CHANGELOG = Path(r"c:\MCMods\Materia\CHANGELOG.md")

REPLACEMENTS = [
    (r"\bSteel Age\b", "Iron Age"),
    (r"\bsteel age\b", "iron age"),
    (r"\bsteel storyline\b", "refined iron storyline"),
    (r"\bsteel ingots\b", "iron ingots"),
    (r"\bsteel ingot\b", "iron ingot"),
    (r"\bSteel ingots\b", "Iron ingots"),
    (r"\bSteel ingot\b", "Iron ingot"),
    (r"\bsteelmaking\b", "iron smelting"),
    (r"\bSteelmaking\b", "Iron smelting"),
    (r"\bsteel components\b", "iron components"),
    (r"\bSteel components\b", "Iron components"),
    (r"\bsteel parts\b", "iron parts"),
    (r"\bsteel hammer\b", "iron hammer"),
    (r"\bSteel hammer\b", "Iron hammer"),
    (r"\bsteel tool\b", "iron tool"),
    (r"\bSteel tool\b", "Iron tool"),
    (r"\bsteel tools\b", "iron tools"),
    (r"\bSteel tools\b", "Iron tools"),
    (r"\bsteel armor\b", "iron armor"),
    (r"\bSteel armor\b", "Iron armor"),
    (r"\bsteel rod\b", "iron rod"),
    (r"\bSteel rod\b", "Iron rod"),
    (r"\bsteel plate\b", "iron plate"),
    (r"\bSteel plate\b", "Iron plate"),
    (r"\bsteel wire\b", "iron wire"),
    (r"\bSteel wire\b", "Iron wire"),
    (r"\bsteel pickaxe\b", "iron pickaxe"),
    (r"\bSteel pickaxe\b", "Iron pickaxe"),
    (r"\bsteel sword\b", "iron sword"),
    (r"\bSteel sword\b", "Iron sword"),
    (r"\bsteel rail\b", "iron rail"),
    (r"\bSteel rail\b", "Iron rail"),
    (r"\bsteel tongs\b", "iron tongs"),
    (r"\bSteel tongs\b", "Iron tongs"),
    (r"\bsteel pipe\b", "iron pipe"),
    (r"\bSteel pipe\b", "Iron pipe"),
    (r"\bsteel spear\b", "iron spear"),
    (r"\bSteel spear\b", "Iron spear"),
    (r"\bsteel knife\b", "iron knife"),
    (r"\bSteel knife\b", "Iron knife"),
    (r"\bsteel arrow\b", "iron arrow"),
    (r"\bSteel arrow\b", "Iron arrow"),
    (r"displayed as [“\"]Steel ingot[”\"]", "outputs `minecraft:iron_ingot` (vanilla iron ingot)"),
    (r"displayed as [“\"]Steel ingot[”\"]", "outputs `minecraft:iron_ingot` (vanilla iron ingot)"),
    (r"treats vanilla iron as \*\*steel\*\*", "uses vanilla `minecraft:iron_ingot` as the refined iron tier"),
    (r"treats vanilla iron as steel", "uses vanilla `minecraft:iron_ingot` as the refined iron tier"),
    (r"labeled as steel in-game", "use standard vanilla iron names"),
    (r"which is labeled as steel in-game", "which use standard vanilla iron names"),
    (r"\*\*steel\*\* advanced kiln", "**iron** advanced kiln"),
    (r"advanced kiln [“\"]steel[”\"] recipe", "advanced kiln iron smelting recipe"),
    (r"Progression \(Steel Age\)", "Progression (Iron Age)"),
    (r"What steel unlocks", "What refined iron unlocks"),
    (r"#materia:steel_hammers", "#materia:steel_hammers (iron-tier hammer tag; registry id unchanged)"),
]

SKIP_PATTERNS = ["flint_and_steel", "flint and steel"]


def should_skip(text: str, start: int) -> bool:
    window = text[max(0, start - 30) : start + 30].lower()
    return any(p in window for p in SKIP_PATTERNS)


def transform(text: str) -> str:
    for pattern, repl in REPLACEMENTS:
        def _sub(m):
            if should_skip(text, m.start()):
                return m.group(0)
            return repl

        text = re.sub(pattern, _sub, text)
    return text


def process_file(path: Path):
    original = path.read_text(encoding="utf-8")
    updated = transform(original)
    if updated != original:
        path.write_text(updated, encoding="utf-8")
        print(f"updated {path}")


for path in DOC.rglob("*.md"):
    process_file(path)

if CHANGELOG.exists():
    cl = CHANGELOG.read_text(encoding="utf-8")
    if "## Unreleased" in cl and "steel naming" not in cl.lower():
        insert = (
            "\n### Naming — remove “steel” terminology\n\n"
            "- **Wrought iron** (`materia:wrought_iron_*`, `materia:iron_*` tools/components/armor) now uses explicit **Wrought Iron** display names.\n"
            "- **Refined iron** tier (`materia:steel_*` registry ids, `minecraft:iron_ingot` output) now displays as **Iron** — vanilla iron tools/items use vanilla names again (removed lang overrides).\n"
            "- Documentation and Flemish (`nl_be`) translations updated to match; internal `steel_*` ids unchanged for compatibility.\n"
        )
        cl = cl.replace("## Unreleased\n", "## Unreleased\n" + insert)
        CHANGELOG.write_text(cl, encoding="utf-8")
        print("updated CHANGELOG")

print("done")
