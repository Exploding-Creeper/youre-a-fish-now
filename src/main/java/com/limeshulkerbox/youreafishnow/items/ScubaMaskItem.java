package com.limeshulkerbox.youreafishnow.items;

import com.limeshulkerbox.youreafishnow.api.BreathingItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;

import java.util.List;

public class ScubaMaskItem extends BreathingItem {

    public ScubaMaskItem(Settings group) {
        super(group.maxCount(1), 10000, 1000);
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.add(new TranslatableText("text.scuba_mask"));
    }
}