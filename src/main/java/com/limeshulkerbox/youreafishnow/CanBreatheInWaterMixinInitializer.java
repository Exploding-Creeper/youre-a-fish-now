package com.limeshulkerbox.youreafishnow;

import net.fabricmc.api.ModInitializer;

public class CanBreatheInWaterMixinInitializer implements ModInitializer {
    @Override
    public void onInitialize() {
        System.out.println("You have magically turned into a fish...");
    }
}
