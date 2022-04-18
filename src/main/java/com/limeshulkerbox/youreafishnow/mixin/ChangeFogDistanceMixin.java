package com.limeshulkerbox.youreafishnow.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.FogShape;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(BackgroundRenderer.class)
public class ChangeFogDistanceMixin {

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void applyFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, CallbackInfo ci) {
        ci.cancel();
        CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
        Entity entity = camera.getFocusedEntity();
        FogShape fogShape = FogShape.SPHERE;
        float f;
        float g;
        if (cameraSubmersionType == CameraSubmersionType.LAVA) {
            if (entity.isSpectator()) {
                f = -8.0F;
                g = viewDistance * 0.5F;
            } else if (entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                f = 0.0F;
                g = 3.0F;
            } else {
                f = 0.25F;
                g = 1.0F;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.POWDER_SNOW) {
            if (entity.isSpectator()) {
                f = -8.0F;
                g = viewDistance * 0.5F;
            } else {
                f = 0.0F;
                g = 2.0F;
            }
        } else if (entity instanceof LivingEntity && ((LivingEntity) entity).hasStatusEffect(StatusEffects.BLINDNESS)) {
            int i = Objects.requireNonNull(((LivingEntity) entity).getStatusEffect(StatusEffects.BLINDNESS)).getDuration();
            float h = MathHelper.lerp(Math.min(1.0F, (float) i / 20.0F), viewDistance, 5.0F);
            if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
                f = 0.0F;
                g = h * 0.8F;
            } else {
                f = cameraSubmersionType == CameraSubmersionType.WATER ? -4.0F : h * 0.25F;
                g = h;
            }
        } else if (cameraSubmersionType == CameraSubmersionType.WATER) {
            f = -8.0F;
            g = 750.0F;
            if (entity instanceof ClientPlayerEntity) {
                ClientPlayerEntity i = (ClientPlayerEntity) entity;
                g *= Math.max(0.25F, i.getUnderwaterVisibility());
                RegistryEntry<Biome> h = i.world.getBiome(i.getBlockPos());
                if (Biome.getCategory(h) == Biome.Category.SWAMP) {
                    g *= 0.5F;
                    g *= 0.5F;
                }
            }

            if (g > viewDistance) {
                g = viewDistance;
                fogShape = FogShape.CYLINDER;
            }
        } else if (thickFog) {
            f = viewDistance * 0.05F;
            g = Math.min(viewDistance, 192.0F) * 0.5F;
        } else if (fogType == BackgroundRenderer.FogType.FOG_SKY) {
            f = 0.0F;
            g = viewDistance;
            fogShape = FogShape.CYLINDER;
        } else {
            float i = MathHelper.clamp(viewDistance / 10.0F, 4.0F, 64.0F);
            f = viewDistance - i;
            g = viewDistance;
            fogShape = FogShape.CYLINDER;
        }

        RenderSystem.setShaderFogStart(f);
        RenderSystem.setShaderFogEnd(g);
        RenderSystem.setShaderFogShape(fogShape);
    }

}
