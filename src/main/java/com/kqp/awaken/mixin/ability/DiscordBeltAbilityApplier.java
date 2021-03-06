package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to implement the belt of discord effect.
 */
@Mixin(EnderPearlEntity.class)
public class DiscordBeltAbilityApplier {
    @Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyDiscordBeltEffect(Entity entity, DamageSource damageSource, float amt) {
        if (entity instanceof PlayerEntity) {
            if (AwakenAbilities.DISCORD_BELT.get((PlayerEntity) entity).flag) {
                amt *= 0.25;
            }
        }

        return entity.damage(damageSource, amt);
    }
}
