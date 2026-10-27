/*
 * Copyright 2026 Siphalor and contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied.
 * See the License for the specific language governing
 * permissions and limitations under the License.
 */

/*
 * The drag behaviour in this file is ported from Mouse Tweaks
 * (https://github.com/YaLTeR/MouseTweaks, yalter.mousetweaks.Main), used under the BSD 3-Clause License:
 *
 * Copyright (c) 2023, Ivan Molodetskikh
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package de.siphalor.mousewheelie.client.inventory;

import com.mojang.blaze3d.platform.InputConstants;
import de.siphalor.mousewheelie.MouseWheelie;
import de.siphalor.mousewheelie.client.mixin.gui.screen.AbstractContainerScreenAccessor;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.MerchantResultSlot;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.ItemStack;

/**
 * Mouse Tweaks' drag features, so that mod isn't needed alongside this one:
 * <ul>
 *     <li>right-drag while holding a stack: put one item into each slot passed over</li>
 *     <li>left-drag while holding an item: collect matching stacks from each slot passed over</li>
 * </ul>
 * Shift-drag moving and scroll moving were already Mouse Wheelie features (whole stack modifier, scrolling).
 */
public final class DragTweaks {
	// mouse events use SDL numbering on 26.3 (left 1, right 3); slot clicks still use 0 = left, 1 = right
	private static final int LEFT = InputConstants.MOUSE_BUTTON_LEFT;
	private static final int RIGHT = InputConstants.MOUSE_BUTTON_RIGHT;
	private static final int CLICK_LEFT = 0;
	private static final int CLICK_RIGHT = 1;

	private final AbstractContainerScreen<?> screen;
	private final AbstractContainerScreenAccessor access;
	private Slot lastSlot;
	private boolean leftDragging;
	private boolean rightDragging;
	private boolean rightLeftStartSlot;

	private DragTweaks(AbstractContainerScreen<?> screen) {
		this.screen = screen;
		this.access = (AbstractContainerScreenAccessor) screen;
	}

	public static void register() {
		ScreenEvents.BEFORE_INIT.register((client, screen, width, height) -> {
			// the creative inventory's slots don't behave like normal container slots
			if (!(screen instanceof AbstractContainerScreen<?> containerScreen) || screen instanceof CreativeModeInventoryScreen) {
				return;
			}
			DragTweaks tweaks = new DragTweaks(containerScreen);
			ScreenMouseEvents.allowMouseClick(screen).register((s, event) -> tweaks.onClick(event));
			ScreenMouseEvents.allowMouseDrag(screen).register((s, event, dx, dy) -> tweaks.onDrag(event));
			ScreenMouseEvents.allowMouseRelease(screen).register((s, event) -> tweaks.onRelease(event));
		});
	}

	private ItemStack carried() {
		return Minecraft.getInstance().player.containerMenu.getCarried();
	}

	private boolean onClick(MouseButtonEvent event) {
		lastSlot = access.mousewheelie$getHoveredSlot(event.x(), event.y());
		ItemStack carried = carried();
		if (event.button() == LEFT) {
			// as in Mouse Tweaks: only a press with an empty hand arms collecting (the press picks a stack up, the drag
			// collects); pressing while already holding a stack leaves vanilla's "spread evenly" drag alone
			leftDragging = carried.isEmpty();
		} else if (event.button() == RIGHT && !carried.isEmpty() && MouseWheelie.config.general.rightDragOnePerSlot
				&& !(carried.getItem() instanceof BundleItem && MouseWheelie.config.general.enableBundleDragging)) {
			rightDragging = true;
			rightLeftStartSlot = false;
		}
		return true; // never cancel: vanilla still handles the click itself
	}

	private boolean onRelease(MouseButtonEvent event) {
		if (event.button() == LEFT) {
			leftDragging = false;
		} else if (event.button() == RIGHT) {
			rightDragging = false;
		}
		return true;
	}

	private boolean onDrag(MouseButtonEvent event) {
		Slot slot = access.mousewheelie$getHoveredSlot(event.x(), event.y());
		if (slot == lastSlot) {
			return true;
		}
		ItemStack carried = carried();

		// leaving the slot the right-drag started on: drop one item there too, and stop vanilla's
		// "spread evenly on release" right-drag from also running
		if (rightDragging && event.button() == RIGHT && !rightLeftStartSlot) {
			rightLeftStartSlot = true;
			access.mousewheelie$setSkipNextRelease(true);
			if (access.mousewheelie$isQuickCrafting() && access.mousewheelie$getQuickCraftingButton() == InputConstants.MOUSE_BUTTON_RIGHT) {
				access.mousewheelie$setQuickCrafting(false);
			}
			putOne(lastSlot, carried);
		}
		lastSlot = slot;
		if (slot == null) {
			return true;
		}

		// with an empty hand, shift-drag is Mouse Wheelie's whole-stack modifier and handled elsewhere
		if (event.button() == LEFT && leftDragging && !carried.isEmpty() && MouseWheelie.config.general.leftDragCollect) {
			collect(slot, carried);
		} else if (event.button() == RIGHT && rightDragging) {
			putOne(slot, carried);
		}
		return true;
	}

	/** Right-click a slot to put one item from the held stack into it, if it fits. */
	private void putOne(Slot slot, ItemStack carried) {
		if (slot == null || carried.isEmpty() || isCraftingOutput(slot)) {
			return;
		}
		if (!(carried.getItem() instanceof BundleItem)) {
			ItemStack inSlot = slot.getItem();
			if (!compatible(inSlot, carried) || inSlot.getCount() == slot.getMaxStackSize(inSlot)) {
				return;
			}
		}
		click(slot, CLICK_RIGHT, ContainerInput.PICKUP);
	}

	/** Merge a matching stack from the slot into the held stack (shift instead moves it across). */
	private void collect(Slot slot, ItemStack carried) {
		ItemStack inSlot = slot.getItem();
		if (inSlot.isEmpty() || carried.isEmpty() || !compatible(inSlot, carried)) {
			return;
		}
		if (InputConstants.isKeyDown(InputConstants.KEY_LSHIFT) || InputConstants.isKeyDown(InputConstants.KEY_RSHIFT)) {
			click(slot, CLICK_LEFT, ContainerInput.QUICK_MOVE);
			return;
		}
		// only if everything fits on the cursor; otherwise the click would leave items behind in the slot
		if (carried.getCount() + inSlot.getCount() > carried.getMaxStackSize()) {
			return;
		}
		// the first click drops the held stack onto the slot's (merging them), the second picks the merged stack up;
		// crafting outputs add straight to the held stack, so they only need one
		click(slot, CLICK_LEFT, ContainerInput.PICKUP);
		if (!isCraftingOutput(slot)) {
			click(slot, CLICK_LEFT, ContainerInput.PICKUP);
		}
	}

	private void click(Slot slot, int button, ContainerInput input) {
		access.mousewheelie$slotClicked(slot, slot.index, button, input);
	}

	private static boolean compatible(ItemStack a, ItemStack b) {
		return a.isEmpty() || b.isEmpty() || ItemStack.isSameItemSameComponents(a, b);
	}

	private static boolean isCraftingOutput(Slot slot) {
		return slot instanceof ResultSlot || slot instanceof FurnaceResultSlot || slot instanceof MerchantResultSlot;
	}
}
