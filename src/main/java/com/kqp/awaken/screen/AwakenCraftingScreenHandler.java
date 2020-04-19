package com.kqp.awaken.screen;

import com.kqp.awaken.block.RecipeAccessProvider;
import com.kqp.awaken.client.slot.AwakenCraftingResultSlot;
import com.kqp.awaken.client.slot.AwakenLookUpResultSlot;
import com.kqp.awaken.init.AwakenNetworking;
import com.kqp.awaken.inventory.AwakenCraftingRecipeLookUpInventory;
import com.kqp.awaken.inventory.AwakenCraftingResultInventory;
import com.kqp.awaken.recipe.AwakenRecipe;
import com.kqp.awaken.recipe.AwakenRecipeManager;
import com.kqp.awaken.recipe.RecipeType;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Screen handler for the crafting GUI.
 */
public class AwakenCraftingScreenHandler extends ScreenHandler {
    public final AwakenRecipeManager awakenRecipeManager;
    
    /**
     * Inventory for the crafting results.
     */
    private final AwakenCraftingResultInventory craftingResultInventory;

    /**
     * Inventory for the look-up results.
     */
    private final AwakenCraftingRecipeLookUpInventory lookUpResultInventory;

    /**
     * Runs tasks for the server-side of things.
     * Client code should be running on {@link ScreenHandlerContext#EMPTY}, which silently fails.
     */
    private final ScreenHandlerContext context;

    /**
     * Reference to the player.
     */
    private final PlayerEntity player;

    /**
     * What recipe types to show the player.
     * See {@link RecipeType} for all of them.
     */
    public String[] recipeTypes;

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
     * Client-side constructor.
     *
     * @param syncId Sync ID
     */
    public AwakenCraftingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    /**
     * Server-side constructor.
     *
     * @param syncId          Sync ID
     * @param playerInventory Player's inventory
     * @param context         Context for executing server-side tasks
     */
    public AwakenCraftingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(null, syncId);
        // TODO: figure out screen handler type

        // Init fields
        this.awakenRecipeManager = AwakenRecipeManager.getFor(playerInventory.player.world);
        this.craftingResultInventory = new AwakenCraftingResultInventory();
        this.lookUpResultInventory = new AwakenCraftingRecipeLookUpInventory();
        this.context = context;
        this.player = playerInventory.player;

        // Crafting results inventory (24 output)
        int counter = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 8; j++) {
                this.addSlot(new AwakenCraftingResultSlot(this, craftingResultInventory, playerInventory, counter++, 8 + j * 18, 18 + i * 18));
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 9; j++) {
                    this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 84 + i * 18) {
                        @Override
                        public void markDirty() {
                            super.markDirty();
                            updateCraftingResults();
                        }
                    });
                }
            }

            for (int j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142) {
                    @Override
                    public void markDirty() {
                        super.markDirty();
                        updateCraftingResults();
                    }
                });
            }
        }

        // Look-up inventory (1 query slot + 18 result slots)
        {
            counter = 0;

            this.addSlot(new Slot(lookUpResultInventory, counter++, 209, 22) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    updateRecipeLookUpResults();
                }
            });

            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 3; j++) {
                    this.addSlot(new AwakenLookUpResultSlot(lookUpResultInventory, counter++, 186 + j * 18, 48 + i * 18));
                }
            }
        }

        this.craftingResults = new ArrayList();
        this.lookUpRecipes = new ArrayList();
        this.lookUpResults = new ArrayList();

        gatherRecipeTypes(player);
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
        if (!player.isAlive() || player instanceof ServerPlayerEntity && ((ServerPlayerEntity) player).method_14239()) {
            player.dropItem(lookUpResultInventory.getStack(0), false);
        } else {
            player.inventory.offerOrDrop(player.world, lookUpResultInventory.removeStack(0));
        }
    }

    /**
     * Updates the results given the valid recipe types and the player's inventory.
     * This method is called on every slot change.
     */
    public void updateCraftingResults() {
        craftingResultRecipes = awakenRecipeManager.getMatches(recipeTypes, player.inventory.main);
        craftingResults = craftingResultRecipes.stream().map(recipe -> recipe.result.copy()).collect(Collectors.toList());

        // If on server, notify the client that something has changed so the client can reply with the scroll bar position.
        if (!player.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AwakenNetworking.SYNC_CRAFTING_RESULTS_ID, buf);
        }
    }

    public void updateRecipeLookUpResults() {
        ItemStack query = this.lookUpResultInventory.getStack(0);

        lookUpRecipes = awakenRecipeManager.getRecipesUsingItemStack(query);
        lookUpResults = lookUpRecipes.stream().map(recipe -> recipe.result.copy()).collect(Collectors.toList());

        // If on server, notify the client that something has changed so the client can reply with the scroll bar position.
        if (!player.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AwakenNetworking.SYNC_LOOK_UP_RESULTS_ID, buf);
        }
    }

    /**
     * Handles the shift clicking.
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

                if (!this.insertItem(itemStack2, 60, 61, false)) {
                    if (!this.insertItem(itemStack2, 51, 60, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (invSlot >= 51 && invSlot < 60) {
                // Shift click inside hot-bar slots
                if (!this.insertItem(itemStack2, 60, 61, false)) {
                    if (!this.insertItem(itemStack2, 24, 51, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (invSlot == 60) {
                // Shift click inside recipe look-up slot
                if (!this.insertItem(itemStack2, 51, 60, false)) {
                    slot.markDirty();
                    return ItemStack.EMPTY;
                } else if (!this.insertItem(itemStack2, 24, 51, false)) {
                    slot.markDirty();
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

                craftingResultInventory.setStack(l + k * 8, itemStack);
                ((AwakenCraftingResultSlot) this.getSlot(l + k * 8)).currentIndex = m;

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(l + k * 8);
                buf.writeItemStack(itemStack);
                buf.writeInt(m);

                // Sends the client EACH ItemStack in the result inventory
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AwakenNetworking.SYNC_CRAFTING_RESULT_SLOT_ID, buf);
            }
        }
    }

    /**
     * Calculates what results should be in view given the position of the scroll bar.
     *
     * @param position Position of the scroll bar
     */
    public void scrollLookUpResults(float position) {
        if (!player.world.isClient) {
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

                    lookUpResultInventory.setStack(1 + slot, itemStack);
                    ((AwakenLookUpResultSlot) this.getSlot(61 + slot)).currentIndex = m;

                    PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                    buf.writeInt(61 + slot);
                    buf.writeItemStack(itemStack);
                    buf.writeInt(m);

                    // Sends the client EACH ItemStack in the result inventory
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, AwakenNetworking.SYNC_LOOK_UP_RESULT_SLOT_ID, buf);
                }
            }
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.craftingResultInventory;
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
