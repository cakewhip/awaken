package com.kqp.awaken.mixin;

import com.kqp.awaken.init.Awaken;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.util.Identifier;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to replace the moon texture during a blood moon.
 */
@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    private static final Identifier NORMAL_MOON = new Identifier("textures/environment/moon_phases.png");
    private static final Identifier BLOOD_MOON = new Identifier(Awaken.MOD_ID, "textures/environment/blood_moon.png");

    @Redirect(method = "renderSky", at = @At(value = "FIELD", opcode = Opcodes.GETSTATIC, target = "Lnet/minecraft/client/render/WorldRenderer;MOON_PHASES:Lnet/minecraft/util/Identifier;"))
    private final Identifier bloodMoonRedirect() {
        if (Awaken.worldProperties.isBloodMoonActive()) {
            return BLOOD_MOON;
        } else {
            return NORMAL_MOON;
        }
    }
}
