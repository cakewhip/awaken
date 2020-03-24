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
    public final Item INGOT;
    public final Item PIECE;

    public OreGroup(String name, BlockStats bs, String pieceName, boolean ingot) {
        this.bs = bs;

        this.ORE = new Block(FabricBlockSettings.of(Material.STONE)
                .strength(bs.hardness, bs.resistance)
                .lightLevel(bs.lightLevel)
                .build()
        );
        Terminus.TBlocks.register(ORE, name + "_ore");

        if (pieceName != null) {
            this.PIECE = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
            Terminus.TItems.register(PIECE, name + "_" + pieceName);
        } else {
            this.PIECE = null;
        }

        if (ingot) {
            this.INGOT = new Item(new Item.Settings().group(ItemGroup.MATERIALS));
            Terminus.TItems.register(INGOT, name + "_ingot");
        } else {
            this.INGOT = null;
        }
    }
}
