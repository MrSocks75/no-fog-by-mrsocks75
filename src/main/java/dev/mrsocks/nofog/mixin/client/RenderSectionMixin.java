package dev.mrsocks.nofog.mixin.client;

import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SectionRenderDispatcher.RenderSection.class)
public class RenderSectionMixin {
	// terrain.fsh blends newly uploaded chunk sections in from the fog colour while this is below 1.
	// At the edge of the render distance that reads as fog, so report every section as fully faded in.
	@Inject(method = "getVisibility", at = @At("HEAD"), cancellable = true)
	private void nofogbymrsocks$skipChunkFade(long now, long fadeTimeMs, CallbackInfoReturnable<Float> cir) {
		cir.setReturnValue(1.0f);
	}
}
