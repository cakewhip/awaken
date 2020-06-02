package com.kqp.awaken.mixin.combat;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply critical melee hits for both main hit targets and sweeping targets.
 */
@Mixin(PlayerEntity.class)
public class MeleeCritChanceApplier {
    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyCritChanceForMainTarget(Entity target, DamageSource source, float damage) {
        return target.damage(source, applyCrit(target, 1.0F, damage, 1.75F, 0.25F));
    }

    @Redirect(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyCritChanceForSweepingEdgeTargets(LivingEntity target, DamageSource source, float damage) {
        return target.damage(source, applyCrit(target, 0.5F, damage, 1.33F, 0.33F));
    }

    private float applyCrit(Entity target, float chanceMult, float damage, float damageMult, float damageMultTail) {
        PlayerEntity player = (PlayerEntity) (Object) this;
        double critChance = player.getAttributeValue(AwakenEntityAttributes.CRIT_CHANCE);

        if (Math.random() < critChance * chanceMult) {
            damage = (float) ((damage * damageMult) + (Math.random() * damage * damageMultTail));

            onCrit(player, target);
        }

        return damage;
    }

    private static void onCrit(PlayerEntity player, Entity target) {
        player.world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, player.getSoundCategory(), 1.0F, 1.0F);
        player.addCritParticles(target);
    }
}
