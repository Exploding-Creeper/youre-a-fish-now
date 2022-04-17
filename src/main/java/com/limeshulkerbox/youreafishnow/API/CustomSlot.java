package com.limeshulkerbox.youreafishnow.API;

import net.minecraft.inventory.Inventory;
import net.minecraft.screen.slot.Slot;

public class CustomSlot extends Slot {
    private final SlotGroup group;
    private final SlotType type;
    private final boolean alwaysVisible;
    private final int slotOffset;
    private final Inventory trinketInventory;

    public CustomSlot(Inventory inventory, int index, int x, int y, SlotType type, int slotOffset,
                       boolean alwaysVisible) {
        super(inventory, index, x, y);
        this.type = type;
        this.slotOffset = slotOffset;
        this.alwaysVisible = alwaysVisible;
        this.trinketInventory = inventory;
    }

    public boolean isTrinketFocused() {
        if (TrinketsClient.activeGroup == group) {
            return slotOffset == 0 || TrinketsClient.activeType == type;
        } else if (TrinketsClient.quickMoveGroup == group) {
            return slotOffset == 0 || TrinketsClient.quickMoveType == type && TrinketsClient.quickMoveTimer > 0;
        }
        return false;
    }

    public Identifier getBackgroundIdentifier() {
        return type.getIcon();
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return canInsert(stack, new SlotReference(trinketInventory, slotOffset), trinketInventory.getComponent().getEntity());
    }

    public static boolean canInsert(ItemStack stack, SlotReference slotRef, LivingEntity entity) {
        boolean res = TrinketsApi.evaluatePredicateSet(slotRef.inventory().getSlotType().getValidatorPredicates(),
                stack, slotRef, entity);

        if (res) {
            return TrinketsApi.getTrinket(stack.getItem()).canEquip(stack, slotRef, entity);
        }

        return false;
    }

    @Override
    public boolean canTakeItems(PlayerEntity player) {
        ItemStack stack = this.getStack();
        return TrinketsApi.getTrinket(stack.getItem())
                .canUnequip(stack, new SlotReference(trinketInventory, slotOffset), player);
    }

    @Override
    public boolean isEnabled() {
        if (alwaysVisible) {
            if (x < 0) {
                if (trinketInventory.getComponent().getEntity().world.isClient) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    Screen s = client.currentScreen;
                    if (s instanceof InventoryScreen screen) {
                        if (screen.getRecipeBookWidget().isOpen()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return isTrinketFocused();
    }

    public SlotType getType() {
        return type;
    }
}
