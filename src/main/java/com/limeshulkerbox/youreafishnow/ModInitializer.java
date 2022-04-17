package com.limeshulkerbox.youreafishnow;

import com.limeshulkerbox.youreafishnow.items.ItemsInit;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ModInitializer implements net.fabricmc.api.ModInitializer {
    @Override
    public void onInitialize() {
        ItemsInit.registerItems();

        System.out.println("You have magically turned into a fish...");
    }

    public static final ItemGroup YOURE_A_FISH_NOW_GROUP = FabricItemGroupBuilder.create(
                    new Identifier("tutorial", "other"))
            .icon(() -> new ItemStack(ItemsInit.SCUBA_MASK_ITEM))
            .appendItems(stacks -> {
                stacks.add(new ItemStack(ItemsInit.SCUBA_MASK_ITEM));
                stacks.add(new ItemStack(ItemsInit.SCUBA_TANK_ITEM));
            })
            .build();
}
