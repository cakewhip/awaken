package com.kqp.terminus.recipe;

import com.google.gson.annotations.Expose;
import com.kqp.terminus.Terminus;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.text.LiteralText;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TerminusRecipe {
    public ItemStack result;
    public HashMap<Reagent, Integer> reagents;

    @Expose(serialize = false, deserialize = false)
    public HashMap<ComparableItemStack, Reagent> itemStackReagentMap;

    public TerminusRecipe() {
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

    public boolean matches(HashMap<ComparableItemStack, Integer> itemStacks) {
        HashMap<Reagent, Integer> reagentMatchMap = new HashMap();

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

        for (Reagent reagent : reagents.keySet()) {
            if (!reagentMatchMap.containsKey(reagent) || reagentMatchMap.get(reagent) < reagents.get(reagent)) {
                return false;
            }
        }

        return true;
    }

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
