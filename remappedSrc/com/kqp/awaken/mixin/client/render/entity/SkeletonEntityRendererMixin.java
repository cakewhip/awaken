package com.kqp.awaken.mixin.client.render.entity;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to override the skeleton texture during fiery moons.
 */
@Mixin(SkeletonEntityRenderer.class)
public class SkeletonEntityRendererMixin {
    private static final Identifier FIERY_MOON_TEXTURE = new Identifier(Awaken.MOD_ID, "textures/entity/fiery_moon/skeleton.png");

    @Inject(method = "getTexture", at = @At("HEAD"), cancellable = true)
    private void overrideTextureForFieryMoon(AbstractSkeletonEntity abstractSkeletonEntity, CallbackInfoReturnable<Identifier> callbackInfo) {
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(MinecraftClient.getInstance().world);

        if (awakenLevelData.isFieryMoonActive()) {
            callbackInfo.setReturnValue(FIERY_MOON_TEXTURE);
        }
    }
}
