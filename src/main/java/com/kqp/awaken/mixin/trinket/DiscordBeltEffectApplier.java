package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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
public class DiscordBeltEffectApplier {
    @Redirect(method = "onCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"))
    private boolean applyDiscordBeltEffect(Entity entity, DamageSource damageSource, float amt) {
        if (entity instanceof PlayerEntity) {
            if (TrinketUtil.hasTrinket((PlayerEntity) entity, AwakenItems.Trinkets.DISCORD_BELT)) {
                amt *= 0.25;
            }
        }

        return entity.damage(damageSource, amt);
    }
}
