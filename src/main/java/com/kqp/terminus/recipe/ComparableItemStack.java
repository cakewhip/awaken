package com.kqp.terminus.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public class ComparableItemStack {
    public final Item item;
    public final CompoundTag tag;
    public final int hashCode;

    public ComparableItemStack(ItemStack itemStack) {
        this.item = itemStack.getItem();
        this.tag = itemStack.getTag();
        this.hashCode = Objects.hash(item, tag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComparableItemStack that = (ComparableItemStack) o;
        return item == that.item &&
                Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }
}
