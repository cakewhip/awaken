package com.kqp.awaken.item.tool;

import com.kqp.awaken.loot.AwakenRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.*;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating swords because the super constructor is protected.
 */
public class AwakenSwordItem extends SwordItem {
    public AwakenRarity rarity = AwakenRarity.OKAY;

    public AwakenSwordItem(ToolMaterial material) {
        super(material, 3, -2.4F, new Item.Settings().group(ItemGroup.COMBAT));
    }

    public Item setRarity(AwakenRarity rarity) {
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
