package com.kqp.awaken.item.trinket.wings;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class WingsItem extends Item {
    public WingsItem(int durability) {
        super(new Item.Settings().maxCount(1).group(ItemGroup.COMBAT).maxDamage(durability));
    }

    @Environment(EnvType.CLIENT)
    public Identifier getTexture() {
        Identifier id = Registry.ITEM.getId(this);

        return new Identifier(id.getNamespace(), "textures/entity/wings/" + id.getPath() + ".png");
    }
}
