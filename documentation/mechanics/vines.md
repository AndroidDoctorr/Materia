## Vines (wisteria + grapes + hops)

Materia has two “vine-like” plant systems:

- **Wisteria**: yields seeds (and sometimes plant fiber)
- **Grapes**: yields grapes + seeds
- **Hops**: yields hops + seeds

Both exist as:

- **Wild vines** that grow on trees in worldgen
- **Planted vines** that start like a crop on farmland, then spread onto nearby supports (trellises/posts/joists)

## Wild vines (found on trees)

Wild vine blocks:

- `materia:wild_grape_vine`
- `materia:wild_wisteria_vine`
- `materia:wild_hops_vine`

Behavior:

- found attached to logs in the world
- right-clicking can drop **seeds** (and may also drop **plant fiber**)

## Planting and growth (farmland → supports)

Seed items:

- `materia:grape_seeds` → places `materia:grape_vine`
- `materia:wisteria_seeds` → places `materia:wisteria_vine`
- `materia:hops_seeds` → places `materia:hops_vine`

How it grows:

1) **Plant the seeds on farmland** (like a normal crop)
2) Once the vine is mature, it can **attach to nearby supports** (turning the support block into a “has vine” variant)

Supports are defined by block tags:

- [`#materia:grape_vine_supports`](../reference/tags/plants-and-farming.md#materiagrape_vine_supports)
- [`#materia:wisteria_vine_supports`](../reference/tags/plants-and-farming.md#materiawisteria_vine_supports)
- `#materia:hops_vine_supports`

In practice, this includes:

- posts
- trellises
- joists

Future note:

- You mentioned fences may be supported later (not currently).

## Supports: grapes / flowers and harvesting

When a support has vines, it can eventually gain:

- **Grapes** (for grape vines)
- **Flowers** (for wisteria)

Harvesting:

- right-click supports with **grapes** to get **grapes + seeds**
- right-click supports with **wisteria flowers** to get **seeds** (and sometimes plant fiber)

## Hanging growth

If a vine-covered support has **air below**, it can place a hanging block:

- `materia:grapes_hanging`
- `materia:wisteria_hanging`
- `materia:hops_hanging`

These can also be harvested by right-clicking.

## Known issue (current bug)

- **Wisteria and grape vines can overlap**, leading to an unsupported “both vines on the same block” state (and missing visuals for that combination).
- Intended behavior (future): they should not be able to grow together / interfere.

## Related pages

- [Grape vine](../content/blocks/grape-vine.md)
- [Wisteria vine](../content/blocks/wisteria-vine.md)
- [Trellises (wood variants)](../content/blocks/wood-trellises.md)
- [Hops and beer](hops-and-beer.md)
