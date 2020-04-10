package com.kqp.awaken.mixin;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.item.sword.AtlanteanSabreItem;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.player.PlayerEntity;
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
 * Give spiders poison effect post-awakening
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(at = @At("HEAD"), method = "onDeath")
    public void onDeath(DamageSource source, CallbackInfo callbackInfo) {
        LivingEntity le = (LivingEntity) (Object) this;
        World world = le.world;

        if (!world.isClient) {
            Broadcaster broadcaster = new Broadcaster();

            if (source.getAttacker() instanceof PlayerEntity) {
                if (le instanceof CowEntity && !Awaken.worldProperties.isPostDragon()) {
                    Awaken.worldProperties.setPostDragon();
                    Broadcaster.broadcastMessage("The ground shakes beneath you...", Formatting.DARK_RED, false, true);
                } else if (le instanceof SheepEntity && !Awaken.worldProperties.isPostWither()) {
                    Awaken.worldProperties.setPostWither();
                    Broadcaster.broadcastMessage("Screams echo from below...", Formatting.DARK_RED, false, true);
                } else if (le instanceof PigEntity && !Awaken.worldProperties.isPostElderGuardian()) {
                    Awaken.worldProperties.setPostElderGuardian();
                    Broadcaster.broadcastMessage("A sharp chill goes down your spine...", Formatting.DARK_RED, false, true);
                } else if (le instanceof ZombieEntity && !Awaken.worldProperties.isPostRaid()) {
                    Awaken.worldProperties.setPostRaid();
                    Broadcaster.broadcastMessage("A distant figure fades into the shadows...", Formatting.DARK_RED, false, true);
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "isUsingRiptide", cancellable = true)
    public void fixRiptideReturn(CallbackInfoReturnable<Boolean> callbackInfo) {
        LivingEntity entity = (LivingEntity) (Object) this;

        if (entity.getMainHandStack().getItem() instanceof AtlanteanSabreItem) {
            callbackInfo.setReturnValue(false);
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    public void implementSpiderPoison(DamageSource source, float amount, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (callbackInfoReturnable.getReturnValue()) {
            if (Awaken.worldProperties.isWorldAwakened()) {
                if (source.getAttacker() instanceof SpiderEntity) {
                    LivingEntity livingEntity = (LivingEntity) (Object) this;

                    if (livingEntity.getRandom().nextFloat() < 0.5F) {
                        livingEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 6 * 20, 1));
                    }
                }
            }
        }
    }
}
