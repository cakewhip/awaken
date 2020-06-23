package com.kqp.awaken.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom entity attributes.
 */
public class AwakenEntityAttributes {
    public static final Map<EntityAttribute, Double> NEW_ATTRIBUTES = new HashMap();

    public static final EntityAttribute DAMAGE_MITIGATION = register("damage_mitigation");

    public static final EntityAttribute CRIT_CHANCE = register("crit_chance", 0.04D);

    public static final EntityAttribute ARMOR_PENETRATION = register("armor_penetration");

    public static final EntityAttribute RANGED_DAMAGE = register("ranged_damage");
    public static final EntityAttribute BOW_DAMAGE = register("bow_damage");
    public static final EntityAttribute CROSSBOW_DAMAGE = register("crossbow_damage");

    public static final EntityAttribute MELEE_DAMAGE = register("melee_damage");
    public static final EntityAttribute SWORD_DAMAGE = register("sword_damage");
    public static final EntityAttribute AXE_DAMAGE = register("axe_damage");

    public static final EntityAttribute BENEFICIAL_POTION_POTENCY = register("beneficial_potion_potency");
    public static final EntityAttribute HARMFUL_POTION_POTENCY = register("harmful_potion_potency");
    public static final EntityAttribute POTION_THROW_STRENGTH = register("potion_throw_strength");

    public static final EntityAttribute UNARMED_DAMAGE = register("unarmed_damage");

    public static final EntityAttribute MOUNTED_DAMAGE = register("mounted_damage");

    public static final EntityAttribute CARNIVORE = register("carnivore");
    public static final EntityAttribute HERBIVORE = register("herbivore");
    public static final EntityAttribute OMNIVORE = register("omnivore");
    public static final EntityAttribute FOOD_SATURATION = register("food_saturation");

    public static final EntityAttribute AMMO_CONSUMPTION = register("ammo_consumption");

    public static final EntityAttribute SHIELD_KNOCKBACK = register("shield_knockback");

    public static void init() {
        Awaken.info("Initializing entity attributes");
    }

    private static EntityAttribute register(String name, double baseValue) {
        EntityAttribute attribute = new ClampedEntityAttribute("attribute.name.generic." + name, 2.0D, 0.0D, 2048.0D);
        Registry.register(Registry.ATTRIBUTE, new Identifier(Awaken.MOD_ID, name), attribute);
        NEW_ATTRIBUTES.put(attribute, baseValue);

        return attribute;
    }

    private static EntityAttribute register(String name) {
        return register(name, 0D);
    }
}
