package com.kqp.awaken.init;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.util.registry.Registry;

public class AwakenStatusEffects {
    public static final StatusEffect CONFUSION = new AwakenStatusEffect(StatusEffectType.HARMFUL, 0xFFFFFF);

    public static void init() {
        Awaken.info("Initializing status effects");

        Registry.register(Registry.STATUS_EFFECT, Awaken.id("confusion"), AwakenStatusEffects.CONFUSION);
    }

    public static class AwakenStatusEffect extends StatusEffect {
        public AwakenStatusEffect(StatusEffectType type, int color) {
            super(type, color);
        }
    }
}
