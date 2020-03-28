package com.kqp.terminus.item.tool;

import com.kqp.terminus.loot.TerminusRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating axes because the super constructor is protected.
 */
public class TerminusAxeItem extends AxeItem {
    public TerminusRarity rarity = TerminusRarity.OKAY;

    public TerminusAxeItem(ToolMaterial material) {
        super(material, 5.0F, -3.0F, new Item.Settings().group(ItemGroup.TOOLS));
    }

    public Item setRarity(TerminusRarity rarity) {
        this.rarity = rarity;

        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
        tooltip.get(0).getStyle().setColor(rarity.color);
    }
}
