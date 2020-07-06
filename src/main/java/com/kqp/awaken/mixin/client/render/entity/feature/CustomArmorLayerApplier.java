package com.kqp.awaken.mixin.client.render.entity.feature;

import com.kqp.awaken.item.armor.AwakenArmorItem;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Used to apply different armor layers.
 */
@Mixin(ArmorFeatureRenderer.class)
public class CustomArmorLayerApplier {
    @Shadow
    @Final
    protected static Map<String, Identifier> ARMOR_TEXTURE_CACHE;

    @Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
    protected void useCustomTextureLayers(ArmorItem item, boolean secondLayer, @Nullable String suffix, CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        if (item instanceof AwakenArmorItem) {
            ((AwakenArmorItem) item).customTextureLayer.ifPresent(customTextureLayer -> {
                String string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + (secondLayer ? 2 : 1) + "_" + customTextureLayer + ".png";

                callbackInfoReturnable.setReturnValue(ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new));
            });
        }
    }
}
