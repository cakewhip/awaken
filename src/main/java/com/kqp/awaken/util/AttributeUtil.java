package com.kqp.awaken.util;

import net.minecraft.entity.attribute.EntityAttributeInstance;

public class AttributeUtil {
    public static double applyAttribute(EntityAttributeInstance attributeInstance, double val) {
        attributeInstance.setBaseValue(val);
        return attributeInstance.getValue();
    }
}
