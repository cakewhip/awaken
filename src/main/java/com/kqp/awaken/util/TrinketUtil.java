package com.kqp.awaken.util;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TrinketUtil {
    public static boolean hasTrinket(PlayerEntity entity, Item trinket) {
        Inventory trinkets = TrinketsApi.getTrinketsInventory(entity);

        for (int i = 0; i < trinkets.size(); i++) {
            ItemStack itemStack = trinkets.getStack(i);

            if (itemStack.getItem() == trinket) {
                return true;
            }
        }

        return false;
    }
}
