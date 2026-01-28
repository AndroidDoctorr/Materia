## Hot metals

Some metal items can become **hot**. Hot items are tagged as:

- `#materia:heatable_metals` (see [tag reference](../reference/tags/heat-and-hot-items.md#materiaheatable_metals), code: `HotMetalEventHandler`)

When a hot item is in your inventory, it will burn you over time.

## Damage rules

Hot metal burns you at different rates depending on where it is:

- **Main hand / offhand**: every 1 second
- **Hotbar**: every 3 seconds
- **Main inventory**: every 5 seconds

When you’re taking damage you’ll also briefly catch fire and see smoke effects.

## How to handle hot metal safely

Use **tongs** to handle hot metal.

With tongs you can:

- **Extract** hot metal from kiln/furnace outputs
- **Place** hot metal into anvil input slots
- **Quench** hot items in a water pot

Related:

- [Tongs](../content/items/tongs.md)
- [Anvils](anvils.md)
- [Kilns](kilns.md)
- [Water pot](water-pot.md)
