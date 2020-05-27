package com.kqp.awaken.util;

import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static boolean hasAllTrinkets(PlayerEntity entity, Item... trinkets) {
        Set<Item> trinketSet = Arrays.stream(trinkets).collect(Collectors.toSet());

        Inventory trinketInv = TrinketsApi.getTrinketsInventory(entity);

        for (int i = 0; i < trinketInv.size(); i++) {
            ItemStack itemStack = trinketInv.getStack(i);

            trinketSet.remove(itemStack.getItem());
        }

        return trinketSet.isEmpty();
    }

    public static boolean hasAnyTrinkets(PlayerEntity entity, Item... trinkets) {
        Set<Item> trinketSet = Arrays.stream(trinkets).collect(Collectors.toSet());

        Inventory trinketInv = TrinketsApi.getTrinketsInventory(entity);

        for (int i = 0; i < trinketInv.size(); i++) {
            ItemStack itemStack = trinketInv.getStack(i);

            if (trinketSet.contains(itemStack.getItem())) {
                return true;
            }
        }

        return false;
    }
}
