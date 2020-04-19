package com.kqp.awaken.init;

import com.kqp.awaken.block.AwakenAnvilBlock;
import com.kqp.awaken.block.CraftingBlock;
import com.kqp.awaken.block.group.BlockStats;
import com.kqp.awaken.block.group.OreBlocks;
import com.kqp.awaken.recipe.RecipeType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenBlocks {
    public static OreBlocks SALVIUM, VALERIUM;

    public static final Block ENDERIAN_HELL_FORGE = new CraftingBlock(
            FabricBlockSettings.of(Material.STONE).strength(35.0F, 12.0F).lightLevel(12).build(),
            RecipeType.ENDERIAN_HELL_FORGE
    );


    public static OreBlocks MOONSTONE, SUNSTONE;

    public static final Block CELESTIAL_ALTAR_BLOCK = new CraftingBlock(
            FabricBlockSettings.of(Material.STONE).strength(35.0F, 12.0F).lightLevel(4).build(),
            RecipeType.CELESTIAL_ALTAR,
            RecipeType.CRAFTING_TABLE
    );

    public static final Block CELESTIAL_STEEL_ANVIL_BLOCK = new AwakenAnvilBlock(
            FabricBlockSettings.of(Material.METAL).strength(35.0F, 24.0F).lightLevel(4).build(),
            RecipeType.CELESTIAL_STEEL_ANVIL,
            RecipeType.ANVIL
    );

    public static void init() {
        Awaken.info("Initializing blocks");

        // Phase 2
        {
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

        // Phase 3
        {
            SUNSTONE = new OreBlocks(
                    "sunstone",
                    new BlockStats(25.0F, 6.0F, 6),
                    true
            );

            MOONSTONE = new OreBlocks(
                    "moonstone",
                    new BlockStats(25.0F, 6.0F, 6),
                    true
            );

            register(CELESTIAL_ALTAR_BLOCK, "celestial_altar");
            register(CELESTIAL_STEEL_ANVIL_BLOCK, "celestial_steel_anvil");
        }
    }

    public static void register(Block block, String name) {
        Registry.register(Registry.BLOCK, new Identifier(Awaken.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));
    }
}
