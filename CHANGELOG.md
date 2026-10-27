# Changelog

## 1.0.4 — 2026-10-26

- Added Mouse Tweaks' drag features, so Mouse Tweaks is no longer needed alongside this mod:
  - Right-drag while holding a stack puts one item into each slot you pass over.
  - Click a stack and keep holding left click, then drag over slots to collect matching items (Shift moves them instead).
  - Both can be turned off in the config (General section). Shift-drag moving and scroll moving were already here.
- Ported from Mouse Tweaks by Ivan Molodetskikh (BSD 3-Clause, see LICENSE_MouseTweaks and NOTICE).

## 1.0.3 — 2026-10-26

- New icon and README logo (Siphalor's Mouse Wheelie artwork is no longer used).
- Added Landram5 as an author of the fork.

## 1.0.2 — 2026-10-26

- Fixed startup on Minecraft 26.3 by moving drop/refill hooks to the new client drop action.
- Fixed the bundled Coat library's startup crash by using the default help cursor on SDL.
- Replaced the creative inventory sorting mouse check with Minecraft's mouse API on 26.3.

Validation: reproduced the original startup crash, verified the patched client reaches the title screen,
and received user confirmation that gameplay works.
