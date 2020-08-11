package com.kqp.awaken.init;

import com.kqp.awaken.block.AwakenOreBlock;
import com.kqp.awaken.block.GenericBlock;
import com.kqp.awaken.block.NullStoneBlock;
import com.kqp.tcrafting.api.TRecipeInterface;
import com.kqp.tcrafting.api.TRecipeInterfaceRegistry;
import com.kqp.tcrafting.api.TRecipeTypeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenRecipes {
    public static final TRecipeInterface ENDERIAN_HELL_FORGE_RECIPE_INTERF = TRecipeInterfaceRegistry.register(
            AwakenBlocks.ENDERIAN_HELL_FORGE,
            TRecipeTypeRegistry.SMELTING
    );

    public static void init() {
    }
}
