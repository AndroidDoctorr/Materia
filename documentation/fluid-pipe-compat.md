# Fluid and pipe compatibility

## Water pot

The **water pot** block entity exposes Forge **`IFluidHandler`** with a single tank of **water** only. Fill level `0–3` maps to **`0–3000` mB** (1000 mB per level). Any side can interact unless a future block change restricts it.

**Versions:** 1.20.1, 1.21.1, and **1.19.2** (water pot only on 1.19.2).

## Amphora (liquid mode)

When the amphora holds liquid (not solid items), it exposes **`IFluidHandler`**. Internal storage is still **bottle-based** in gameplay; for pipes, each bottle is treated as **250 mB**, up to **nine bottles** (2250 mB max) of one fluid type.

**Mapped fluids**

- **Vanilla / Forge:** water (`minecraft:water`), lava (`minecraft:lava`), milk (`ForgeMod` registered milk fluid).
- **Materia-registered** (for wine, brewing, oil, etc.): `ModFluids` pairs (`wine`, `grape_juice`, `olive_oil`, `vinegar`, `beer`, `beer_mash`) — no buckets or fluid blocks; registry exists so `FluidStack` and JEI/pipes agree on type.

**Versions:** Full registration and amphora fluid capability are implemented for **1.20.1** and **1.21.1** only. **1.19.2** does not use Forge’s `FluidType` stack the same way; that port keeps **water pot** fluid I/O only until a dedicated backport (e.g. `FluidAttributes`) is added.

## Item automation

**Baskets** (and similar) expose **`IItemHandler`** for hoppers, transporters, etc., with **no intentional face blocking** unless a block’s own code restricts it.

**Amphora (solid mode)** — `IItemHandler` is exposed **only** when the block is in **solid** storage mode. `WorldlyContainer#getSlotsForFace` returns **all six slots on every face** in that mode. In **empty** or **liquid** mode, there are **no item slots** for automation (fluids use `IFluidHandler` instead). Nested amphorae cannot be inserted as items (rejected in `setItem` / `canPlaceItemThroughFace`).

**Water pot** — single-slot **`ItemStackHandler`** plus **`IFluidHandler`** for water; capability queries do not restrict by face, so pipes and hoppers can use any side unless you add a block-specific restriction later.

## Tags

Optional `data/materia/tags/fluids/` entries can be added later to align with `forge:fluid_type` / cross-mod fluid tags; current focus is capability exposure and stable `FluidStack` types.
