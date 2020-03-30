package com.kqp.awaken.group;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.tool.AwakenShovelItem;
import com.kqp.awaken.item.tool.AwakenSwordItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

/**
 * Class to mass create a set of armor for a given material.
 */
public class ArmorGroup {
    public final Item HELMET;
    public final Item CHESTPLATE;
    public final Item LEGGINGS;
    public final Item BOOTS;

    public ArmorGroup(String name, ArmorMaterial material) {
        HELMET = new ArmorItem(material, EquipmentSlot.HEAD, (new Item.Settings()).group(ItemGroup.COMBAT));
        CHESTPLATE = new ArmorItem(material, EquipmentSlot.CHEST, (new Item.Settings()).group(ItemGroup.COMBAT));
        LEGGINGS = new ArmorItem(material, EquipmentSlot.LEGS, (new Item.Settings()).group(ItemGroup.COMBAT));
        BOOTS = new ArmorItem(material, EquipmentSlot.FEET, (new Item.Settings()).group(ItemGroup.COMBAT));

        Awaken.TItems.register(HELMET, name + "_helmet");
        Awaken.TItems.register(CHESTPLATE, name + "_chestplate");
        Awaken.TItems.register(LEGGINGS, name + "_leggings");
        Awaken.TItems.register(BOOTS, name + "_boots");
    }
}
