package com.kqp.awaken.item.effect;

import net.minecraft.item.Item;

import java.util.HashMap;

/**
 * Registry of items that do special things at special times.
 */
public class SpecialItemRegistry {
    public static final HashMap<Item, Equippable> EQUIPPABLE_ARMOR = new HashMap();
    public static final HashMap<Item, Equippable> EQUIPPABLE_ITEM = new HashMap();
}
