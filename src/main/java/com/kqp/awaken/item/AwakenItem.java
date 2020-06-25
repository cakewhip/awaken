package com.kqp.awaken.item;

import com.kqp.awaken.util.TooltipUtil;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class AwakenItem extends Item {
    public AwakenItem(ItemGroup itemGroup) {
        super(new Item.Settings().group(itemGroup));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        TooltipUtil.addIterableTooltips(tooltip, this.getTranslationKey(), Formatting.YELLOW);
    }
}
