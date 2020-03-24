package com.kqp.terminus.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class TerminusRecipes {
    public static final ArrayList<TerminusRecipe> RECIPES = new ArrayList();

    static {
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Items.BEDROCK, 1),
                new ItemStack(Items.DIAMOND, 72),
                new ItemStack(Items.FISHING_ROD, 1),
                new ItemStack(Items.DIRT, 2)
        ));
    }

    public static Optional<TerminusRecipe> getFirstMatch(List<ItemStack> itemStacks) {
        HashMap<ComparableItemStack, Integer> input = TerminusRecipe.toComparableMap(itemStacks);

        for (TerminusRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                return Optional.of(recipe);
            }
        }

        return Optional.empty();
    }
}
