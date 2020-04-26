package com.kqp.awaken.item.effect;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Used to detect full sets of armor.
 */
public interface ArmorListener {
    void onOtherEquip(ItemStack stack, PlayerEntity player);

    void onOtherUnEquip(ItemStack stack, PlayerEntity player);
}
