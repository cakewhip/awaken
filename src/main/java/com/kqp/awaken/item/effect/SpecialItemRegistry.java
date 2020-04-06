package com.kqp.awaken.item.effect;

import com.kqp.awaken.group.ArmorGroup;
import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * Registry of items that do special things at special times.
 */
public class SpecialItemRegistry {
    public static final HashMap<Item, Equippable> EQUIPPABLE_ARMOR = new HashMap();
    public static final HashMap<Item, Equippable> EQUIPPABLE_ITEM = new HashMap();

    public static void addArmor(ArmorGroup armorGroup, Equippable equippable) {
        EQUIPPABLE_ARMOR.put(armorGroup.BOOTS, equippable);
        EQUIPPABLE_ARMOR.put(armorGroup.LEGGINGS, equippable);
        EQUIPPABLE_ARMOR.put(armorGroup.CHESTPLATE, equippable);
        EQUIPPABLE_ARMOR.put(armorGroup.HELMET, equippable);
    }
}
