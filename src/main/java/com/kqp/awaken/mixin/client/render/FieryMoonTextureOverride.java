package com.kqp.awaken.mixin.client.render;

import com.kqp.awaken.client.AwakenClientLevelData;
import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to replace and up-scale the moon texture during a fiery moon.
 */
@Mixin(WorldRenderer.class)
public abstract class FieryMoonTextureOverride {
    private static final Identifier NORMAL_MOON = new Identifier("textures/environment/moon_phases.png");
    private static final Identifier FIERY_MOON = Awaken.id("textures/environment/fiery_moon.png");

    @Redirect(method = "renderSky", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/render/WorldRenderer;MOON_PHASES:Lnet/minecraft/util/Identifier;"))
    private final Identifier fieryMoonRedirect() {
        AwakenLevelData awakenLevelData = AwakenClientLevelData.INSTANCE.getAwakenLevelData();

        if (awakenLevelData.isFieryMoonActive()) {
            return FIERY_MOON;
        } else {
            return NORMAL_MOON;
        }
    }

    @ModifyConstant(method = "renderSky", constant = @Constant(floatValue = 20.0F))
    private float scaleMoonSize(float size) {
        AwakenLevelData awakenLevelData = AwakenClientLevelData.INSTANCE.getAwakenLevelData();

        if (awakenLevelData.isFieryMoonActive()) {
            if (size == 20.0F) {
                return 60.0F;
            }
        }

        return size;
    }
}
