## Hand cart

The **hand cart** is Materia’s draft-pulled land vehicle: a wheeled chest you ride, hitch animals to, and optionally cover for travel and shelter. It replaces vanilla boat-style paddling — the cart only moves when a leashed draft team pulls it while you steer.

Source of truth:

- Entity: `CartEntity` in each version’s `.../entity/` package
- Items: `ModCarts`, `ModItems` (`cart_wheel`, cart covers, per-wood cart items)
- Recipes: `shared/src/main/resources/data/materia/recipes/*_cart*.json`
- Destroy loot: `shared/src/main/resources/data/materia/loot_tables/entities/cart.json`
- Phantom shelter: `CartPhantomHandler`
- Cart sleep: `CartSleepHandler`, `CartMenu`

## Crafting

### Cart base (hull)

Shaped recipe per wood type — six **smooth planks** of that wood plus **`#materia:all_nails`** in a “U” pattern.

Example: `shared/src/main/resources/data/materia/recipes/oak_cart_base.json`

### Finished cart

Shapeless assembly:

- 1× cart base (wood-specific)
- 1× crafting table
- 1× chest
- 1× **`#materia:all_beds`**
- 4× **`materia:cart_wheel`**

Example: `shared/src/main/resources/data/materia/recipes/oak_cart.json`

Wood variants include vanilla logs (oak through pale oak), crimson/warped, and Materia woods (fig, cedar, eucalyptus, rubber wood, etc.). Each wood has its own item id (`materia:oak_cart`, `materia:cedar_cart`, …).

### Cart wheels

- **`materia:cart_wheel`** — `materia:wooden_wheel` + `materia:iron_band`
- **`materia:wooden_wheel`** — early wooden wheel (used in cart wheel recipe and minecart axle chain)

Iron-anvil route also exists for minecart wheels from plates (`iron_anvil/minecart_wheel_from_plate.json`).

### Cart covers

Dyed fabric covers (`materia:*_cart_cover`) are crafted separately and applied in-world. All Materia dye colors that have cover recipes can be used.

## Riding and driving

- **Mount** like a boat (right-click empty cart).
- **W / S** — forward / reverse intent along the draft heading (reverse coasts; no paddle acceleration).
- **A / D** — steer the **draft team heading**, not the player’s look direction.
- The cart **does not** use vanilla boat paddle logic.

Movement requires at least one **leashed draft animal** hitched to the cart (see below). Without a team, the cart coasts to a stop.

### Draft team

Use a **lead** on the cart:

- **Lead + use** — attach a nearby eligible mob, or transfer a mob you are already leading.
- **Sneak + lead + use** — release all hitched mobs back to you.

Eligible draft animals (pull strength is approximate):

| Mob | Pull |
|---|---:|
| Horse (tamed) | 1.0 |
| Donkey / mule (tamed) | 0.85 |
| Llama | 0.65 |
| Cow, sheep, pig, goat | 0.35 |
| Other leashed mobs | 0.2 |

Extra animals help with **diminishing returns** (second and later animals add ~55% of their raw pull). Untamed horses do not draft.

Draft animals are positioned ahead of the cart and move with the team heading. Hitch state persists in cart NBT.

### Surfaces and speed

Speed depends on blocks under the cart footprint:

- **`#materia:cart_fast_surfaces`** (paths, smooth planks, paved stone, etc.) — up to **2×** speed when the whole footprint is on fast surfaces.
- Separate surface tags slow or affect rolling on snow, sand, gravel, grass, dirt, cobble, wood, and stone (used for movement sounds as well as physics sampling).

The cart can **ford shallow water** (`isInWater()`); wheel sounds switch to the water clip while wading.

### Sounds

- **Wheel rolling** — surface-specific clips (`entity.cart.move_*`), replayed on a ~2.7s loop while moving on ground or in water; volume/pitch scale with speed. Plays immediately when movement starts or the dominant surface changes.
- **Draft hooves** — vanilla `HORSE_STEP` / `HORSE_GALLOP` for hitched **horses** only, interval ~8–18 ticks based on speed (independent of wheel sounds).

Rolling clips do **not** cut off instantly when the cart stops; stopping mid-clip is a known polish gap for a future pass.

## Storage

- **27 slots** — same capacity as a single chest.
- **Empty hand + use while riding** — open the cart inventory (`CartMenu`).
- Chest contents, health, wood type, cover, lantern, draft team, and custom name persist in the item when picked up.

## Cover, lantern, repair

While **not** sneaking:

- **Cart cover item** — applies a dyed cover (one cover per cart).
- **Lantern** — mounts a lantern on the cart; at night or during thunderstorms it places a small **light block** ahead of the cart for visibility.
- **Matching smooth plank** — repairs **10 HP** per plank (wood type must match the hull).

Wood types differ in **mass**, **toughness**, and **damage taken** (e.g. fig is light/fragile, dark oak is heavy/tough; crimson/warped take extra fire damage). Max health is **60** at baseline; the UI bar normalizes across wood types.

## Sneak dismantling

**Sneak + use** (while not riding) strips the cart in order:

1. Release draft team (lead returns to you)
2. Remove cover → drops cover item
3. Remove lantern → drops lantern
4. Pick up cart item (empty passengers only) — includes chest NBT and health

Sneak dismantle is the way to recover the **full cart item**. Destroying the cart does **not** drop the assembled cart.

## Destroyed cart loot

When the cart’s health reaches zero (or it is removed with entity drops enabled):

1. **Chest contents** spill
2. **`materia:entities/cart`** loot table — two rolls each for **`materia:cart_wheel`** (50% per roll) and **`#materia:all_nails`** (35% per roll, expanded)
3. **2–4 smooth planks** of the cart’s wood type (recipe uses six)
4. **Cover** — 85% chance if present
5. **Lantern** — 75% chance if present

## Sleep and phantoms

### Cart sleep

From the cart inventory screen, use the **sleep** action while mounted. This uses a custom sleep flow (forced prone pose, ~5s fade) so vanilla bed sleep does not dismount you. Sleep is only allowed at **night** or during **thunderstorms** (same time window as phantom spawning).

### Phantom shelter

A player riding in a cart with a **cover** is treated as sheltered:

- Phantoms do not spawn for that player
- Nearby phantoms do not acquire or keep that player as a target
- Phantoms attempting to spawn near sheltered riders are blocked

Same idea as sleeping under a roof — useful for overworld travel camps.

## Related

- [Structure chest loot](structure-chest-loot.md) — village and dungeon Materia bonus loot
- [Lashing](../content/items/lashing.md), [Plant fiber](../content/items/plant-fiber.md) — common village loot tied to early crafting
- [Release roadmap](../release-roadmap.md)
