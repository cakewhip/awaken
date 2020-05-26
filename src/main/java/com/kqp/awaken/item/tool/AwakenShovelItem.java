package com.kqp.awaken.item.tool;

import com.kqp.awaken.item.material.AwakenToolMaterial;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.List;

/**
 * Class for creating shovels because the super constructor is protected.
 */
public class AwakenShovelItem extends ShovelItem {
    public AwakenShovelItem(AwakenToolMaterial material) {
        super(material, -1, material.getAttackSpeed() - 4, new Settings().group(ItemGroup.TOOLS));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);
    }
}
