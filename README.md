<div align="center">
    <img src="src/main/resources/assets/fakemousewheelie/icon_large.png" alt="Logo" width="150" height="150" />

# fakemousewheelie

</div>

**Unofficial fork of [Mouse Wheelie](https://github.com/Siphalor/mouse-wheelie) by Siphalor, updated for Minecraft 26.3.**
Not affiliated with or endorsed by the original author. See [NOTICE](NOTICE) for what was changed.

"Small" Minecraft Fabric mod featuring inventory utilities like scrolling, sorting and auto refilling of items.

_No mouses were harmed in the making of this mod._


## Main Features

| Feature               | Description                                                                | How?                                          | Availability                                             |
|-----------------------|----------------------------------------------------------------------------|-----------------------------------------------|----------------------------------------------------------|
| Inventory scrolling   | Scroll over stacks in inventories to move them around                      | Scroll over stacks                            | Everywhere                                               |
| Depositing/Restocking | Move all items that already exist in another inventory                     | Hold space and left click                     | Everywhere                                               |
| Inventory sorting     | Sort items in inventories by name, quantity or creative order              | Press middle mouse button                     | Everywhere (faster if on server¹, creative since 1.19.3) |
| Scroll through tabs   | Scroll through tabs and pages in the creative inventory or recipe book     | Scroll over tabs/pages                        | Everywhere                                               |
| Auto refill           | Automatically refills used up tools, foods and the like from the inventory | Use something up                              | Everywhere                                               |
| Tool picking          | Pick the correct tools for the focused block                               | Press middle mouse button when holding a tool | Everywhere                                               |
| Quick crafing/trading | Right click on recipes in the recipe book or trades to instant trade       | Read to the left :)                           | Everywhere                                               |
| Armor swapping        | Quickly swap equivalent armor pieces                                       | Right-click while having the item in hotbar   | Up to 1.19.4, later built in to Vanilla                  |

Additionally, clicks and scrolling in the inventory will change behavior when pressing the following modifier keys:
- <kbd>Shift</kbd>: Applies the action to the full stack instead of single items
- <kbd>Ctrl</kbd>: Applies the action to all items of the same type
- <kbd>Alt</kbd>: Drops the item instead of moving it

Basically all the actions are rebindable in the controls menu.

¹: Works on singleplayer or on servers if the mod is installed on the server as well.

## Configuration

Lots of stuff is configurable.

<!-- curseforge.exclude.start -->
To open the config screen you'll need to install [Mod Menu](https://modrinth.com/project/mOgUt4GM).
<!-- curseforge.exclude.end -->
<!-- curseforge.include: To open the config screen you'll need to install [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu). -->

Alternatively, you can edit the config file `fakemousewheelie.hjson` in the config folder after the first launch.  
Changes will only be applied after a restart when editing the config file.

## Translation status

[![Translation status](https://weblate.siphalor.de/widgets/mouse-wheelie/-/lang/multi-auto.svg)](https://weblate.siphalor.de/engage/mouse-wheelie/)

Contribute or improve translations [here](https://weblate.siphalor.de/engage/mouse-wheelie).

## Notes for Server Operators

A bunch of the features in this mod are rightfully considered cheating.  
See [SERVER_OPERATORS.md](SERVER_OPERATORS.md) for information on how to prevent this.

<!-- mod_site.exclude.start -->
## License

This mod is available under [the Apache 2.0 License](./LICENSE.md). Terms and conditions apply.
<!-- mod_site.exclude.end -->
