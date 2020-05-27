package com.kqp.awaken.mixin.client.render;

import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.TrinketUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Used to apply the snorkel mask effect.
 */
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Redirect(method = "applyFog", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;fogDensity(F)V"))
    private static void applySnorkelMaskEffect(float density) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (TrinketUtil.hasAnyTrinkets(player, AwakenItems.Trinkets.SNORKEL_MASK, AwakenItems.Trinkets.MAGMA_VISOR)) {
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
