# fakemousewheelie

Unofficial Minecraft **26.3** fork of [Mouse Wheelie](https://github.com/Siphalor/mouse-wheelie) by Siphalor (Apache-2.0). Not affiliated with or endorsed by the original author.

Inventory utilities for Fabric: scroll stacks between inventories, sort, deposit/restock, scroll through creative tabs and recipe-book pages, refill items and more.

**Changes in this fork**
- Runs on Minecraft 26.3 (Fabric).
- Hardened shutdown: background work uses daemon threads and is stopped when the client closes, so quitting the game should not hang or crash because of this mod.
- Mod id is `fakemousewheelie` (separate config file); it conflicts with the original Mouse Wheelie, so install only one.
- Mouse Tweaks' drag features are built in, so you don't need Mouse Tweaks alongside it:
  - **Right-drag** while holding a stack puts one item into each slot you pass over.
  - **Click a stack and keep holding left click**, then drag over slots to collect matching items (hold Shift to move them to the other inventory instead).
  - **Shift + left-drag** with an empty hand moves every stack you pass over.
  - Both new drag features can be turned off in the config (General section).
- Fixed mouse buttons on 26.3, so shift/ctrl/alt-dragging, bundle dragging and recipe-book quick craft work.

**Requires:** Fabric API. Client-side mod; most features also work on servers without it.

**Credits:** based on Mouse Wheelie by Siphalor and contributors (Apache-2.0). The drag features are ported from [Mouse Tweaks](https://github.com/YaLTeR/MouseTweaks), Copyright (c) 2023 Ivan Molodetskikh, used under the BSD 3-Clause License (included in the jar as `LICENSE_MouseTweaks`). Neither author is affiliated with or endorses this fork.
