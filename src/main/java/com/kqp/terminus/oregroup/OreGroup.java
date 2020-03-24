package com.kqp.terminus.oregroup;

import com.kqp.terminus.Terminus;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class OreGroup {
    public final BlockStats bs;
    public final Block ORE;
    public final Block BLOCK;
    public final Item INGOT;

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
