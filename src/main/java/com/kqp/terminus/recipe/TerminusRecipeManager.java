package com.kqp.terminus.recipe;

import com.kqp.terminus.Terminus;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TerminusRecipeManager {
    private static final HashMap<String, ArrayList<TerminusRecipe>> RECIPE_MAP = new HashMap();

    public static void init() {
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.SWORD, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.SHOVEL, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.PICKAXE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.AXE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.HELMET, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 60)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.CHESTPLATE, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 96)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.LEGGINGS, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 72)
        );
        addRecipe("reinforced_anvil", new ItemStack(Terminus.Groups.CELESTIAL.BOOTS, 1),
                new ItemStack(Terminus.TItems.CELESTIAL_STEEL_INGOT, 60)
        );
    }
    
    public static void addRecipe(String type, ItemStack output, ItemStack... inputs) {
        add(type, new TerminusRecipe(output, inputs));
    }

    public static void addRecipe(String type, ItemStack output, HashMap<Reagent, Integer> reagents) {
        add(type, new TerminusRecipe(output, reagents));
    }

    private static void add(String type, TerminusRecipe recipe) {
        if (!RECIPE_MAP.containsKey(type)) {
            RECIPE_MAP.put(type, new ArrayList());
        }

        RECIPE_MAP.get(type).add(recipe);
    }

    public static List<TerminusRecipe> getMatches(String[] types, List<ItemStack> itemStacks) {
        ArrayList<TerminusRecipe> output = new ArrayList();
        HashMap<ComparableItemStack, Integer> input = TerminusRecipeManager.toComparableMap(itemStacks);

        for (String type : types) {
            List<TerminusRecipe> recipes = RECIPE_MAP.get(type);

            for (TerminusRecipe recipe : recipes) {
                if (recipe.matches(input)) {
                    output.add(recipe);
                }
            }
        }


        output.sort(Comparator.comparing(TerminusRecipe::getSortString));

        return output;
    }

    public static List<TerminusRecipe> getMatchesForOutput(String[] types, ItemStack itemStack) {
        ArrayList<TerminusRecipe> output = new ArrayList();

        for (String type : types) {
            List<TerminusRecipe> recipes = RECIPE_MAP.get(type);

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
