package com.kqp.awaken.entity.attribute;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;

/**
 * Custom entity attributes.
 */
public class AwakenEntityAttributes {
    public static final EntityAttribute RANGED_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.ranged_damage", 2.0D, 0.0D, 2048.0D);
    public static final EntityAttribute BOW_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.bow_damage", 2.0D, 0.0D, 2048.0D);
    public static final EntityAttribute CROSSBOW_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.crossbow_damage", 2.0D, 0.0D, 2048.0D);
    public static final EntityAttribute TRIDENT_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.trident_damage", 2.0D, 0.0D, 2048.0D);


    public static final EntityAttribute MELEE_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.melee_damage", 2.0D, 0.0D, 2048.0D);
    public static final EntityAttribute SWORD_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.sword_damage", 2.0D, 0.0D, 2048.0D);
    public static final EntityAttribute AXE_DAMAGE =
            new ClampedEntityAttribute("attribute.name.generic.axe_damage", 2.0D, 0.0D, 2048.0D);
}
