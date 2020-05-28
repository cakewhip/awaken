package com.kqp.awaken.mixin.client.render;

import com.kqp.awaken.client.AwakenClientLevelData;
import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to replace the moon texture during a fiery moon.
 */
@Mixin(WorldRenderer.class)
public abstract class FieryMoonTextureOverride {
    private static final Identifier NORMAL_MOON = new Identifier("textures/environment/moon_phases.png");
    private static final Identifier FIERY_MOON = new Identifier(Awaken.MOD_ID, "textures/environment/fiery_moon.png");

    @Redirect(method = "renderSky", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/render/WorldRenderer;MOON_PHASES:Lnet/minecraft/util/Identifier;"))
    private final Identifier fieryMoonRedirect() {
        AwakenLevelData awakenLevelData = AwakenClientLevelData.INSTANCE.getAwakenLevelData();

        if (awakenLevelData.isFieryMoonActive()) {
            return FIERY_MOON;
        } else {
            return NORMAL_MOON;
        }
    }
}
