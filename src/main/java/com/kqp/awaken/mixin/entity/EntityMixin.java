package com.kqp.awaken.mixin.entity;

import com.kqp.awaken.entity.player.PlayerFlightProperties;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.item.trinket.flight.FlightTrinketItem;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to:
 * Apply the rocket boot effect.
 * Apply the scorched mask effect.
 */
@Mixin(Entity.class)
public class EntityMixin {
    private static final double FLOAT_SPEED = -0.07D;

    @Redirect(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    public void addRocketBootVelocity(Entity entity, Vec3d vel) {
        if (entity instanceof PlayerFlightProperties) {
            PlayerFlightProperties flightProperties = (PlayerFlightProperties) entity;

            if (entity.isAlive()) {
                boolean landGently = false;

                if (flightProperties.isFlying()) {
                    landGently = true;

                    FlightTrinketItem flyingItem = (FlightTrinketItem) flightProperties.getFlyingItemStack().getItem();
                    vel = vel.add(0D, flyingItem.getFlySpeed(), 0D);

                    vel = new Vec3d(vel.x, absMin(vel.y, flyingItem.getMaxFlySpeed()), vel.z);

                } else if (flightProperties.isFloating()) {
                    landGently = true;

                    if (vel.y < FLOAT_SPEED) {
                        vel = vel.add(0D, 0.2D, 0D);
                        vel = new Vec3d(vel.x, Math.min(vel.y, FLOAT_SPEED), vel.z);
                    }
                }

                if (landGently) {
                    if (vel.y > -0.4) {
                        entity.fallDistance = 0;
                    }
                }
            }
        }

        entity.setVelocity(vel);
    }

    @ModifyVariable(method = "setOnFireFor", name = "i", at = @At(value = "STORE", ordinal = 0))
    private int applyScorchedMaskEffect(int i) {
        if ((Object) this instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (TrinketUtil.hasTrinket(player, AwakenItems.Trinkets.SCORCHED_MASK)) {
                i *= 0.25;
            }
        }

        return i;
    }

    private static double absMin(double a, double b) {
        return Math.signum(a) * Math.min(Math.abs(a), Math.abs(b));
    }
}
