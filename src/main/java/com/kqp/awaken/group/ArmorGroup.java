package com.kqp.awaken.group;

import com.kqp.awaken.init.AwakenItems;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class to mass create a set of armor for a given material.
 */
public class ArmorGroup {
    public final Item HELMET;
    public final Item CHESTPLATE;
    public final Item LEGGINGS;
    public final Item BOOTS;

    public ArmorGroup(String name, ArmorMaterial material, String... tooltips) {
        HELMET = new ArmorItem(material, EquipmentSlot.HEAD, (new Item.Settings()).group(ItemGroup.COMBAT)) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                for (String str : tooltips) {
                    tooltip.add(new LiteralText(str));
                }
            }
        };

        CHESTPLATE = new ArmorItem(material, EquipmentSlot.CHEST, (new Item.Settings()).group(ItemGroup.COMBAT)) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                for (String str : tooltips) {
                    tooltip.add(new LiteralText(str));
                }
            }
        };

        LEGGINGS = new ArmorItem(material, EquipmentSlot.LEGS, (new Item.Settings()).group(ItemGroup.COMBAT)) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                for (String str : tooltips) {
                    tooltip.add(new LiteralText(str));
                }
            }
        };

        BOOTS = new ArmorItem(material, EquipmentSlot.FEET, (new Item.Settings()).group(ItemGroup.COMBAT)) {
            @Override
            public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
                for (String str : tooltips) {
                    tooltip.add(new LiteralText(str));
                }
            }
        };

        AwakenItems.register(HELMET, name + "_helmet");
        AwakenItems.register(CHESTPLATE, name + "_chestplate");
        AwakenItems.register(LEGGINGS, name + "_leggings");
        AwakenItems.register(BOOTS, name + "_boots");
    }
}
