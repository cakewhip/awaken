package com.kqp.awaken.mixin;

import com.kqp.awaken.item.armor.AwakenArmorItem;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.entity.EquipmentSlot;
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
 * Used to use different armor textures for different head equipment.
 */
@Mixin(ArmorFeatureRenderer.class)
public class ArmorFeatureRendererMixin {
    @Shadow
    @Final
    protected static Map<String, Identifier> ARMOR_TEXTURE_CACHE;

    @Inject(method = "getArmorTexture", at = @At("HEAD"), cancellable = true)
    protected void useCustomTextureLayers(EquipmentSlot slot, ArmorItem item, boolean secondLayer, @Nullable String suffix, CallbackInfoReturnable<Identifier> callbackInfoReturnable) {
        if (item instanceof AwakenArmorItem) {
            String customTextureLayer = ((AwakenArmorItem) item).getCustomTextureLayer();

            if (customTextureLayer != null) {
                String string = "textures/models/armor/" + item.getMaterial().getName() + "_layer_" + (secondLayer ? 2 : 1) + "_" + customTextureLayer + ".png";

                callbackInfoReturnable.setReturnValue(ARMOR_TEXTURE_CACHE.computeIfAbsent(string, Identifier::new));
            }
        }
    }
}
