package com.limeshulkerbox.youreafishnow.items;

import com.limeshulkerbox.youreafishnow.API.WaterBreatheableItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemsInit {

    public static final WaterBreatheableItem SCUBA_TANK_ITEM = new WaterBreatheableItem(new FabricItemSettings().group(ItemGroup.MISC), 10000);
    public static final WaterBreatheableItem SCUBA_MASK_ITEM = new WaterBreatheableItem(new FabricItemSettings().group(ItemGroup.MISC), 5000);

    public static void registerItems() {
        Registry.register(Registry.ITEM, new Identifier("youreafishnow", "scuba_tank_item"), SCUBA_TANK_ITEM);
        Registry.register(Registry.ITEM, new Identifier("youreafishnow", "scuba_mask_item"), SCUBA_MASK_ITEM);
    }
}
