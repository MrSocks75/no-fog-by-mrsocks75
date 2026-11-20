package dev.mrsocks.nofog.mixin.client;

import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.buffers.GpuBufferSlice;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
	// Vanilla's "fog off" buffer: every distance is Float.MAX_VALUE and the fog colour is fully transparent.
	@Shadow
	@Final
	private GpuBuffer emptyBuffer;

	// Every shader reads fog from the buffer this returns, so serving the empty one removes all fog:
	// terrain, sky, clouds, water, lava, powder snow, and the blindness and darkness effects.
	// The sky and clear colour come from the separate FogData, so they are unaffected.
	@Inject(method = "getBuffer", at = @At("HEAD"), cancellable = true)
	private void nofogbymrsocks$removeFog(FogRenderer.FogMode mode, CallbackInfoReturnable<GpuBufferSlice> cir) {
		cir.setReturnValue(this.emptyBuffer.slice(0L, (long) FogRenderer.FOG_UBO_SIZE));
	}
}
