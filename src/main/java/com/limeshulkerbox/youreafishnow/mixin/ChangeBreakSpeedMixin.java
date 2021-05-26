package com.limeshulkerbox.youreafishnow.mixin;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Either;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.CommandBlockBlockEntity;
import net.minecraft.block.entity.JigsawBlockEntity;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.block.entity.StructureBlockBlockEntity;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.player.*;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.recipe.Recipe;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stat;
import net.minecraft.tag.FluidTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.village.TradeOfferList;
import net.minecraft.world.CommandBlockExecutor;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;

@Mixin(PlayerEntity.class)
public abstract class ChangeBreakSpeedMixin extends LivingEntity {
    @Shadow @Final public PlayerInventory inventory;

    protected ChangeBreakSpeedMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    //copied from base with changed lines marked with /*change*/
    /**
     * @author j
     */
    @Overwrite
    public float getBlockBreakingSpeed(BlockState block)
    {
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

      return f;
    }
}