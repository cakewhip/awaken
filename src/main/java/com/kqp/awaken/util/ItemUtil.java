package com.kqp.awaken.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DefaultedList;

public class ItemUtil {
    public static boolean wearingFullSet(PlayerEntity player, ArmorMaterial material) {
        DefaultedList<ItemStack> armor = player.inventory.armor;

        for (ItemStack itemStack : armor) {
            ArmorItem item = (ArmorItem) itemStack.getItem();

            if (item.getMaterial() != material) {
                return false;
            }
        }

        return true;
    }
}
