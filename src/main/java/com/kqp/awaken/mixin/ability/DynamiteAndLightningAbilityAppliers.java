package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Used to apply the effects of the stick of dynamite, lightning in a bottle, and electrifying dynamite.
 */
@Mixin(LivingEntity.class)
public class DynamiteAndLightningAbilityAppliers {
    @Inject(method = "damage", at = @At(value = "RETURN"))
    private void applyDynamiteAndLightningEffects(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (callbackInfo.getReturnValue()) {
            LivingEntity target = (LivingEntity) (Object) this;
            Entity entity = source.getAttacker();

            if (entity != null && !entity.world.isClient) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) entity;

                    boolean dynamite = AwakenAbilities.DYNAMITE_STICK.get(player).flag;
                    boolean lightning = AwakenAbilities.LIGHTNING_BOTTLE.get(player).flag;
                    boolean both = dynamite && lightning;

                    float explosionChance = both ? 0.10F : 0.06F;
                    float lightningChance = both ? 0.12F : 0.08F;

                    if (dynamite || both) {
                        if (player.getRandom().nextFloat() < explosionChance) {
                            player.world.createExplosion(
                                    player,
                                    source.setExplosive(),
                                    null,
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    1.5F + player.getRandom().nextFloat() * 1.5F,
                                    false,
                                    Explosion.DestructionType.NONE
                            );
                        }
                    }

                    if (lightning || both) {
                        if (player.getRandom().nextFloat() < lightningChance) {
                            LightningEntity lightningEntity = new LightningEntity(
                                    EntityType.LIGHTNING_BOLT,
                                    player.world
                            );

                            lightningEntity.refreshPositionAndAngles(
                                    target.getX(),
                                    target.getY(),
                                    target.getZ(),
                                    0F,
                                    0F
                            );

                            // Make cosmetic (no damage)
                            lightningEntity.method_29498(true);

                            player.world.spawnEntity(lightningEntity);

                            doLightningDamage(lightningEntity, player);
                        }
                    }
                }
            }
        }
    }

    /**
     * We don't want the channeller of the lightning being hurt by it's own attack.
     */
    private static void doLightningDamage(LightningEntity lightning, LivingEntity channeller) {
        List<Entity> list = lightning.world.getEntities(
                lightning,
                new Box(lightning.getX() - 3.0D, lightning.getY() - 3.0D, lightning.getZ() - 3.0D, lightning.getX() + 3.0D, lightning.getY() + 6.0D + 3.0D, lightning.getZ() + 3.0D),
                entity -> entity.isAlive() && entity != channeller
        );

        for (Entity entity : list) {
            entity.onStruckByLightning(lightning);
        }

        if (channeller != null && channeller instanceof ServerPlayerEntity) {
            Criteria.CHANNELED_LIGHTNING.trigger((ServerPlayerEntity) channeller, list);
        }
    }
}
