# Changelog

## 1.0.3 — 2026-10-26

- New icon and README logo (Siphalor's Mouse Wheelie artwork is no longer used).
- Added Landram5 as an author of the fork.

## 1.0.2 — 2026-10-26

- Fixed startup on Minecraft 26.3 by moving drop/refill hooks to the new client drop action.
- Fixed the bundled Coat library's startup crash by using the default help cursor on SDL.
- Replaced the creative inventory sorting mouse check with Minecraft's mouse API on 26.3.

Validation: reproduced the original startup crash, verified the patched client reaches the title screen,
and received user confirmation that gameplay works.
