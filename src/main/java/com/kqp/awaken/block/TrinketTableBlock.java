package com.kqp.awaken.block;

import com.kqp.awaken.recipe.RecipeType;
import com.kqp.awaken.util.TooltipUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.BlockView;

import java.util.List;

public class TrinketTableBlock extends Block implements RecipeAccessProvider {
    public TrinketTableBlock() {
        super(FabricBlockSettings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(2.5F).breakByTool(FabricToolTags.AXES));
    }

    @Override
    public String[] getRecipeTypes() {
        return new String[] { RecipeType.TRINKET_TABLE, RecipeType.CRAFTING_TABLE };
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void buildTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
        TooltipUtil.addIterableTooltips(tooltip, this.getTranslationKey(), Formatting.YELLOW);
    }
}
