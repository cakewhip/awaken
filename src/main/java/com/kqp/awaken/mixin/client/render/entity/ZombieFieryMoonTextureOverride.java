package com.kqp.awaken.mixin.client.render.entity;

import com.kqp.awaken.client.AwakenClientLevelData;
import com.kqp.awaken.world.data.AwakenLevelData;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override the zombie texture during fiery moons.
 */
@Mixin(ZombieBaseEntityRenderer.class)
public class ZombieFieryMoonTextureOverride {
    private static final Identifier FIERY_MOON_TEXTURE = Awaken.id("textures/entity/fiery_moon/zombie.png");

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void overrideTextureForFieryMoon(ZombieEntity zombieEntity, CallbackInfoReturnable<Identifier> callbackInfo) {
        AwakenLevelData awakenLevelData = AwakenClientLevelData.INSTANCE.getAwakenLevelData();

        if (awakenLevelData.isFieryMoonActive()) {
            callbackInfo.setReturnValue(FIERY_MOON_TEXTURE);
        }
    }
}
