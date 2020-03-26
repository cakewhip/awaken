package com.kqp.terminus.block;

import net.minecraft.block.Block;

public class CraftingBlock extends Block implements CraftingInterface {
    private final String[] craftingTypes;

    public CraftingBlock(Settings settings, String... craftingTypes) {
        super(settings);
        this.craftingTypes = craftingTypes;
    }

    @Override
    public String[] getRecipeTypes() {
        return craftingTypes;
    }
}
