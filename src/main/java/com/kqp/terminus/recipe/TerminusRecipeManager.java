package com.kqp.terminus.recipe;

import net.minecraft.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages all of the Terminus recipes.
 */
public class TerminusRecipeManager {
    /**
     * Map of recipe types ({@link RecipeType}) to a list of recipes.
     */
    private static final HashMap<String, ArrayList<TerminusRecipe>> RECIPE_MAP = new HashMap();

    public static void addRecipe(String type, ItemStack output, HashMap<Reagent, Integer> reagents) {
        addRecipe(type, new TerminusRecipe(output, reagents));
    }

    public static void addRecipe(String type, TerminusRecipe recipe) {
        getRecipesForType(type).add(recipe);
    }

    /**
     * Returns a list of recipes for a given recipe type.
     *
     * @param type RecipeType
     * @return List of corresponding recipes
     */
    private static List<TerminusRecipe> getRecipesForType(String type) {
        if (!RECIPE_MAP.containsKey(type)) {
            RECIPE_MAP.put(type, new ArrayList());
        }

        return RECIPE_MAP.get(type);
    }

    /**
     * Returns a list of recipes that a passed list of item stacks can craft.
     *
     * @param types      Recipe types to access
     * @param itemStacks Input item stacks
     * @return List of possible recipes
     */
    public static List<TerminusRecipe> getMatches(String[] types, List<ItemStack> itemStacks) {
        ArrayList<TerminusRecipe> output = new ArrayList();
        HashMap<ComparableItemStack, Integer> input = TerminusRecipeManager.toComparableMap(itemStacks);

        for (String type : types) {
            List<TerminusRecipe> recipes = getRecipesForType(type);

            // Parallel-ized to improve performance (very marginally)
            output.addAll(recipes.parallelStream().filter(recipe -> recipe.matches(input)).collect(Collectors.toList()));
        }

        // Sort for that hot UX
        output.sort(Comparator.comparing(TerminusRecipe::getSortString));

        return output;
    }

    /**
     * Returns a list of recipes that have the passed item stack as an output.
     *
     * @param types     Recipe types to access
     * @param itemStack Item stack output
     * @return List of recipes that have the passed item stack as an output
     */
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

    /**
     * Converts the passed list of item stacks to a map of comparable item stack objects to their counts.
     *
     * @param input List of item stacks to convert
     * @return Map of comparable item stacks to their counts
     */
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
