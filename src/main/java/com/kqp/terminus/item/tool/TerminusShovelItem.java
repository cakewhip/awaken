package com.kqp.terminus.item.tool;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.ToolMaterial;

/**
 * Class for creating shovels because the super constructor is protected.
 */
public class TerminusShovelItem extends ShovelItem {
    public TerminusShovelItem(ToolMaterial material) {
        super(material, 1.5F, -3.0F, new Settings().group(ItemGroup.TOOLS));
    }
}
