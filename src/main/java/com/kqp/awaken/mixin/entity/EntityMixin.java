package com.kqp.awaken.mixin.entity;

import com.kqp.awaken.entity.player.PlayerFlyingInfo;
import com.kqp.awaken.item.trinket.FlyingItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Entity.class)
public class EntityMixin {
    @Redirect(method = "updateVelocity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    public void addRocketBootVelocity(Entity entity, Vec3d vel) {
        if (entity instanceof PlayerFlyingInfo) {
            PlayerFlyingInfo flyingStatus = (PlayerFlyingInfo) entity;

            if (flyingStatus.isFlying()) {
                FlyingItem flyingItem = flyingStatus.getFlyingItem();
                vel = vel.add(0D, flyingItem.getFlySpeed(), 0D);

                if (vel.y > -0.4) {
                    entity.fallDistance = 0;
                }

                vel = new Vec3d(vel.x, absMin(vel.y, flyingItem.getMaxFlySpeed()), vel.z);
            }
        }

        entity.setVelocity(vel);
    }

    private static double absMin(double a, double b) {
        return Math.signum(a) * Math.min(Math.abs(a), Math.abs(b));
    }
}
