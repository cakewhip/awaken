package com.kqp.awaken.client.container;

import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;

public class AwakenLookUpResultSlot extends Slot {
    public int currentIndex = 0;

    public AwakenLookUpResultSlot(Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
    }

    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        return false;
    }
}
