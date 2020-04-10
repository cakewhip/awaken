package com.kqp.awaken.client.slot;

import com.kqp.awaken.inventory.AwakenCraftingRecipeLookUpInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

/**
 * Slot for the look-up results in the crafting screen.
 */
public class AwakenLookUpResultSlot extends Slot {
    public int currentIndex = 0;

    public AwakenLookUpResultSlot(AwakenCraftingRecipeLookUpInventory lookUpResultInventory, int invSlot, int xPosition, int yPosition) {
        super(lookUpResultInventory, invSlot, xPosition, yPosition);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }

    @Override
    public boolean canInsert(ItemStack itemStack) {
        return false;
    }
}
