package com.limeshulkerbox.youreafishnow.api;

import net.minecraft.entity.Entity;
import net.minecraft.item.*;
import net.minecraft.world.World;

public class BreathingItem extends Item {

    float maxWaterAmount;
    float waterLeft;
    static boolean canBreathe;

    public BreathingItem(Settings settings, float maxWaterAmount, float startingWaterAmount) {
        super(settings);
        waterLeft = startingWaterAmount;
        this.maxWaterAmount = maxWaterAmount;
    }

    public boolean canBreathe() {
        return canBreathe;
    }

    private void depleteWater() {
        //Called from overrided tick method
        if (waterLeft > 0) {
            canBreathe = true;
            waterLeft--;
        } else {
            canBreathe = false;
        }
        System.out.println(waterLeft);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        depleteWater();
        super.inventoryTick(stack, world, entity, slot, selected);
    }
}