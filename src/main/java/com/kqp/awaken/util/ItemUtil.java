package com.kqp.awaken.util;

import com.kqp.awaken.group.ArmorGroup;
import net.minecraft.entity.player.PlayerEntity;

public class ItemUtil {
    public static boolean wearingFullSet(PlayerEntity player, ArmorGroup armor) {
        return (player.inventory.armor.get(0).getItem() == armor.BOOTS)
                && (player.inventory.armor.get(1).getItem() == armor.LEGGINGS)
                && (player.inventory.armor.get(2).getItem() == armor.CHESTPLATE)
                && (player.inventory.armor.get(3).getItem() == armor.HELMET);
    }
}
