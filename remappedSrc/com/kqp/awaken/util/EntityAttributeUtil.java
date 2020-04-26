package com.kqp.awaken.util;

import com.google.common.collect.ArrayListMultimap;
import com.kqp.awaken.init.Awaken;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;

/**
 * Util class for entities.
 */
public class EntityAttributeUtil {
    /**
     * Used to easily create a group of attribute modifiers.
     */
    public static class EntityAttributeModifierGroup {
        private final String entityName;
        private final String modName;
        private final ArrayListMultimap<EntityAttribute, EntityAttributeModifier> modMap;

        public EntityAttributeModifierGroup(String entityName, String modName) {
            this.entityName = entityName;
            this.modName = modName;
            this.modMap = ArrayListMultimap.create();
        }

        public EntityAttributeModifierGroup add(EntityAttribute attribute, double modifier) {
            modMap.put(attribute, new EntityAttributeModifier(
                    Awaken.MOD_ID + "." + entityName + "." + "." + modName + "." + attribute.getTranslationKey(),
                    modifier,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL
            ));

            return this;
        }

        public void apply(LivingEntity entity, boolean resetHealth) {
            entity.getAttributes().addTemporaryModifiers(modMap);

            if (resetHealth) {
                entity.setHealth(entity.getMaximumHealth());
            }
        }
    }
}
