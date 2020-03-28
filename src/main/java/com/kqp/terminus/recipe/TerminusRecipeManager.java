package com.kqp.terminus.recipe;

import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class TerminusRecipeManager {
    private static final HashMap<String, ArrayList<TerminusRecipe>> RECIPE_MAP = new HashMap();

    public static void addRecipe(String type, ItemStack output, HashMap<Reagent, Integer> reagents) {
        addRecipe(type, new TerminusRecipe(output, reagents));
    }

    public static void addRecipe(String type, TerminusRecipe recipe) {
        getRecipesForType(type).add(recipe);
    }

    private static List<TerminusRecipe> getRecipesForType(String type) {
        if (!RECIPE_MAP.containsKey(type)) {
            RECIPE_MAP.put(type, new ArrayList());
        }

        return RECIPE_MAP.get(type);
    }

    public static List<TerminusRecipe> getMatches(String[] types, List<ItemStack> itemStacks) {
        ArrayList<TerminusRecipe> output = new ArrayList();
        HashMap<ComparableItemStack, Integer> input = TerminusRecipeManager.toComparableMap(itemStacks);

        for (String type : types) {
            List<TerminusRecipe> recipes = getRecipesForType(type);

            output.addAll(recipes.parallelStream().filter(recipe -> recipe.matches(input)).collect(Collectors.toList()));
        }

        output.sort(Comparator.comparing(TerminusRecipe::getSortString));

        return output;
    }

    public static List<TerminusRecipe> getMatchesForOutput(String[] types, ItemStack itemStack) {
        ArrayList<TerminusRecipe> output = new ArrayList();

        for (String type : types) {
            List<TerminusRecipe> recipes = getRecipesForType(type);

            for (TerminusRecipe recipe : recipes) {
                if (ItemStack.areItemsEqual(recipe.result, itemStack)) {
                    output.add(recipe);
                }
            }
        }

        return output;
    }

    public static HashMap<ComparableItemStack, Integer> toComparableMap(List<ItemStack> input) {
        HashMap<ComparableItemStack, Integer> ret = new HashMap();

        input.stream().forEach(itemStack -> {
            ComparableItemStack key = new ComparableItemStack(itemStack);

            if (ret.containsKey(key)) {
                ret.replace(key, ret.get(key) + itemStack.getCount());
            } else {
                ret.put(key, itemStack.getCount());
            }
        });

        return ret;
    }

    public static void clear() {
        RECIPE_MAP.clear();
    }

    public static void sort() {
        RECIPE_MAP.forEach((type, recipes) -> {
            recipes.sort(Comparator.comparing(TerminusRecipe::getSortString));
        });
    }
}
