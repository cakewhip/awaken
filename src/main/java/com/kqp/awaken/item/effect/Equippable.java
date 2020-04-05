package com.kqp.awaken.item.effect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

public interface Equippable {
    void equip(ItemStack itemStack, PlayerEntity player);

    void unEquip(ItemStack itemStack, PlayerEntity player);
}
