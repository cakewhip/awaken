package com.kqp.terminus.client.container;

import com.kqp.terminus.inventory.CelestialAltarInventory;
import com.kqp.terminus.recipe.ComparableItemStack;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.recipe.TerminusRecipes;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class CelestialAltarResultSlot extends Slot {
    private final CelestialAltarInventory craftingInv;
    private final PlayerEntity player;
    private int amount;

    public CelestialAltarResultSlot(PlayerEntity player, CelestialAltarInventory craftingInv, Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        this.player = player;
        this.craftingInv = craftingInv;
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onTake(int amount) {
        this.amount += amount;
    }

    protected void onCrafted(ItemStack stack) {
        if (this.amount > 0) {
            stack.onCraft(this.player.world, this.player, this.amount);
        }

        this.amount = 0;
    }

    public ItemStack onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);

        Optional<TerminusRecipe> optional = TerminusRecipes.getFirstMatch(craftingInv.getItemStacks());

        if (optional.isPresent()) {
            TerminusRecipe recipe = optional.get();
            for (ComparableItemStack key : recipe.recipe.keySet()) {
                ItemStack reagent = new ItemStack(key.item);
                reagent.setTag(key.tag);
                int count = recipe.recipe.get(key);

                for (int i = 0; i < craftingInv.getInvSize(); i++) {
                    ItemStack itemStack = craftingInv.getInvStack(i);

                    if (ItemStack.areItemsEqual(reagent, itemStack)) {
                        if (itemStack.getCount() > count) {
                            itemStack.decrement(count);
                            break;
                        } else {
                            count -= itemStack.getCount();
                            craftingInv.setInvStack(i, ItemStack.EMPTY);
                        }
                    }
                }
            }
        }

        return stack;
    }
}
