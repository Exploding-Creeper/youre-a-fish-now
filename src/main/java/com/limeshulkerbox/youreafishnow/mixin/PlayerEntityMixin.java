package com.limeshulkerbox.youreafishnow.mixin;

import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import static net.minecraft.entity.effect.StatusEffects.WATER_BREATHING;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Shadow public abstract Iterable<ItemStack> getArmorItems();

	@Shadow public abstract boolean isPlayer();

	boolean canBreathe;

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
		super(entityType, world);
	}

	@Override()
	public boolean canBreatheInWater() {
		return true;
	}

	public boolean canBreathe() {
		return canBreathe;
	}

	@Unique
	protected void tickWaterBreathingAir(int air) {
		if (this.isAlive() && !this.isSubmergedIn(FluidTags.WATER) && !this.hasStatusEffect(WATER_BREATHING) && !canBreathe()) {
			this.setAir(air - 1);
			if (this.getAir() == -20) {
				this.setAir(0);
				this.damage(DamageSource.DROWN, 2.0F);
			}
		} else {
			this.setAir(getNextAirOnLand(air));
		}
	}

	@Override
	public void baseTick() {
		int i = this.getAir();
		super.baseTick();
		this.tickWaterBreathingAir(i);

	}
}
