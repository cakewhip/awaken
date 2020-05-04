package com.kqp.awaken.init;

import com.kqp.awaken.block.CraftingBlock;
import com.kqp.awaken.block.NullStoneBlock;
import com.kqp.awaken.block.group.BlockStats;
import com.kqp.awaken.block.group.OreBlocks;
import com.kqp.awaken.recipe.RecipeType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenBlocks {
    public static final Block CRACKED_BEDROCK = new Block(FabricBlockSettings.of(Material.STONE).strength(100.0F, 1200.0F).breakByTool(FabricToolTags.PICKAXES, 4).build());
    public static final Block ANCIENT_STONE = new Block(FabricBlockSettings.of(Material.STONE).strength(40.0F, 40.0F).breakByTool(FabricToolTags.PICKAXES, 4).build());
    public static final Block ANCIENT_COBBLESTONE = new Block(FabricBlockSettings.of(Material.STONE).strength(40.0F, 40.0F).breakByTool(FabricToolTags.PICKAXES, 3).build());
    public static final Block NULL_STONE = new NullStoneBlock();

    public static OreBlocks SALVIUM, VALERIUM;

    public static final Block ENDERIAN_HELL_FORGE = new CraftingBlock(
            FabricBlockSettings.of(Material.STONE).strength(35.0F, 12.0F).lightLevel(12).build(),
            RecipeType.ENDERIAN_HELL_FORGE
    );

    public static void init() {
        Awaken.info("Initializing blocks");

        // Phase 2
        {
            register(CRACKED_BEDROCK, "cracked_bedrock");
            register(ANCIENT_STONE, "ancient_stone");
            register(ANCIENT_COBBLESTONE, "ancient_cobblestone");
            register(NULL_STONE, "null_stone");

            SALVIUM = new OreBlocks(
                    "salvium",
                    new BlockStats(25.0F, 6.0F, 0),
                    false
            );

            VALERIUM = new OreBlocks(
                    "valerium",
                    new BlockStats(25.0F, 6.0F, 0),
                    false
            );

            register(ENDERIAN_HELL_FORGE, "enderian_hell_forge");
        }
    }

    public static void register(Block block, String name) {
        Registry.register(Registry.BLOCK, new Identifier(Awaken.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
    }
}
