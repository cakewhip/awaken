package com.kqp.terminus.inventory;

import net.minecraft.container.Container;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

import java.util.Iterator;
import java.util.List;

public class CelestialAltarInventory implements Inventory {
    private final DefaultedList<ItemStack> stacks;
    private final Container container;

    public CelestialAltarInventory(Container container) {
        this.stacks = DefaultedList.ofSize(15, ItemStack.EMPTY);
        this.container = container;
    }

    public int getInvSize() {
        return this.stacks.size();
    }

    public boolean isInvEmpty() {
        Iterator var1 = this.stacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = (ItemStack)var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getInvStack(int slot) {
        return slot >= this.getInvSize() ? ItemStack.EMPTY : (ItemStack)this.stacks.get(slot);
    }

    public ItemStack removeInvStack(int slot) {
        return Inventories.removeStack(this.stacks, slot);
    }

    public ItemStack takeInvStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(this.stacks, slot, amount);
        if (!itemStack.isEmpty()) {
            this.container.onContentChanged(this);
        }

        return itemStack;
    }

    public List<ItemStack> getItemStacks() {
        return stacks;
    }

    public void setInvStack(int slot, ItemStack stack) {
        this.stacks.set(slot, stack);
        this.container.onContentChanged(this);
    }

    public void markDirty() {
    }

    public boolean canPlayerUseInv(PlayerEntity player) {
        return true;
    }

    public void clear() {
        this.stacks.clear();
    }
}
