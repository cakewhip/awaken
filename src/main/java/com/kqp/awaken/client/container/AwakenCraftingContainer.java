package com.kqp.awaken.client.container;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.block.RecipeAccessProvider;
import com.kqp.awaken.inventory.AwakenCraftingRecipeLookUpInventory;
import com.kqp.awaken.inventory.AwakenCraftingResultInventory;
import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.PacketByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Container for Awaken's crafting GUI.
 * Has 24 result slots {@link AwakenCraftingResultSlot} and the player's inventory slots.
 * Decides what recipes to show the player at {@link #gatherRecipeTypes(PlayerEntity)}.
 */
public class AwakenCraftingContainer extends Container {
    /**
     * The player's inventory.
     */
    public final PlayerInventory playerInventory;

    /**
     * What recipe types to show the player.
     * See {@link RecipeType} for all of them.
     */
    public String[] recipeTypes;

    /**
     * The crafting result inventory.
     */
    public final AwakenCraftingResultInventory craftingResultInventory;

    /**
     * The usages look-up inventory.
     */
    public final AwakenCraftingRecipeLookUpInventory lookUpInventory;

    /**
     * All the current valid recipes.
     * Calculated on both client and server side because I couldn't think of a better way.
     * Probably a better way, but screw it.
     */
    public List<AwakenRecipe> craftingResultRecipes;

    /**
     * The recipes resulting from the usage look-up.
     */
    public List<AwakenRecipe> lookUpRecipes;

    /**
     * All the current crafting results.
     * Calculated on both client and server side, because see {@link #craftingResultRecipes}.
     */
    public List<ItemStack> craftingResults;

    /**
     * The results of the usage look-up.
     */
    public List<ItemStack> lookUpResults;

    /**
     * Creates a new instance of the container.
     *
     * @param syncId          Sync ID used by Minecraft to sync the container
     * @param playerInventory The player's inventory
     */
    public AwakenCraftingContainer(int syncId, PlayerInventory playerInventory) {
        super(null, syncId);

        this.playerInventory = playerInventory;

        gatherRecipeTypes(playerInventory.player);

        this.craftingResultInventory = new AwakenCraftingResultInventory();
        this.lookUpInventory = new AwakenCraftingRecipeLookUpInventory();

        this.lookUpRecipes = new ArrayList();
        this.craftingResults = new ArrayList();
        this.lookUpResults = new ArrayList();

        // Crafting results inventory (24 output)
        int i, j;
        int counter = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                this.addSlot(new AwakenCraftingResultSlot(this, playerInventory.player, craftingResultInventory, counter++, 8 + j * 18, 18 + i * 18));
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 84 + i * 18) {
                    @Override
                    public void markDirty() {
                        super.markDirty();
                        updateCraftingResults();
                    }
                });
            }
        }

        for (j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    updateCraftingResults();
                }
            });
        }

        counter = 0;

        // Look-up inventory (1 query slot + 18 result slots)
        this.addSlot(new Slot(lookUpInventory, counter++, 209, 22) {
            @Override
            public void markDirty() {
                super.markDirty();
                updateRecipeLookUpResults();
            }
        });


        for (i = 0; i < 6; i++) {
            for (j = 0; j < 3; j++) {
                this.addSlot(new AwakenLookUpResultSlot(lookUpInventory, counter++, 186 + j * 18, 48 + i * 18) {
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return false;
                    }
                });
            }
        }

        // Update initial results
        updateCraftingResults();
    }

    /**
     * Gathers the valid recipe types given the player's proximity to blocks implementing {@link RecipeAccessProvider}.
     * Also gives recipes for the vanilla crafting table, furnace, and anvil.
     *
     * @param player The player
     */
    private void gatherRecipeTypes(PlayerEntity player) {
        HashSet<String> types = new HashSet<>();

        // 2x2 recipes should always be accessible
        types.add(RecipeType.TWO_BY_TWO);

        // Finds blocks within a 6x3x6 box
        for (int x = -3; x < 4; x++) {
            for (int z = -3; z < 4; z++) {
                for (int y = -1; y < 3; y++) {
                    Block block = player.world.getBlockState(player.getBlockPos().add(x, y, z)).getBlock();

                    if (block instanceof RecipeAccessProvider) {
                        types.addAll(Arrays.asList(((RecipeAccessProvider) block).getRecipeTypes()));
                    } else if (block == Blocks.CRAFTING_TABLE) {
                        types.add(RecipeType.CRAFTING_TABLE);
                    } else if (block == Blocks.FURNACE) {
                        types.add(RecipeType.FURNACE);
                    } else if (block == Blocks.ANVIL) {
                        types.add(RecipeType.ANVIL);
                    }
                }
            }
        }

        recipeTypes = types.toArray(new String[0]);
    }

    /**
     * Detect on close to drop stuff.
     *
     * @param player PlayerEntity
     */
    @Override
    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity)player).method_14239()) {
            player.dropItem(lookUpInventory.getInvStack(0), false);
        } else {
            player.inventory.offerOrDrop(player.world, lookUpInventory.removeInvStack(0));
        }
    }

    /**
     * Updates the results given the valid recipe types and the player's inventory.
     * This method is called on every slot change.
     */
    public void updateCraftingResults() {
        craftingResultRecipes = AwakenRecipeManager.getMatches(recipeTypes, playerInventory.main);
        craftingResults = craftingResultRecipes.stream().map(recipe -> recipe.result.copy()).collect(Collectors.toList());

        // If on server, notify the client that something has changed so the client can reply with the scroll bar position.
        if (!playerInventory.player.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Awaken.TNetworking.SYNC_CRAFTING_RESULTS_ID, buf);
        }
    }

    public void updateRecipeLookUpResults() {
        ItemStack query = this.lookUpInventory.getInvStack(0);

        lookUpRecipes = AwakenRecipeManager.getRecipesUsingItemStack(query);
        lookUpResults = lookUpRecipes.stream().map(recipe -> recipe.result.copy()).collect(Collectors.toList());

        // If on server, notify the client that something has changed so the client can reply with the scroll bar position.
        if (!playerInventory.player.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Awaken.TNetworking.SYNC_LOOK_UP_RESULTS_ID, buf);
        }
    }

    /**
     * Handles the shift clicking. I honestly can't explain it further than that.
     *
     * @param player  The player
     * @param invSlot The slot that is being shift clicked (I think)
     * @return The ItemStack (I don't know)
     */
    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(invSlot);

        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();

            if (invSlot < 24) {
                // Shift click inside result slots

                if (!this.insertItem(itemStack2, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (invSlot >= 24 && invSlot < 51) {
                // Shift click inside main inventory

                if (!this.insertItem(itemStack2, 51, 60, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (invSlot >= 51 && invSlot < 60) {
                // Shift click inside hot-bar slots
                if (!this.insertItem(itemStack2, 24, 51, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (invSlot == 60) {
                // Shift click inside recipe look-up slot
                if (!this.insertItem(itemStack2, 51, 60, false)) {
                    return ItemStack.EMPTY;
                } else if (!this.insertItem(itemStack2, 24, 51, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (itemStack2.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            if (itemStack2.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            ItemStack itemStack3 = slot.onTakeItem(player, itemStack2);
            if (invSlot == 0) {
                player.dropItem(itemStack3, false);
            }
        }

        return itemStack;
    }

    /**
     * Calculates what outputs should be in view given the position of the scroll bar.
     *
     * @param position Position of the scroll bar
     */
    public void scrollOutputs(float position) {
        int i = (this.craftingResults.size() + 8 - 1) / 8 - 3;
        int j = (int) ((double) (position * (float) i) + 0.5D);
        if (j < 0) {
            j = 0;
        }

        for (int k = 0; k < 3; ++k) {
            for (int l = 0; l < 8; ++l) {
                int m = l + (k + j) * 8;

                ItemStack itemStack = ItemStack.EMPTY;

                if (m >= 0 && m < this.craftingResults.size()) {
                    itemStack = this.craftingResults.get(m);
                }

                craftingResultInventory.setInvStack(l + k * 8, itemStack);
                ((AwakenCraftingResultSlot) this.getSlot(l + k * 8)).currentIndex = m;

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(l + k * 8);
                buf.writeItemStack(itemStack);
                buf.writeInt(m);

                // Sends the client EACH ItemStack in the result inventory
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Awaken.TNetworking.SYNC_CRAFTING_RESULT_SLOT_ID, buf);
            }
        }
    }

    /**
     * Calculates what results should be in view given the position of the scroll bar.
     *
     * @param position Position of the scroll bar
     */
    public void scrollLookUpResults(float position) {
        if (!playerInventory.player.world.isClient) {
            int i = (this.lookUpResults.size() + 3 - 1) / 3 - 6;
            int j = (int) ((double) (position * (float) i) + 0.5D);

            if (j < 0) {
                j = 0;
            }

            for (int k = 0; k < 6; ++k) {
                for (int l = 0; l < 3; ++l) {
                    int m = (k + j) * 3 + l;

                    ItemStack itemStack = ItemStack.EMPTY;

                    if (m >= 0 && m < this.lookUpResults.size()) {
                        itemStack = this.lookUpResults.get(m);
                    }

                    int slot = l + k * 3;

                    lookUpInventory.setInvStack(1 + slot, itemStack);
                    ((AwakenLookUpResultSlot) this.getSlot(61 + slot)).currentIndex = m;

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(61 + slot);
                    buf.writeItemStack(itemStack);
                    buf.writeInt(m);

                    // Sends the client EACH ItemStack in the result inventory
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Awaken.TNetworking.SYNC_LOOK_UP_RESULT_SLOT_ID, buf);
                }
            }
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftingResultInventory && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    public boolean shouldShowOutputsScrollbar() {
        return this.craftingResults.size() > 24;
    }

    public boolean shouldShowLookUpResultsScrollbar() {
        return this.lookUpResults.size() > 18;
    }
}
