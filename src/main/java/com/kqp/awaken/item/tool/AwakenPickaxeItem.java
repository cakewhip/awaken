package com.kqp.awaken.item.tool;

import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.loot.AwakenRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PickaxeItem;
import net.minecraft.text.BaseText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating pickaxes because the super constructor is protected.
 */
public class AwakenPickaxeItem extends PickaxeItem {
    public AwakenRarity rarity = AwakenRarity.OKAY;

    public AwakenPickaxeItem(AwakenToolMaterial material) {
        super(material, -1, material.getAttackSpeed() - 4, new Settings().group(ItemGroup.TOOLS));
    }

    public Item setRarity(AwakenRarity rarity) {
        this.rarity = rarity;

        return this;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        BaseText firstLine = (BaseText) tooltip.get(0);
        firstLine.setStyle(firstLine.getStyle().withColor(this.rarity.color));
    }
}
