package com.kqp.terminus.recipe;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TerminusRecipe {
    public ItemStack result;
    public HashMap<ComparableItemStack, Integer> recipe;

    public TerminusRecipe(ItemStack result, ItemStack... input) {
        this.result = result;
        recipe = toComparableMap(Arrays.asList(input));
    }

    public boolean matches(HashMap<ComparableItemStack, Integer> input) {
        for (ComparableItemStack key : recipe.keySet()) {
            if (!input.containsKey(key)) {
                return false;
            } else {
                if (input.get(key) < recipe.get(key)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static HashMap<ComparableItemStack, Integer> toComparableMap(List<ItemStack> input) {
        HashMap<ComparableItemStack, Integer> ret = new HashMap();

        input.forEach(itemStack -> {
            ComparableItemStack key = new ComparableItemStack(itemStack);

            if (ret.containsKey(key)) {
                ret.replace(key, ret.get(key) + itemStack.getCount());
            } else {
                ret.put(key, itemStack.getCount());
            }
        });

        return ret;
    }
}
