package com.kqp.awaken.entity.effect;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectType;

public class AwakenStatusEffects {
    public static final StatusEffect CONFUSION = new AwakenStatusEffect(StatusEffectType.HARMFUL, 0xFFFFFF);

    public static class AwakenStatusEffect extends StatusEffect {
        public AwakenStatusEffect(StatusEffectType type, int color) {
            super(type, color);
        }
    }
}
