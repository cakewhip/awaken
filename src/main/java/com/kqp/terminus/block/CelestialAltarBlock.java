package com.kqp.terminus.block;

import com.kqp.terminus.recipe.RecipeType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class CelestialAltarBlock extends Block implements CraftingInterface {
    public CelestialAltarBlock() {
        super(FabricBlockSettings.of(Material.STONE)
                .strength(35.0F, 12.0F)
                .lightLevel(4)
                .build()
        );
    }

    @Override
    public String[] getRecipeTypes() {
        return new String[] { RecipeType.CELESTIAL_ALTAR };
    }
}
