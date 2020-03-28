package com.kqp.terminus.item.tool;

import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ToolMaterial;

/**
 * Class for creating axes because the super constructor is protected.
 */
public class TerminusAxeItem extends AxeItem {
    public TerminusAxeItem(ToolMaterial material) {
        super(material, 5.0F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS));
    }
}
