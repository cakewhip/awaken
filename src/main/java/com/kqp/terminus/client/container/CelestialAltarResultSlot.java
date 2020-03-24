package com.kqp.terminus.client.container;

import com.kqp.terminus.recipe.ComparableItemStack;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.recipe.TerminusRecipes;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class CelestialAltarResultSlot extends Slot {
    private final PlayerEntity player;

    public PlayerInventory craftingInv;

    public int currentIndex = 0;

    public CelestialAltarResultSlot(PlayerEntity player, Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        this.player = player;
        this.craftingInv = player.inventory;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack takeStack(int amount) {
        return super.takeStack(amount);
    }

    @Override
    protected void onCrafted(ItemStack itemStack, int amount) {
        onCrafted(itemStack);
    }

    @Override
    protected void onTake(int amount) {
    }

    @Override
    protected void onCrafted(ItemStack stack) {
        if (!stack.isEmpty()) {
            HashMap<ComparableItemStack, Integer> compMap = TerminusRecipe.toComparableMap(craftingInv.main);
            List<TerminusRecipe> matches = TerminusRecipes.getMatchesForOutput(stack);

            for (TerminusRecipe recipe : matches) {
                if (recipe.matches(compMap)) {
                    for (ComparableItemStack key : recipe.recipe.keySet()) {
                        ItemStack reagent = new ItemStack(key.item);
                        reagent.setTag(key.tag);
                        int count = recipe.recipe.get(key);

                        for (int i = 0; i < craftingInv.main.size(); i++) {
                            ItemStack itemStack = craftingInv.main.get(i);

                            if (ItemStack.areItemsEqual(reagent, itemStack)) {
                                if (itemStack.getCount() > count) {
                                    itemStack.decrement(count);
                                    break;
                                } else {
                                    count -= itemStack.getCount();
                                    craftingInv.main.set(i, ItemStack.EMPTY);
                                }
                            }
                        }
                    }

                    break;
                }
            }
        }

        markDirty();
    }

    @Override
    public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);

        return stack;
    }

    public ItemStack getStackInSlot() {
        return inventory.getInvStack(this.id);
    }
}
