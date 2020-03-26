package com.kqp.terminus.block;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import net.minecraft.container.NameableContainerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TerminusAnvilBlock extends AnvilBlock implements CraftingInterface {
    private final String[] craftingTypes;

    public TerminusAnvilBlock(Settings settings, String... craftingTypes) {
        super(settings);
        this.craftingTypes = craftingTypes;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return ActionResult.PASS;
    }

    @Override
    @Nullable
    public NameableContainerFactory createContainerFactory(BlockState state, World world, BlockPos pos) {
        return null;
    }

    @Override
    public String[] getRecipeTypes() {
        return craftingTypes;
    }
}
