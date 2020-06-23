package com.kqp.awaken.item.alchemy;

import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class PotionBagItem extends Item {
    public final int maxCapacity;

    public PotionBagItem(int durability, int maxCapacity) {
        super(new Item.Settings().group(ItemGroup.COMBAT).maxCount(1).maxDamage(durability));

        this.maxCapacity = maxCapacity;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        CompoundTag tag = itemStack.getOrCreateSubTag("PotionBag");
        int capacity = tag.getInt("Capacity");

        if (user.isSneaking()) {
            ItemStack storedPotion = tag.contains("StoredPotion") ? ItemStack.fromTag(tag.getCompound("StoredPotion")) : null;

            DefaultedList<ItemStack> inventory = user.inventory.main;
            for (int i = 0; capacity < maxCapacity && i < inventory.size(); i++) {
                ItemStack invStack = inventory.get(i);

                if (invStack.getItem() instanceof PotionItem) {
                    if (storedPotion == null) {
                        CompoundTag stackTag = new CompoundTag();
                        invStack.toTag(stackTag);

                        tag.put("StoredPotion", stackTag);
                        capacity++;
                    } else if (ItemStack.areEqual(storedPotion, invStack)) {
                        capacity++;
                    }
                }
            }

            tag.putInt("Capacity", capacity);
        } else if (capacity > 0) {
            ItemStack storedPotion = ItemStack.fromTag(tag.getCompound("StoredPotion"));

            if (storedPotion.getItem() instanceof ThrowablePotionItem) {
                PotionEntity potionEntity = new PotionEntity(world, user);

                potionEntity.setItem(storedPotion);
                potionEntity.setProperties(user, user.pitch, user.yaw, -20.0F, 0.5F, 1.0F);
                world.spawnEntity(potionEntity);

                capacity--;

                final int dropAmt = capacity;
                user.getItemCooldownManager().set(this, 20);
                itemStack.damage(1, user, (player) -> {
                    for (int i = 0; i < dropAmt; i++) {
                        player.dropStack(storedPotion.copy());
                    }
                });


                if (capacity == 0) {
                    tag.remove("StoredPotion");
                }

                tag.putInt("Capacity", capacity);

                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack itemStack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag tag = itemStack.getOrCreateSubTag("PotionBag");
        int capacity = tag.getInt("Capacity");

        if (capacity > 0) {
            ItemStack storedPotion = ItemStack.fromTag(tag.getCompound("StoredPotion"));

            tooltip.add(new LiteralText("Capacity: " + capacity));
            PotionUtil.buildTooltip(storedPotion, tooltip, 1.0F);
        }
    }
}
