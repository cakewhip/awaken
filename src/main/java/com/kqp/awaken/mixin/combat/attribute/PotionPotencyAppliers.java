package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(StatusEffect.class)
public class PotionPotencyAppliers {
    @Redirect(
            method = "applyInstantEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z")
    )
    private boolean applyHarmfulToInstantHealthAndDamage(
            // Redirected method parameters
            LivingEntity target, DamageSource damageSource, float amount,
            // Injected method parameters
            @Nullable Entity source, @Nullable Entity attacker, LivingEntity _target, int amplifier, double proximity) {
        if (attacker != null && attacker instanceof PlayerEntity) {
            EntityAttributeInstance harmfulPotionPotency = ((PlayerEntity) attacker).getAttributeInstance(AwakenEntityAttributes.HARMFUL_POTION_POTENCY);

            harmfulPotionPotency.setBaseValue(amount);
            amount = (float) harmfulPotionPotency.getValue();
            harmfulPotionPotency.setBaseValue(0D);
        }

        return target.damage(damageSource, amount);
    }

    @Redirect(
            method = "applyInstantEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;heal(F)V")
    )
    private void applyBeneficialToInstantHealthAndDamage(
            // Redirected method parameters
            LivingEntity target, float amount,
            // Injected method parameters
            @Nullable Entity source, @Nullable Entity attacker, LivingEntity _target, int amplifier, double proximity) {
        if (attacker != null && attacker instanceof PlayerEntity) {
            EntityAttributeInstance beneficialPotionPotency = ((PlayerEntity) attacker).getAttributeInstance(AwakenEntityAttributes.BENEFICIAL_POTION_POTENCY);

            beneficialPotionPotency.setBaseValue(amount);
            target.heal((float) beneficialPotionPotency.getValue());
            beneficialPotionPotency.setBaseValue(0D);
        }
    }

    /**
     * Makes all the update-able status effects be affecated by the beneficial and harmful potion potency attributes.
     * This redirect is nasty because Mojang's code is nasty.
     * TODO: move these effect appliers to a registry where it can be expandable and not so hard coded
     *
     * @param target
     * @param amplifier
     * @param source
     * @param attacker
     * @param _target
     * @param _amplifier
     * @param proximity
     */
    @Redirect(
            method = "applyInstantEffect",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/effect/StatusEffect;applyUpdateEffect(Lnet/minecraft/entity/LivingEntity;I)V")
    )
    private void applyBeneficialAndHarmfulToUpdateEffects(
            // Redirected method parameters
            StatusEffect effect, LivingEntity target, int amplifier,
            // Injected method parameters
            @Nullable Entity source, @Nullable Entity attacker, LivingEntity _target, int _amplifier, double proximity) {
        EntityAttributeInstance beneficialPotionPotency = null;
        EntityAttributeInstance harmfulPotionPotency = null;

        if (attacker instanceof PlayerEntity) {
            beneficialPotionPotency = ((PlayerEntity) attacker).getAttributeInstance(AwakenEntityAttributes.BENEFICIAL_POTION_POTENCY);
            harmfulPotionPotency = ((PlayerEntity) attacker).getAttributeInstance(AwakenEntityAttributes.BENEFICIAL_POTION_POTENCY);
        }

        if (effect == StatusEffects.REGENERATION) {
            if (target.getHealth() < target.getMaxHealth()) {
                target.heal(applyAttribute(beneficialPotionPotency, 1.0F));
            }
        } else if (effect == StatusEffects.POISON) {
            if (target.getHealth() > 1.0F) {
                target.damage(DamageSource.MAGIC, applyAttribute(harmfulPotionPotency, 1.0F));
            }
        } else if (effect == StatusEffects.WITHER) {
            target.damage(DamageSource.WITHER, applyAttribute(harmfulPotionPotency, 1.0F));
        } else if (effect == StatusEffects.HUNGER && target instanceof PlayerEntity) {
            ((PlayerEntity) target).addExhaustion(applyAttribute(harmfulPotionPotency, 0.005F * (float) (amplifier + 1)));
        } else if (effect == StatusEffects.SATURATION && target instanceof PlayerEntity) {
            if (!target.world.isClient) {
                ((PlayerEntity) target).getHungerManager().add(amplifier + 1, applyAttribute(beneficialPotionPotency, 1.0F));
            }
        } else if ((effect != StatusEffects.INSTANT_HEALTH || target.isUndead()) && (effect != StatusEffects.INSTANT_DAMAGE || !target.isUndead())) {
            if (effect == StatusEffects.INSTANT_DAMAGE && !target.isUndead() || effect == StatusEffects.INSTANT_HEALTH && target.isUndead()) {
                target.damage(DamageSource.MAGIC, applyAttribute(harmfulPotionPotency, (float) (6 << amplifier)));
            }
        } else {
            target.heal(applyAttribute(beneficialPotionPotency, (float) Math.max(4 << amplifier, 0)));
        }
    }

    private static float applyAttribute(@Nullable EntityAttributeInstance attribute, float value) {
        if (attribute == null) {
            return value;
        }

        attribute.setBaseValue(value);
        return (float) attribute.getValue();
    }
}
