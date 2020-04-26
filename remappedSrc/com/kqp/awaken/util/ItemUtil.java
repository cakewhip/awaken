package com.kqp.awaken.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

/**
 * Util class for handling items.
 */
public class ItemUtil {
    public static boolean wearingFullSet(PlayerEntity player, ArmorMaterial material) {
        DefaultedList<ItemStack> armor = player.inventory.armor;

        for (ItemStack itemStack : armor) {
            if (itemStack.getItem() instanceof ArmorItem) {
                ArmorItem item = (ArmorItem) itemStack.getItem();

                if (item.getMaterial() != material) {
                    return false;
                }
            } else {
                return false;
            }
        }

        return true;
    }
}
