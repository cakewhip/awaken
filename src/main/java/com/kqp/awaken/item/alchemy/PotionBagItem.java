package com.kqp.awaken.item.alchemy;

import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
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

        if (!world.isClient) {
            CompoundTag tag = itemStack.getOrCreateSubTag("PotionBag");
            int capacity = tag.getInt("Capacity");

            if (capacity > 0) {
                ItemStack storedPotion = ItemStack.fromTag(tag.getCompound("StoredPotion"));

                if (storedPotion.getItem() instanceof ThrowablePotionItem) {
                    throwPotion(world, user, storedPotion);
                    user.getItemCooldownManager().set(this, 20);
                } else {
                    drinkPotion(world, user, storedPotion);
                    user.getItemCooldownManager().set(this, 200);
                }

                if (!user.abilities.creativeMode) {
                    capacity--;

                    final int dropAmt = capacity;
                    itemStack.damage(1, user, (player) -> {
                        for (int i = 0; i < dropAmt; i++) {
                            player.dropStack(storedPotion.copy());
                        }
                    });

                    tag.putInt("Capacity", capacity);

                    if (capacity == 0) {
                        tag.remove("StoredPotion");
                    }

                    user.inventory.offerOrDrop(world, new ItemStack(Items.GLASS_BOTTLE));
                }

                return TypedActionResult.success(itemStack);
            }
        }

        return TypedActionResult.pass(itemStack);
    }

    private void throwPotion(World world, PlayerEntity user, ItemStack potionStack) {
        EntityAttributeInstance potionThrowStrength = user.getAttributeInstance(AwakenEntityAttributes.POTION_THROW_STRENGTH);

        PotionEntity potionEntity = new PotionEntity(world, user);
        potionEntity.setItem(potionStack);
        potionEntity.setProperties(user, user.pitch, user.yaw, 0.0F, (float) AttributeUtil.applyAttribute(potionThrowStrength, 1F), 1.0F);
        world.spawnEntity(potionEntity);
    }

    private void drinkPotion(World world, PlayerEntity user, ItemStack potionStack) {
        List<StatusEffectInstance> statusEffects = PotionUtil.getPotionEffects(potionStack);

        statusEffects.forEach(statusEffect -> {
            if (statusEffect.getEffectType().isInstant()) {
                statusEffect.getEffectType().applyInstantEffect(user, user, user, statusEffect.getAmplifier(), 1.0D);
            } else {
                user.addStatusEffect(new StatusEffectInstance(statusEffect));
            }
        });
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
