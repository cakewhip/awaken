package com.kqp.terminus.group;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.item.TerminusArmorMaterial;
import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusAxeItem;
import com.kqp.terminus.item.tool.TerminusPickaxeItem;
import com.kqp.terminus.item.tool.TerminusShovelItem;
import com.kqp.terminus.item.tool.TerminusSwordItem;
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
        SWORD = new TerminusSwordItem(toolMaterial);
        SHOVEL = new TerminusShovelItem(toolMaterial);
        PICKAXE = new TerminusPickaxeItem(toolMaterial);
        AXE = new TerminusAxeItem(toolMaterial);

        Terminus.TItems.register(SWORD, name + "_sword");
        Terminus.TItems.register(SHOVEL, name + "_shovel");
        Terminus.TItems.register(PICKAXE, name + "_pickaxe");
        Terminus.TItems.register(AXE, name + "_axe");

        HELMET = new ArmorItem(armorMaterial, EquipmentSlot.HEAD, (new Item.Settings()).group(ItemGroup.COMBAT));
        CHESTPLATE = new ArmorItem(armorMaterial, EquipmentSlot.CHEST, (new Item.Settings()).group(ItemGroup.COMBAT));
        LEGGINGS = new ArmorItem(armorMaterial, EquipmentSlot.LEGS, (new Item.Settings()).group(ItemGroup.COMBAT));
        BOOTS = new ArmorItem(armorMaterial, EquipmentSlot.FEET, (new Item.Settings()).group(ItemGroup.COMBAT));

        Terminus.TItems.register(HELMET, name + "_helmet");
        Terminus.TItems.register(CHESTPLATE, name + "_chestplate");
        Terminus.TItems.register(LEGGINGS, name + "_leggings");
        Terminus.TItems.register(BOOTS, name + "_boots");
    }
}
