package com.kqp.awaken.client.container;

import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.ComparableItemStack;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class AwakenResultSlot extends Slot {
    private final AwakenCraftingContainer container;
    private final PlayerEntity player;

    public PlayerInventory craftingInv;

    public int currentIndex = 0;

    public AwakenResultSlot(AwakenCraftingContainer container, PlayerEntity player, Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        this.player = player;
        this.container = container;
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
            HashMap<ComparableItemStack, Integer> compMap = AwakenRecipeManager.toComparableMap(craftingInv.main);
            List<AwakenRecipe> matches = AwakenRecipeManager.getMatchesForOutput(container.recipeTypes, stack);

            for (AwakenRecipe recipe : matches) {
                if (recipe.matches(compMap)) {
                    recipe.onCraft(craftingInv);
                    break;
                }
            }
        }

        markDirty();
        container.updateResult();
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
