package com.kqp.terminus.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;

public class CelestialAltarResultInventory implements Inventory {
    private final DefaultedList<ItemStack> stack;

    public CelestialAltarResultInventory() {
        this.stack = DefaultedList.ofSize(1, ItemStack.EMPTY);
    }

    public int getInvSize() {
        return 1;
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
        return (ItemStack) this.stack.get(0);
    }

    public ItemStack takeInvStack(int slot, int amount) {
        return Inventories.removeStack(this.stack, 0);
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(this.stack, 0);
    }

    public void setInvStack(int slot, ItemStack stack) {
        this.stack.set(0, stack);
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