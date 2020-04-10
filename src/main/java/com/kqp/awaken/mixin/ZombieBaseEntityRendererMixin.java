package com.kqp.awaken.mixin;

import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.entity.ZombieBaseEntityRenderer;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override the zombie texture during blood moons.
 */
@Mixin(ZombieBaseEntityRenderer.class)
public class ZombieBaseEntityRendererMixin {
    private static final Identifier BLOOD_MOON_TEXTURE = new Identifier(Awaken.MOD_ID, "textures/entity/blood_moon/zombie.png");

    @Inject(at = @At("HEAD"), method = "getTexture", cancellable = true)
    private void overrideTextureForBloodMoon(ZombieEntity zombieEntity, CallbackInfoReturnable<Identifier> callbackInfo) {
        if (Awaken.worldProperties.isBloodMoonActive()) {
            callbackInfo.setReturnValue(BLOOD_MOON_TEXTURE);
        }
    }
}
