## Instruments

Instruments are held items that play musical samples.

Core design goals:

- multiple players can play together without perfect timing
- different instrument “flavors” can be mixed freely (you don’t need matching elements)
- samples are in a compatible tempo/key “family” so layering sounds good

## How instruments sync (why group play works)

When you use an instrument, it does **not** necessarily play immediately.

Instead, it queues itself to start on a shared timing boundary:

- Sync points happen every **10 ticks** (0.5 seconds), which corresponds to **120 BPM** timing.
- Instruments start on the **next** sync point so multiple players line up naturally.

Once active, the instrument repeats its sample on a fixed loop:

- **Short** samples loop about every 40 ticks
- **Medium** samples loop about every 80 ticks
- **Long** samples loop about every 160 ticks

## Instrument families and “elements”

Most instruments come in four “element” flavors:

- **Air** (crafted with a yellow dye tag)
- **Earth** (crafted with a green dye tag)
- **Fire** (crafted with a red dye tag)
- **Water** (crafted with a blue dye tag)

These flavors primarily change **which sound sample** is played.

Mixing rule of thumb:

- You can mix any elements and instrument types together (ex: earth drums + fire flute + water bass).

## A small hint

Some players swear there are “other” sounds out there besides the four main flavors, but they’re not something you can count on finding in normal play.

## Related

- [Instruments (items and crafting)](../content/items/instruments.md)
