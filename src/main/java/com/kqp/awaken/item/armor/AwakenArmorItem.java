package com.kqp.awaken.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

import java.util.Optional;

public class AwakenArmorItem extends ArmorItem {
    public final Optional<String> customTextureLayer;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot, String customTextureLayer) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));

        this.customTextureLayer = Optional.ofNullable(customTextureLayer);
    }
}
