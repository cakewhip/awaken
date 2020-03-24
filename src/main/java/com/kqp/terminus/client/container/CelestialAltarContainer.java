package com.kqp.terminus.client.container;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.inventory.CelestialAltarInventory;
import com.kqp.terminus.inventory.CelestialAltarResultInventory;
import com.kqp.terminus.recipe.ComparableItemStack;
import com.kqp.terminus.recipe.TerminusRecipe;
import com.kqp.terminus.recipe.TerminusRecipes;
import net.minecraft.container.BlockContext;
import net.minecraft.container.Container;
import net.minecraft.container.Slot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ContainerSlotUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

import java.util.Optional;

public class CelestialAltarContainer extends Container {
    public final PlayerInventory playerInventory;
    public final BlockContext context;

    public final CelestialAltarInventory craftingInv;
    public final CelestialAltarResultInventory resultInv;

    public CelestialAltarContainer(int syncId, PlayerInventory playerInventory, BlockContext context) {
        super(null, syncId);

        this.context = context;
        this.playerInventory = playerInventory;

        craftingInv = new CelestialAltarInventory(this);
        resultInv = new CelestialAltarResultInventory();

        // Celestial Altar inventory (1 output + 15 input)
        this.addSlot(new CelestialAltarResultSlot(playerInventory.player, craftingInv, resultInv, 0, 138, 36));

        int i, j;
        int counter = 0;
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 5; j++) {
                this.addSlot(new Slot(craftingInv, counter++, 8 + j * 18, 18 + i * 18));
            }
        }

        // Player Inventory (27 storage + 9 hotbar)
        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, i * 9 + j + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (j = 0; j < 9; j++) {
            this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
        }
    }

    @Override
    public void close(PlayerEntity player) {
        super.close(player);

        this.context.run((world, blockPos) -> {
            this.dropInventory(player, world, this.craftingInv);
        });
    }

    public void updateResult(int syncId, World world, PlayerEntity player, CelestialAltarInventory craftingInventory, CelestialAltarResultInventory resultInventory) {
        if (!world.isClient) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)player;
            ItemStack itemStack = ItemStack.EMPTY;

            Optional<TerminusRecipe> optional = TerminusRecipes.getFirstMatch(craftingInventory.getItemStacks());
            System.out.println(TerminusRecipes.RECIPES);
            if (optional.isPresent()) {
                itemStack = optional.get().result.copy();
            }

            resultInventory.setInvStack(0, itemStack);
            serverPlayerEntity.networkHandler.sendPacket(new ContainerSlotUpdateS2CPacket(syncId, 0, itemStack));
        }
    }

    public void onContentChanged(Inventory inventory) {
        this.context.run((world, blockPos) -> {
            updateResult(this.syncId, world, playerInventory.player, this.craftingInv, this.resultInv);
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
            if (invSlot == 0) {
                this.context.run((world, blockPos) -> {
                    itemStack2.getItem().onCraft(itemStack2, world, player);
                });
                if (!this.insertItem(itemStack2, 16, 52, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onStackChanged(itemStack2, itemStack);
            } else if (invSlot >= 16 && invSlot < 52) {
                if (!this.insertItem(itemStack2, 1, 16, false)) {
                    if (invSlot < 43) {
                        if (!this.insertItem(itemStack2, 43, 58, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (!this.insertItem(itemStack2, 16, 43, false)) {
                        return ItemStack.EMPTY;
                    }
                }
            } else if (!this.insertItem(itemStack2, 16, 52, false)) {
                return ItemStack.EMPTY;
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

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.resultInv && super.canInsertIntoSlot(stack, slot);
    }
}
