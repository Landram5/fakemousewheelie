/*
 * Copyright 2020 Siphalor and contributors
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

package de.siphalor.mousewheelie.client.mixin.compat;

//# if MC_VERSION_NUMBER >= 260300
import com.mojang.blaze3d.platform.cursor.CursorType;
import com.mojang.datafixers.util.Pair;
import de.siphalor.coat.cursor.CoatCursorTypes;
import java.nio.ByteBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
@Mixin(value = CoatCursorTypes.class, remap = false)
public abstract class MixinCoatCursorTypes {
	@Shadow
	private static Pair<CursorType, ByteBuffer> helpCursor;

	@Inject(method = "initialize", at = @At("HEAD"), cancellable = true)
	private static void initializeSdlCursor(CallbackInfo ci) {
		// shortcut: use the default help cursor until Coat ships an SDL-compatible build.
		helpCursor = Pair.of(CursorType.DEFAULT, null);
		ci.cancel();
	}
}
//# end
