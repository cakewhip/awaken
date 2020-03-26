package com.kqp.terminus.client.container;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.inventory.CelestialAltarResultInventory;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.recipe.TerminusRecipeManager;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.PacketByteBuf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CelestialAltarContainer extends Container {
    public static final String[] CRAFTING_TYPES = { "vanilla_crafting", "reinforced_anvil" };

    public final PlayerInventory playerInventory;
    public final BlockContext context;

    public final CelestialAltarResultInventory resultInv;

    public List<TerminusRecipe> recipes;
    public List<ItemStack> outputs;

    public CelestialAltarContainer(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(null, syncId);

        this.context = context;
        this.playerInventory = playerInventory;

        resultInv = new CelestialAltarResultInventory();
        this.outputs = new ArrayList();

        // Celestial Altar inventory (24 output)
        int i, j;
        int counter = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 8; j++) {
                this.addSlot(new CelestialAltarResultSlot(playerInventory.player, resultInv, counter++, 8 + j * 18, 18 + i * 18) {
                    @Override
                    public void markDirty() {
                        super.markDirty();
                        updateResult();
                    }
                });
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 84 + i * 18) {
                    @Override
                    public void markDirty() {
                        super.markDirty();
                        updateResult();
                    }
                });
            }
        }

        for (j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142) {
                @Override
                public void markDirty() {
                    super.markDirty();
                    updateResult();
                }
            });
        }

        updateResult();
    }

    public void updateResult() {
        recipes = TerminusRecipeManager.getMatches(CRAFTING_TYPES, playerInventory.main);
        outputs = recipes.stream().map(recipe -> recipe.result.copy()).collect(Collectors.toList());

        if (!playerInventory.player.world.isClient) {
            PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Terminus.TNetworking.SYNC_RESULTS_ID, buf);
        }
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) -> {
            updateResult();
        });
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return canUse(this.context, player, Terminus.TBlocks.CELESTIAL_ALTAR_BLOCK);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int invSlot) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(invSlot);
        if (slot != null && slot.hasStack()) {
            ItemStack itemStack2 = slot.getStack();
            itemStack = itemStack2.copy();
            if (invSlot < 24) {
                if (!this.insertItem(itemStack2, 24, 60, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (invSlot >= 24 && invSlot < 51) {
                if (!this.insertItem(itemStack2, 51, 60, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStack2, 24, 51, false)) {
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

    public void scrollItems(float position) {
        int i = (this.outputs.size() + 8 - 1) / 8 - 3;
        int j = (int)((double)(position * (float)i) + 0.5D);
        if (j < 0) {
            j = 0;
        }

        for(int k = 0; k < 3; ++k) {
            for(int l = 0; l < 8; ++l) {
                int m = l + (k + j) * 8;

                ItemStack itemStack = ItemStack.EMPTY;

                if (m >= 0 && m < this.outputs.size()) {
                    itemStack = this.outputs.get(m);
                }

                resultInv.setInvStack(l + k * 8, itemStack);
                ((CelestialAltarResultSlot) this.getSlot(l + k * 8)).currentIndex = m;

                PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
                buf.writeInt(l + k * 8);
                buf.writeItemStack(itemStack);
                buf.writeInt(m);

                ServerSidePacketRegistry.INSTANCE.sendToPlayer(playerInventory.player, Terminus.TNetworking.SYNC_RESULT_SLOT_ID, buf);
            }
        }

    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultInv && super.canInsertIntoSlot(stack, slot);
    }

    public boolean shouldShowScrollbar() {
        return this.outputs.size() > 24;
    }
}
