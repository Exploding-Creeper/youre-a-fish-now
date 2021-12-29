package com.limeshulkerbox.youreafishnow;

import com.limeshulkerbox.youreafishnow.items.ScubaMaskItem;
import com.limeshulkerbox.youreafishnow.items.ScubaTankItem;
import dev.emi.trinkets.api.client.TrinketRendererRegistry;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

public class ModInitializer implements net.fabricmc.api.ModInitializer {

    public static final String MODID = "youreafishnow";

    public static final ItemGroup YOURE_A_FISH_NOW_GROUP = FabricItemGroupBuilder.create(
            new Identifier("youreafishnow", "youreafishnowtab"))
            .icon(() -> new ItemStack(Items.COD_BUCKET))
            .appendItems(stacks -> {
            })
            .build();

    public static final ScubaTankItem SCUBA_TANK_ITEM = new ScubaTankItem(new Item.Settings().group(YOURE_A_FISH_NOW_GROUP));
    public static final ScubaMaskItem SCUBA_MASK_ITEM = new ScubaMaskItem(new Item.Settings().group(YOURE_A_FISH_NOW_GROUP));

    @Override
    public void onInitialize() {
        System.out.println("You have magically turned into a fish...");
        Registry.register(Registry.ITEM,new Identifier("youreafishnow","scuba_tank"),SCUBA_TANK_ITEM);
        Registry.register(Registry.ITEM,new Identifier("youreafishnow","scuba_mask"),SCUBA_MASK_ITEM);
    }
}
