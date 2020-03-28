package com.kqp.terminus.item.shovel;

import com.kqp.terminus.item.TerminusToolMaterial;
import com.kqp.terminus.item.tool.TerminusShovelItem;
import jdk.internal.jline.internal.Nullable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArchaeologistSpadeItem extends TerminusShovelItem {
    public static final Set<Block> SAND_BLOCKS = new HashSet(Arrays.asList(
            Blocks.SAND,
            Blocks.RED_SAND,
            Blocks.SOUL_SAND
    ));

    public ArchaeologistSpadeItem() {
        super(TerminusToolMaterial.PHASE_0_SPECIAL);
    }


    @Override
    public float getMiningSpeed(ItemStack stack, BlockState state) {
        return SAND_BLOCKS.contains(state.getBlock()) ? this.miningSpeed * 4.0F : super.getMiningSpeed(stack, state);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        tooltip.add(new LiteralText("400% faster when digging sand"));
    }
}
