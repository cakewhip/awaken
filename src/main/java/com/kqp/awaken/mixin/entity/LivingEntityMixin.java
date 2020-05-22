package com.kqp.awaken.mixin.entity;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.entity.attribute.AwakenEntityAttributes;
import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.util.Broadcaster;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.TridentItem;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to:
 * Detect deaths for triggering the awakening.
 * Deny riptide status when using the Atlantean sword.
 * Give spiders poison effect post-awakening.
 * Apply the silky glove effect.
 * Apply the shock-wave shield effect.
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "onDeath", at = @At("HEAD"))
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity le = (LivingEntity) (Object) this;
        World world = le.world;

        if (!world.isClient) {
            if (source.getAttacker() instanceof PlayerEntity) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(world.getServer());
                MinecraftServer server = world.getServer();

                if (le instanceof CowEntity && !awakenLevelData.isPostDragon()) {
                    awakenLevelData.setPostDragon();
                    Broadcaster.broadcastMessage(server, "The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (le instanceof SheepEntity && !awakenLevelData.isPostWither()) {
                    awakenLevelData.setPostWither();
                    Broadcaster.broadcastMessage(server, "Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (le instanceof PigEntity && !awakenLevelData.isPostElderGuardian()) {
                    awakenLevelData.setPostElderGuardian();
                    Broadcaster.broadcastMessage(server, "A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                } else if (le instanceof ZombieEntity && !awakenLevelData.isPostRaid()) {
                    awakenLevelData.setPostRaid();
                    Broadcaster.broadcastMessage(server, "A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }

    @Inject(method = "isUsingRiptide", at = @At("HEAD"), cancellable = true)
    public void fixRiptideReturn(CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getMainHandStack().getItem() instanceof AtlanteanSabreItem) {
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void implementSpiderPoison(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValue()) {
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(((LivingEntity) (Object) this).world.getServer());

            if (awakenLevelData.isWorldAwakened()) {
                if (source.getAttacker() instanceof SpiderEntity) {
                    LivingEntity livingEntity = (LivingEntity) (Object) this;

                    if (livingEntity.getRandom().nextFloat() < 0.5F) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 3 * 20, 0));
                    }
                }
            }
        }
    }

    @Inject(method = "getAttributeValue", at = @At("HEAD"), cancellable = true)
    public void buffDamage(EntityAttribute entityAttribute, CallbackInfoReturnable<Double> callbackInfo) {
        if (((Object) this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (entityAttribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                double damage = player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.MELEE_DAMAGE);
                attribute.setBaseValue(damage);
                damage = attribute.getValue();
                attribute.setBaseValue(0D);

                ItemStack held = player.getMainHandStack();
                attribute = null;

                if (held.getItem() instanceof SwordItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.SWORD_DAMAGE);
                } else if (held.getItem() instanceof AxeItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.AXE_DAMAGE);
                } else if (held.getItem() instanceof TridentItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.TRIDENT_DAMAGE);
                }

                if (attribute != null) {
                    attribute.setBaseValue(damage);
                    damage = attribute.getValue();
                    attribute.setBaseValue(0D);
                }

                callbackInfo.setReturnValue(damage);
            }
        }
    }

    @Inject(method = "getMovementSpeed", at = @At("HEAD"), cancellable = true)
    private void overideFlyingMovementSpeed(float slipperiness, CallbackInfoReturnable<Float> callbackInfo) {
        LivingEntity entity = ((LivingEntity) (Object) this);

        if (this instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) entity;

            if (flightProperties.canFly() && !entity.isOnGround()) {
                callbackInfo.setReturnValue((float) (entity.getMovementSpeed() * 0.28663526131445843));
            }
        }
    }

    @Inject(method = "isClimbing", at = @At("HEAD"), cancellable = true)
    private void applySilkyGloveEffect(CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (!entity.isSpectator()) {
            if (entity.horizontalCollision) {
                if (TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.SILKY_GLOVE)) {
                    callbackInfo.setReturnValue(true);
                }
            }
        }
    }

    @Inject(method = "takeShieldHit", at = @At("HEAD"), cancellable = true)
    private void applyShockwaveShieldEffect(LivingEntity attacker, CallbackInfo callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (TrinketUtil.hasTrinket(entity, AwakenItems.Trinkets.SHOCKWAVE_SHIELD)) {
            if (entity.getRandom().nextFloat() < 0.75F) {
                attacker.takeKnockback(0.5F * 1.5F, entity.getX() - attacker.getX(), entity.getZ() - attacker.getZ());

                callbackInfo.cancel();
            }
        }
    }
}
