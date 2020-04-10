package com.kqp.awaken.group;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.tool.AwakenAxeItem;
import com.kqp.awaken.item.tool.AwakenPickaxeItem;
import com.kqp.awaken.item.tool.AwakenShovelItem;
import com.kqp.awaken.item.tool.AwakenSwordItem;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterial;

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

        AwakenItems.register(SWORD, name + "_sword");
        AwakenItems.register(SHOVEL, name + "_shovel");
        AwakenItems.register(PICKAXE, name + "_pickaxe");
        AwakenItems.register(AXE, name + "_axe");
    }
}
