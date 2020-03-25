package com.kqp.terminus.recipe;

import com.kqp.terminus.Terminus;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TerminusRecipes {
    public static final ArrayList<TerminusRecipe> RECIPES = new ArrayList();

    public static void init() {
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.SWORD, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.SHOVEL, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.PICKAXE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.AXE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.HELMET, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 60)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.CHESTPLATE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.LEGGINGS, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        ));
        RECIPES.add(new TerminusRecipe(
                new ItemStack(Terminus.Groups.CELESTIAL.BOOTS, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 60)
        ));
    }

    public static List<TerminusRecipe> getMatches(List<ItemStack> itemStacks) {
        ArrayList<TerminusRecipe> output = new ArrayList();
        HashMap<ComparableItemStack, Integer> input = TerminusRecipes.toComparableMap(itemStacks);

        for (TerminusRecipe recipe : RECIPES) {
            if (recipe.matches(input)) {
                output.add(recipe);
            }
        }

        return output;
    }

    public static List<TerminusRecipe> getMatchesForOutput(ItemStack itemStack) {
        ArrayList<TerminusRecipe> output = new ArrayList();

        for (TerminusRecipe recipe : RECIPES) {
            if (ItemStack.areItemsEqual(recipe.result, itemStack)) {
                output.add(recipe);
            }
        }

        return output;
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
