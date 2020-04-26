package com.kqp.awaken.block;

import net.minecraft.block.Block;

/**
 * Generic block type to provide a crafting interface.
 * See {@link RecipeAccessProvider} for more information.
 */
public class CraftingBlock extends Block implements RecipeAccessProvider {
    /**
     * The recipe types to show the player within proximity.
     */
    private final String[] recipeTypes;

    public CraftingBlock(Settings settings, String... recipeTypes) {
        super(settings);
        this.recipeTypes = recipeTypes;
    }

    @Override
    public String[] getRecipeTypes() {
        return recipeTypes;
    }
}
