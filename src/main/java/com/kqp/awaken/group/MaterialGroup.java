package com.kqp.awaken.group;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.tool.AwakenShovelItem;
import com.kqp.awaken.item.tool.AwakenSwordItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

/**
 * Class to mass create a set of tools and armor for a given material.
 */
public class MaterialGroup {
    public final Item SWORD;
    public final Item SHOVEL;
    public final Item PICKAXE;
    public final Item AXE;

    public final Item HELMET;
    public final Item CHESTPLATE;
    public final Item LEGGINGS;
    public final Item BOOTS;

    public MaterialGroup(String name, ToolMaterial toolMaterial, ArmorMaterial armorMaterial) {
        SWORD = new AwakenSwordItem(toolMaterial);
        SHOVEL = new AwakenShovelItem(toolMaterial);
        PICKAXE = new AwakenPickaxeItem(toolMaterial);
        AXE = new AwakenAxeItem(toolMaterial);

        Awaken.TItems.register(SWORD, name + "_sword");
        Awaken.TItems.register(SHOVEL, name + "_shovel");
        Awaken.TItems.register(PICKAXE, name + "_pickaxe");
        Awaken.TItems.register(AXE, name + "_axe");

        HELMET = new ArmorItem(armorMaterial, EquipmentSlot.HEAD, (new Item.Settings()).group(ItemGroup.COMBAT));
        CHESTPLATE = new ArmorItem(armorMaterial, EquipmentSlot.CHEST, (new Item.Settings()).group(ItemGroup.COMBAT));
        LEGGINGS = new ArmorItem(armorMaterial, EquipmentSlot.LEGS, (new Item.Settings()).group(ItemGroup.COMBAT));
        BOOTS = new ArmorItem(armorMaterial, EquipmentSlot.FEET, (new Item.Settings()).group(ItemGroup.COMBAT));

        Awaken.TItems.register(HELMET, name + "_helmet");
        Awaken.TItems.register(CHESTPLATE, name + "_chestplate");
        Awaken.TItems.register(LEGGINGS, name + "_leggings");
        Awaken.TItems.register(BOOTS, name + "_boots");
    }
}
