# fakemousewheelie

Unofficial Minecraft **26.3** fork of [Mouse Wheelie](https://github.com/Siphalor/mouse-wheelie) by Siphalor (Apache-2.0). Not affiliated with or endorsed by the original author.

Inventory utilities for Fabric: scroll stacks between inventories, sort, deposit/restock, scroll through creative tabs and recipe-book pages, refill items and more.

**Changes in this fork**
- Runs on Minecraft 26.3 (Fabric).
- Hardened shutdown: background work uses daemon threads and is stopped when the client closes, so quitting the game should not hang or crash because of this mod.
- Mod id is `fakemousewheelie` (separate config file); it conflicts with the original Mouse Wheelie, so install only one.

**Requires:** Fabric API. Client-side mod; most features also work on servers without it.
