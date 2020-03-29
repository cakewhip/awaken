package com.kqp.awaken.block;

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

/**
 * Awaken's anvil block type.
 * Exists solely to implement {@link RecipeAccessProvider}. Sad.
 */
public class AwakenAnvilBlock extends AnvilBlock implements RecipeAccessProvider {
    private final String[] recipeTypes;

    /**
     * Creates a new Awaken anvil block.
     * Provides the passed recipes.
     *
     * @param settings    Settings for the anvil block
     * @param recipeTypes Valid recipe types.
     */
    public AwakenAnvilBlock(Settings settings, String... recipeTypes) {
        super(settings);
        this.recipeTypes = recipeTypes;
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
        return recipeTypes;
    }
}
