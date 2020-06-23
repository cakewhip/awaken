package com.kqp.awaken.recipe.dynamic;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.alchemy.PotionBagItem;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.ComparableItemStack;
import com.kqp.awaken.recipe.Reagent;
import com.kqp.awaken.recipe.RecipeType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.nbt.CompoundTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PotionBagRecipe extends DynamicAwakenRecipe {
    @Override
    public List<AwakenRecipe> getPossibleRecipes(Map<ComparableItemStack, Integer> inputMap) {
        List<AwakenRecipe> outputs = new ArrayList();

        Set<ComparableItemStack> potionBags = new HashSet();
        Map<ComparableItemStack, Integer> potions = new HashMap();

        inputMap.forEach((inStack, count) -> {
            if (inStack.item == AwakenItems.Alchemist.POTION_BAG) {
                potionBags.add(inStack);
            } else if (inStack.item instanceof PotionItem) {
                potions.put(inStack, count);
            }
        });

        potionBags.forEach(potionBag -> {
            int maxCapacity = ((PotionBagItem) potionBag.item).maxCapacity;
            CompoundTag tag = potionBag.tag.getCompound("PotionBag");
            int capacity = tag.getInt("Capacity");

            if (capacity < maxCapacity) {
                ItemStack storedPotion = null;

                if (capacity > 0) {
                    storedPotion = ItemStack.fromTag(tag.getCompound("StoredPotion"));
                }

                for (ComparableItemStack potion : potions.keySet()) {
                    ItemStack potionStack = null;
                    int potionCount = potions.get(potion);

                    if (storedPotion == null) {
                        potionStack = potion.itemStack;
                    } else if (ItemStack.areEqual(storedPotion, potion.itemStack)) {
                        potionStack = potion.itemStack;
                    }

                    if (potionStack != null) {
                        int consumeAmount = 0;
                        int newCapacity = 0;

                        if (capacity + potionCount <= maxCapacity) {
                            consumeAmount = potionCount;
                            newCapacity = capacity + consumeAmount;
                        } else {
                            consumeAmount = maxCapacity - capacity;
                            newCapacity = maxCapacity;
                        }

                        ItemStack output = potionBag.itemStack.copy();
                        CompoundTag newTag = output.getOrCreateSubTag("PotionBag");

                        CompoundTag newPotionTag = new CompoundTag();
                        potionStack.toTag(newPotionTag);
                        newTag.put("StoredPotion", newPotionTag);

                        newTag.putInt("Capacity", newCapacity);

                        HashMap<Reagent, Integer> reagents = new HashMap();
                        reagents.put(new Reagent(potionBag.itemStack), 1);
                        reagents.put(new Reagent(potionStack), consumeAmount);

                        outputs.add(new AwakenRecipe(
                                RecipeType.TWO_BY_TWO,
                                output,
                                reagents
                        ));
                    }
                }
            }
        });

        return outputs;
    }
}
