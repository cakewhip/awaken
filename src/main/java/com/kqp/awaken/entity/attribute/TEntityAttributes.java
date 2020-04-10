package com.kqp.awaken.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Custom entity attributes.
 */
public class TEntityAttributes {
    public static final EntityAttribute RANGED_DAMAGE =
            new ClampedEntityAttribute("generic.ranged_damage", 2.0D, 0.0D, 2048.0D);
}
