package com.kqp.awaken.block.group;

import com.kqp.awaken.init.AwakenBlocks;
import com.kqp.awaken.init.AwakenItems;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Class to mass create blocks for a given type.
 */
public class OreBlocks {
    public final BlockStats bs;
    public final Block ORE;
    public final Block BLOCK;

    /**
     * Creates blocks using a given name.
     *
     * @param name      Name for blocks and items
     * @param bs        Properties for blocks
     */
    public OreBlocks(String name, BlockStats bs, boolean hasBlock) {
        this.bs = bs;

        this.ORE = new Block(FabricBlockSettings.of(Material.STONE)
                .strength(bs.hardness, bs.resistance)
                .lightLevel(bs.lightLevel)
                .build()
        );
        AwakenBlocks.register(ORE, name + "_ore");

        if (hasBlock) {
            this.BLOCK = new Block(FabricBlockSettings.of(Material.STONE)
                    .strength(bs.hardness, bs.resistance)
                    .lightLevel(bs.lightLevel)
                    .build()
            );
            AwakenBlocks.register(BLOCK, name + "_block");
        } else {
            this.BLOCK = null;
        }
    }
}
