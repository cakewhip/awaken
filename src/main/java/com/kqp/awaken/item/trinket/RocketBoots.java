package com.kqp.awaken.item.trinket;

import com.kqp.awaken.item.material.AwakenArmorMaterial;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;

/**
 * These are rocket boots.
 * With these stats, the player can go up 5 blocks!
 */
public class RocketBoots extends ArmorItem implements FlyingItem {
    public RocketBoots() {
        super(AwakenArmorMaterial.trinketMaterial("rocket_boots", 100), EquipmentSlot.FEET, new Item.Settings().maxCount(1).group(ItemGroup.COMBAT));
    }

    @Override
    public double getMaxFlySpeed() {
        return 0.3D;
    }

    @Override
    public double getFlySpeed() {
        return 0.1D;
    }

    @Override
    public int getMaxFlyTime() {
        return 20;
    }

    @Override
    public boolean canFloat() {
        return true;
    }
}
