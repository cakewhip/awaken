package com.kqp.awaken.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class AwakenOreBlock extends Block {
    private final int minXp, maxXp;

    public AwakenOreBlock(AbstractBlock.Settings settings, int minXp, int maxXp) {
        super(settings);

        this.minXp = minXp;
        this.maxXp = maxXp;
    }

    public AwakenOreBlock(AbstractBlock.Settings settings) {
        this(settings, 0, 0);
    }

    protected int getExperienceWhenMined(Random random) {
        return MathHelper.nextInt(random, minXp, maxXp);
    }

    @Override
    public void onStacksDropped(BlockState state, World world, BlockPos pos, ItemStack stack) {
        super.onStacksDropped(state, world, pos, stack);
        if (EnchantmentHelper.getLevel(Enchantments.SILK_TOUCH, stack) == 0) {
            int i = this.getExperienceWhenMined(world.random);
            if (i > 0) {
                this.dropExperience(world, pos, i);
            }
        }
    }
}
