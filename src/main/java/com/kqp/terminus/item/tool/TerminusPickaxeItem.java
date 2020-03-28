package com.kqp.terminus.item.tool;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ToolMaterial;

/**
 * Class for creating pickaxes because the super constructor is protected.
 */
public class TerminusPickaxeItem extends PickaxeItem {
    public TerminusPickaxeItem(ToolMaterial material) {
        super(material, 1, -3.0F, new Settings().group(ItemGroup.TOOLS));
    }
}
