package com.kqp.awaken.item.sword;

import com.kqp.awaken.item.material.AwakenToolMaterial;
import com.kqp.awaken.loot.AwakenRarity;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating swords because the super constructor is protected.
 */
public class AwakenSwordItem extends SwordItem {
    public AwakenRarity rarity = AwakenRarity.OKAY;

    public AwakenSwordItem(AwakenToolMaterial material) {
        super(material, -1, material.getAttackSpeed() - 4, new Item.Settings().group(ItemGroup.COMBAT));
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
