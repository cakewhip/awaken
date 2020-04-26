package com.kqp.awaken.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Random;

/**
 * Utility class for giving mobs equipment.
 */
public class MobDecorator {
    public static void giveArmor(HostileEntity entity) {
        Random r = entity.getRandom();

        if (r.nextFloat() < 0.5F) {
            if (r.nextFloat() < 0.75F)
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
            if (r.nextFloat() < 0.9F)
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
            if (r.nextFloat() < 0.75F)
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
            if (r.nextFloat() < 0.5F)
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));
        } else {
            if (r.nextFloat() < 0.75F)
                entity.equipStack(EquipmentSlot.HEAD, new ItemStack(Items.IRON_HELMET));
            if (r.nextFloat() < 0.9F)
                entity.equipStack(EquipmentSlot.CHEST, new ItemStack(Items.IRON_CHESTPLATE));
            if (r.nextFloat() < 0.75F)
                entity.equipStack(EquipmentSlot.LEGS, new ItemStack(Items.IRON_LEGGINGS));
            if (r.nextFloat() < 0.5F)
                entity.equipStack(EquipmentSlot.FEET, new ItemStack(Items.IRON_BOOTS));
        }

        if (r.nextFloat() < 0.75F && !entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.HEAD), 25, false);
        if (r.nextFloat() < 0.9F && !entity.getEquippedStack(EquipmentSlot.CHEST).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.CHEST), 25, false);
        if (r.nextFloat() < 0.75F && !entity.getEquippedStack(EquipmentSlot.LEGS).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.LEGS), 25, false);
        if (r.nextFloat() < 0.5F && !entity.getEquippedStack(EquipmentSlot.FEET).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.FEET), 25, false);
    }

    public static void giveSword(HostileEntity entity) {
        Random r = entity.getRandom();

        if (r.nextFloat() < 0.75F) {
            entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        } else {
            entity.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
        }

        if (r.nextFloat() < 0.95F && !entity.getEquippedStack(EquipmentSlot.MAINHAND).isEmpty())
            EnchantmentHelper.enchant(r, entity.getEquippedStack(EquipmentSlot.MAINHAND), 30, false);
    }
}
