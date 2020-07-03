package com.kqp.awaken.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

public class AwakenArmorItem extends ArmorItem {
    private String customTextureLayer = null;

    public AwakenArmorItem(ArmorMaterial material, EquipmentSlot slot) {
        super(material, slot, new Item.Settings().group(ItemGroup.COMBAT));
    }

    public AwakenArmorItem setCustomTextureLayer(String customTextureLayer) {
        this.customTextureLayer = customTextureLayer;

        return this;
    }

    public String getCustomTextureLayer() {
        return customTextureLayer;
    }
}
