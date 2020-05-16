package com.kqp.awaken.item.trinket;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class AwakenTrinket extends Item {
    public AwakenTrinket(int durability) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(durability));
    }
}
