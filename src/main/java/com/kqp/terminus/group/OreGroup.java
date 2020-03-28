package com.kqp.terminus.group;

import com.kqp.terminus.Terminus;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * Class to mass create blocks and items for a given type.
 */
public class OreGroup {
    public final BlockStats bs;
    public final Block ORE;
    public final Block BLOCK;
    public final Item INGOT;

    /**
     * Creates blocks and items using a given name.
     *
     * @param name      Name for blocks and items
     * @param bs        Properties for blocks
     * @param ingotName Name of the ingot, null to not make it
     */
    public OreGroup(String name, BlockStats bs, String ingotName) {
        this.bs = bs;

        this.ORE = new Block(FabricBlockSettings.of(Material.STONE)
                .strength(bs.hardness, bs.resistance)
                .lightLevel(bs.lightLevel)
                .build()
        );
        Terminus.TBlocks.register(ORE, name + "_ore");

        this.BLOCK = new Block(FabricBlockSettings.of(Material.STONE)
                .strength(bs.hardness, bs.resistance)
                .lightLevel(bs.lightLevel)
                .build()
        );
        Terminus.TBlocks.register(BLOCK, name + "_block");

        if (ingotName != null) {
            this.INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
            Terminus.TItems.register(INGOT, name + "_" + ingotName);
        } else {
            this.INGOT = null;
        }
    }
}
