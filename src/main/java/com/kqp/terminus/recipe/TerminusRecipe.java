package com.kqp.terminus.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Represents a Terminus recipe with an ItemStack result and a map of reagents to their counts.
 */
public class TerminusRecipe {
    public ItemStack result;
    public HashMap<Reagent, Integer> reagents;

    /**
     * A pre-calculated map of what item stacks match with a given reagent.
     * I consider this the secret sauce of the Terminus recipe system.
     */
    public HashMap<ComparableItemStack, Reagent> itemStackReagentMap;

    private TerminusRecipe() {
        this.reagents = new HashMap();
        this.itemStackReagentMap = new HashMap();
    }

    public TerminusRecipe(ItemStack result, ItemStack... input) {
        this();

        this.result = result;

        Arrays.stream(input).forEach(inputStack -> {
            Reagent reagent = new Reagent(inputStack);
            reagents.put(reagent, inputStack.getCount());
            itemStackReagentMap.put(new ComparableItemStack(inputStack), reagent);
        });
    }

    public TerminusRecipe(ItemStack result, HashMap<Reagent, Integer> reagents) {
        this();

        this.result = result;
        this.reagents = reagents;

        reagents.keySet().forEach(reagent -> {
            reagent.matchingStacks.forEach(matchingStack -> {
                itemStackReagentMap.put(matchingStack, reagent);
            });
        });
    }

    /**
     * If this recipe matches a passed map of item stacks and their counts.
     *
     * @param itemStacks Input map of item stacks
     * @return Whether this recipe matches the passed map
     */
    public boolean matches(HashMap<ComparableItemStack, Integer> itemStacks) {
        // Used to keep track of what matching reagents the passed map of item stacks has
        HashMap<Reagent, Integer> reagentMatchMap = new HashMap();

        // If the item stack matches a required reagent, it places it into the match map with a count.
        itemStacks.forEach((itemStack, count) -> {
            if (itemStackReagentMap.containsKey(itemStack)) {
                Reagent matchingReagent = itemStackReagentMap.get(itemStack);

                if (reagentMatchMap.containsKey(matchingReagent)) {
                    reagentMatchMap.replace(matchingReagent, count + reagentMatchMap.get(matchingReagent));
                } else {
                    reagentMatchMap.put(matchingReagent, count);
                }
            }
        });

        // Iterate through the required reagents and what this map has.
        // Returns false if it sees a required reagent is missing or doesn't have enough.
        for (Reagent reagent : reagents.keySet()) {
            if (!reagentMatchMap.containsKey(reagent) || reagentMatchMap.get(reagent) < reagents.get(reagent)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Called whenever this recipe is crafted.
     * Consumes required reagents from the passed inventory.
     *
     * @param craftInv Inventory used to craft the recipe.
     */
    public void onCraft(Inventory craftInv) {
        HashMap<Reagent, Integer> reagentMatchMap = new HashMap();

        for (int i = 0; i < craftInv.getInvSize(); i++) {
            ItemStack itemStack = craftInv.getInvStack(i);
            ComparableItemStack comparableItemStack = new ComparableItemStack(itemStack);
            int count = itemStack.getCount();

            if (itemStackReagentMap.containsKey(comparableItemStack)) {
                Reagent matchingReagent = itemStackReagentMap.get(comparableItemStack);
                int already = reagentMatchMap.getOrDefault(matchingReagent, 0);
                int required = reagents.get(matchingReagent);

                if (already < required) {
                    int needed = required - already;

                    if (count > needed) {
                        itemStack.decrement(needed);
                        reagentMatchMap.put(matchingReagent, required);
                    } else if (count <= needed) {
                        reagentMatchMap.put(matchingReagent, already + count);
                        craftInv.setInvStack(i, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    public String getSortString() {
        String ret = new LiteralText("").append(result.getName()).asFormattedString();

        return ret != null ? ret : "";
    }
}
