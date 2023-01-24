package dev.pieman.youreafishnow.mixin;

import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.MathHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BackgroundRenderer.class)
public class ChangeFogDistanceMixin {

    @Unique
    private static float viewDistance;

    @Inject(method = "applyFog", at = @At(value = "HEAD"))
    private static void injected(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        ChangeFogDistanceMixin.viewDistance = viewDistance;
    }

    @Redirect(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogData;fogEnd:F", opcode = Opcodes.PUTFIELD, ordinal = 5))
    private static void changeUnderwater(BackgroundRenderer.FogData instance, float value) {
        float f = MathHelper.clamp(viewDistance / 10.0F, 4.0F, 64.0F);
        instance.fogStart = viewDistance - f;
        instance.fogEnd = viewDistance;
    }

    @Redirect(method = "applyFog", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/BackgroundRenderer$FogData;fogEnd:F", opcode = Opcodes.PUTFIELD, ordinal = 11))
    private static void changeInAir(BackgroundRenderer.FogData instance, float value) {
        instance.fogStart = -8f;
        instance.fogEnd = 96f;
    }
}
