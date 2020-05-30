package com.kqp.awaken.mixin.client.trinket;

import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EquipmentUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the snorkel mask effect, which is also used by the magma visor.
 */
@Mixin(BackgroundRenderer.class)
public class SnorkelMaskEffectApplier {
    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogDensity(F)V"))
    private static void applySnorkelMaskEffect(float density) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (AwakenAbilities.SNORKEL_MASK_EFFECT.get(player).flag) {
            // When in lava, it's 2.0F
            // When it's in neither lava nor water, it's 1.0F
            if (density == 1.0F || density == 2.0F) {
                density = 0.15F;
            } else {
                density *= 0.25F;
            }
        }

        RenderSystem.fogDensity(density);
    }
}
