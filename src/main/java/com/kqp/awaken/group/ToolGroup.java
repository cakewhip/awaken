package com.kqp.awaken.group;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.tool.AwakenShovelItem;
import com.kqp.awaken.item.tool.AwakenSwordItem;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;

/**
 * Class to mass create a set of tools for a given material.
 */
public class ToolGroup {
    public final Item SWORD;
    public final Item SHOVEL;
    public final Item PICKAXE;
    public final Item AXE;

    public ToolGroup(String name, ToolMaterial material) {
        SWORD = new AwakenSwordItem(material);
        SHOVEL = new AwakenShovelItem(material);
        PICKAXE = new AwakenPickaxeItem(material);
        AXE = new AwakenAxeItem(material);

        Awaken.TItems.register(SWORD, name + "_sword");
        Awaken.TItems.register(SHOVEL, name + "_shovel");
        Awaken.TItems.register(PICKAXE, name + "_pickaxe");
        Awaken.TItems.register(AXE, name + "_axe");
    }
}