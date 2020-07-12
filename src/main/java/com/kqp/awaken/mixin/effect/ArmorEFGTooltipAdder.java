package com.kqp.awaken.mixin.effect;

import com.kqp.awaken.item.armor.ArmorEFGMutator;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
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

        if (itemStack.getItem() instanceof ArmorItem) {
            List<Text> tooltip = callbackInfo.getReturnValue();

            ArmorEFGMutator efgMutator = (ArmorEFGMutator) itemStack.getItem();
            int modTextIndex = -1;

            for (int i = 0; i < tooltip.size(); i++) {
                Text text = tooltip.get(i);

                if (text instanceof TranslatableText) {
                    TranslatableText translatable = (TranslatableText) text;

                    if (translatable.getKey().startsWith("item.modifiers.")) {
                        modTextIndex = i;
                        break;
                    }
                }
            }

            if (modTextIndex == -1) {
                // TODO: figure out what to do when the armor doesn't have a modifier tooltip
                return;
            }

            // Get insertion point for normal bonuses
            final int itemModIndex = modTextIndex + 1;
            efgMutator.getItemMods().ifPresent(efg -> {
                List<Text> efgTooltips = new ArrayList();
                efg.populateTooltips(efgTooltips);

                for (Text efgText : efgTooltips) {
                    tooltip.add(itemModIndex, efgText);
                }
            });

            // Get insertion point for set bonuses
            final int setModIndex = modTextIndex - 1;
            efgMutator.getSetMods().ifPresent(effects -> {
                List<Text> efgTooltips = new ArrayList();
                effects.populateTooltips(efgTooltips);

                for (Text efgText : efgTooltips) {
                    tooltip.add(setModIndex, efgText);
                }

                tooltip.add(setModIndex, new TranslatableText("item.set_bonus").formatted(Formatting.GRAY));
                tooltip.add(setModIndex, new LiteralText(""));
            });
        }
    }
}
