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

package de.siphalor.mousewheelie.client.mixin.gui.screen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;

/** Vanilla container screen internals used by {@link de.siphalor.mousewheelie.client.inventory.DragTweaks}. */
@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor {
	@Invoker("getHoveredSlot")
	Slot mousewheelie$getHoveredSlot(double x, double y);

	@Invoker("slotClicked")
	void mousewheelie$slotClicked(Slot slot, int index, int button, ContainerInput input);

	@Accessor("isQuickCrafting")
	boolean mousewheelie$isQuickCrafting();

	@Accessor("isQuickCrafting")
	void mousewheelie$setQuickCrafting(boolean value);

	@Accessor("quickCraftingButton")
	int mousewheelie$getQuickCraftingButton();

	@Accessor("skipNextRelease")
	void mousewheelie$setSkipNextRelease(boolean value);
}
