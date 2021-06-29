package com.limeshulkerbox.youreafishnow.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import oshi.annotation.concurrent.NotThreadSafe;

import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class ChangeBreakSpeedMixin extends LivingEntity {
    @Shadow @Final
    private PlayerInventory inventory;

    protected ChangeBreakSpeedMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //copied from base with changed lines marked with /*change*/
    /**
     * @author j
     */
    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "HEAD"), cancellable = true)
    public void getBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        float f = this.inventory.getBlockBreakingSpeed(block);
      if (f > 1.0F) {
        int i = EnchantmentHelper.getEfficiency(this);
        ItemStack itemStack = this.getMainHandStack();
        if (i > 0 && !itemStack.isEmpty()) {
            f += (float)(i * i + 1);
        }
    }

      if (StatusEffectUtil.hasHaste(this)) {
        f *= 1.0F + (float)(StatusEffectUtil.getHasteAmplifier(this) + 1) * 0.2F;
    }

      if (this.hasStatusEffect(StatusEffects.MINING_FATIGUE)) {
        float k;
        switch(Objects.requireNonNull(this.getStatusEffect(StatusEffects.MINING_FATIGUE)).getAmplifier()) {
            case 0:
                k = 0.3F;
                break;
            case 1:
                k = 0.09F;
                break;
            case 2:
                k = 0.0027F;
                break;
            case 3:
            default:
                k = 8.1E-4F;
        }

        f *= k;
    }

      if (!this.isSubmergedIn(FluidTags.WATER) && !EnchantmentHelper.hasAquaAffinity(this)) {
        f /= 5.0F;
    }

      if (!this.onGround && !this.isSubmergedIn(FluidTags.WATER)) {
        f /= 5.0F;
    }

      cir.setReturnValue(f);
    }
}