package dev.mrsocks.nofog.mixin.client;

import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FogType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
	// Far enough that the fog factor stays ~0 at any render distance, but finite so the shader math is safe.
	private static final float NO_FOG_DISTANCE = 1_000_000f;

	@Inject(method = "setupFog", at = @At("RETURN"))
	private void nofogbymrsocks$removeFog(Camera camera, int renderDistanceChunks, DeltaTracker deltaTracker, float darkenWorldAmount, ClientLevel level, CallbackInfoReturnable<FogData> cir) {
		// Keep water, lava and powder snow fog, which are part of how those blocks look.
		if (camera.getFluidInCamera() != FogType.ATMOSPHERIC) {
			return;
		}
		// Keep the fog from the blindness and darkness effects.
		if (camera.entity() instanceof LivingEntity living
				&& (living.hasEffect(MobEffects.BLINDNESS) || living.hasEffect(MobEffects.DARKNESS))) {
			return;
		}

		FogData fog = cir.getReturnValue();
		fog.environmentalStart = NO_FOG_DISTANCE;
		fog.environmentalEnd = NO_FOG_DISTANCE;
		fog.renderDistanceStart = NO_FOG_DISTANCE;
		fog.renderDistanceEnd = NO_FOG_DISTANCE;
	}
}
