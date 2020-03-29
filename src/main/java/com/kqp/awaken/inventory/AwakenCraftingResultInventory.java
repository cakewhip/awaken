package com.kqp.awaken.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;

/**
 * Inventory class for the result slots in Awaken's crafting container.
 * Just 24 slots that only allow extraction.
 */
public class AwakenCraftingResultInventory implements Inventory {
    private final DefaultedList<ItemStack> stack;

    public AwakenCraftingResultInventory() {
        this.stack = DefaultedList.ofSize(24, ItemStack.EMPTY);
    }

    public int getInvSize() {
        return 24;
    }

    public boolean isInvEmpty() {
        Iterator var1 = this.stack.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack) var1.next();
        } while (itemStack.isEmpty());

        return false;
    }

    public ItemStack getInvStack(int slot) {
        return this.stack.get(slot);
    }

    public ItemStack takeInvStack(int slot, int amount) {
        return Inventories.removeStack(this.stack, slot);
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(this.stack, slot);
    }

    public void setInvStack(int slot, ItemStack stack) {
        this.stack.set(slot, stack);
    }

    public void markDirty() {
    }

    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    public void clear() {
        this.stack.clear();
    }
}