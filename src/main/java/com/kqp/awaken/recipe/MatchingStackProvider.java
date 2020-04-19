package com.kqp.awaken.recipe;

import net.minecraft.item.ItemStack;

public interface MatchingStackProvider {
    ItemStack[] getMatchingStacks();
}
