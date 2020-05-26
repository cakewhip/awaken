package com.kqp.awaken.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Util class for handling armor.
 */
public class ArmorUtil {
    public static boolean wearingFullSet(LivingEntity living, ArmorMaterial material) {
        List<ItemStack> armorStacks = new ArrayList();

        armorStacks.add(living.getEquippedStack(EquipmentSlot.HEAD));
        armorStacks.add(living.getEquippedStack(EquipmentSlot.CHEST));
        armorStacks.add(living.getEquippedStack(EquipmentSlot.LEGS));
        armorStacks.add(living.getEquippedStack(EquipmentSlot.FEET));

        for (ItemStack itemStack : armorStacks) {
            if (itemStack.isEmpty()) {
                return false;
            } else if (itemStack.getItem() instanceof ArmorItem) {
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
