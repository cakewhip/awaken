package com.kqp.awaken.mixin.effect;

import com.kqp.awaken.item.armor.AwakenArmorItem;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(ItemStack.class)
public class ArmorEFGTooltipAdder {
    @Inject(
            method = "getTooltip",
            at = @At("RETURN"),
            cancellable = true
    )
    public void addArmorEFGTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> callbackInfo) {
        ItemStack itemStack = (ItemStack) (Object) this;

        if (itemStack.getItem() instanceof AwakenArmorItem) {
            ((AwakenArmorItem) itemStack.getItem()).itemMods.ifPresent(efg -> {
                List<Text> efgTooltips = new ArrayList();
                efg.populateTooltips(efgTooltips);

                List<Text> tooltip = callbackInfo.getReturnValue();

                for (int i = 0; i < tooltip.size(); i++) {
                    Text text = tooltip.get(i);

                    if (text instanceof TranslatableText) {
                        TranslatableText translatable = (TranslatableText) text;

                        if (translatable.getKey().startsWith("item.modifiers.")) {
                            int insertionPoint = i + 1;

                            for (Text efgText : efgTooltips) {
                                tooltip.add(insertionPoint, efgText);
                            }
                        }
                    }
                }
            });
        }
    }
}
