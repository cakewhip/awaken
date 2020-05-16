package com.kqp.awaken.util;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;

public class TrinketUtil {
    public static boolean hasTrinket(LivingEntity entity, Item trinket) {
        return entity.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == trinket
                || entity.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == trinket;
    }
}
