package com.kqp.terminus.client.container;

import com.kqp.terminus.recipe.ComparableItemStack;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.recipe.TerminusRecipeManager;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.List;

public class TerminusResultSlot extends Slot {
    private final String[] craftingTypes;
    private final PlayerEntity player;

    public PlayerInventory craftingInv;

    public int currentIndex = 0;

    public TerminusResultSlot(PlayerEntity player, String[] craftingTypes, Inventory inventory, int invSlot, int xPosition, int yPosition) {
        super(inventory, invSlot, xPosition, yPosition);
        this.player = player;
        this.craftingTypes = craftingTypes;
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
            HashMap<ComparableItemStack, Integer> compMap = TerminusRecipeManager.toComparableMap(craftingInv.main);
            List<TerminusRecipe> matches = TerminusRecipeManager.getMatchesForOutput(craftingTypes, stack);

            for (TerminusRecipe recipe : matches) {
                if (recipe.matches(compMap)) {
                    recipe.onCraft(craftingInv);
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
