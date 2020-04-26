package com.kqp.awaken.item.armor;

import net.minecraft.entity.attribute.EntityAttribute;

public class ArmorSetBonus {
    private final String setName;
    private final EntityAttribute attribute;
    private final float amount;

    public ArmorSetBonus(String setName, EntityAttribute attribute, float amount) {
        this.setName = setName;
        this.attribute = attribute;
        this.amount = amount;
    }

    public String getSetName() {
        return setName;
    }

    public EntityAttribute getAttribute() {
        return attribute;
    }

    public float getAmount() {
        return amount;
    }
}
