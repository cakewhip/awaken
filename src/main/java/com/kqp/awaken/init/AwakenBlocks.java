package com.kqp.awaken.init;

import com.kqp.awaken.block.AwakenOreBlock;
import com.kqp.awaken.block.CraftingBlock;
import com.kqp.awaken.block.NullStoneBlock;
import com.kqp.awaken.recipe.RecipeType;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AwakenBlocks {
    public static final Block CRACKED_BEDROCK = register("cracked_bedrock", new Block(stoneSettings(80F, 1200F, 4).build()));

    public static final Block ANCIENT_STONE = register("ancient_stone", new Block(stoneSettings(7.5F, 80F, 4).build()));
    public static final Block ANCIENT_COBBLESTONE = register("ancient_cobblestone", new Block(stoneSettings(5F, 60F, 2).build()));
    public static final Block NULL_STONE = register("null_stone", new NullStoneBlock(stoneSettings(160F, 1200F, 5).lightLevel(5).build()));

    public static final Block ANCIENT_COAL_ORE = register("ancient_coal_ore", new AwakenOreBlock(ancientOreSettings(10F).build(), 0, 2));
    public static final Block ANCIENT_IRON_ORE = register("ancient_iron_ore", new AwakenOreBlock(ancientOreSettings(10F).build()));
    public static final Block ANCIENT_GOLD_ORE = register("ancient_gold_ore", new AwakenOreBlock(ancientOreSettings(10F).build()));
    public static final Block ANCIENT_REDSTONE_ORE = register("ancient_redstone_ore", new RedstoneOreBlock(ancientOreSettings(10F).build()));
    public static final Block ANCIENT_LAPIS_ORE = register("ancient_lapis_ore", new AwakenOreBlock(ancientOreSettings(10F).build(), 2, 5));
    public static final Block ANCIENT_DIAMOND_ORE = register("ancient_diamond_ore", new AwakenOreBlock(ancientOreSettings(10F).build(), 3, 7));
    public static final Block ANCIENT_EMERALD_ORE = register("ancient_emerald_ore", new AwakenOreBlock(ancientOreSettings(10F).build(), 3, 7));
    public static final Block ANCIENT_SALVIUM_ORE = register("ancient_salvium_ore", new AwakenOreBlock(ancientOreSettings(15F).build()));
    public static final Block ANCIENT_VALERIUM_ORE = register("ancient_valerium_ore", new AwakenOreBlock(ancientOreSettings(15F).build()));

    // TODO: find out why vanilla pickaxes don't work for mining level
    // as a result of the issue mentioned above, salvium and valerium will be minable by any pickaxe
    public static final Block SALVIUM_ORE = register("salvium_ore", new AwakenOreBlock(stoneSettings(10F, 40F, 0).build()));
    public static final Block VALERIUM_ORE = register("valerium_ore", new AwakenOreBlock(stoneSettings(10F, 40F, 0).build()));


    public static final Block ENDERIAN_HELL_FORGE = register("enderian_hell_forge", new CraftingBlock(
            FabricBlockSettings.of(Material.STONE).strength(35F, 12F).lightLevel(12).build(),
            RecipeType.ENDERIAN_HELL_FORGE
    ));

    public static void init() {
    }

    public static Block register(String name, Block block) {
        Registry.register(Registry.BLOCK, new Identifier(Awaken.MOD_ID, name), block);
        Registry.register(Registry.ITEM, new Identifier(Awaken.MOD_ID, name), new BlockItem(block, new Item.Settings().group(ItemGroup.BUILDING_BLOCKS)));

        return block;
    }

    public static FabricBlockSettings stoneSettings(float hardness, float resistance, int miningLevel) {
        return FabricBlockSettings.of(Material.STONE).strength(hardness, resistance).breakByTool(FabricToolTags.PICKAXES, miningLevel);
    }

    public static FabricBlockSettings ancientOreSettings(float hardness) {
        return stoneSettings(hardness, 80F, 4);
    }
}
