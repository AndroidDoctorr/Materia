import argparse
from pathlib import Path


def rel_java_paths(root: Path) -> set[str]:
    root = root.resolve()
    out: set[str] = set()
    for p in root.rglob("*.java"):
        if not p.is_file():
            continue
        rel = p.resolve().relative_to(root).as_posix()
        out.add(rel)
    return out


def write_list(path: Path, items: list[str]) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text("\n".join(items) + ("\n" if items else ""), encoding="utf-8")


def main() -> int:
    ap = argparse.ArgumentParser()
    ap.add_argument("--java18", required=True, type=Path)
    ap.add_argument("--java19", required=True, type=Path)
    ap.add_argument("--outdir", required=True, type=Path)
    args = ap.parse_args()

    a = rel_java_paths(args.java18)
    b = rel_java_paths(args.java19)

    only_a = sorted(a - b)
    only_b = sorted(b - a)

    write_list(args.outdir / "java-only-in-1.18.2.txt", only_a)
    write_list(args.outdir / "java-only-in-1.19.2.txt", only_b)

    print(f"1.18.2 Java files: {len(a)}")
    print(f"1.19.2 Java files: {len(b)}")
    print(f"Only in 1.18.2: {len(only_a)} (written to {args.outdir / 'java-only-in-1.18.2.txt'})")
    print(f"Only in 1.19.2: {len(only_b)} (written to {args.outdir / 'java-only-in-1.19.2.txt'})")

    if only_a:
        print("\nFirst 25 only-in-1.18.2:")
        for x in only_a[:25]:
            print(f"  {x}")
    if only_b:
        print("\nFirst 25 only-in-1.19.2:")
        for x in only_b[:25]:
            print(f"  {x}")

    return 0


if __name__ == "__main__":
    raise SystemExit(main())

