package com.kqp.awaken.mixin.ability;

import com.kqp.awaken.init.AwakenAbilities;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

/**
 * Used to apply the netherian belt effect.
 */
@Mixin(ItemStack.class)
public class NetherianBeltAbilityApplier {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> callbackInfo) {
        if (player != null) {
            if (AwakenAbilities.NETHERIAN_BELT.get(player).flag) {
                ItemStack itemStack = (ItemStack) (Object) this;
                Item item = itemStack.getItem();

                boolean goldTool = item instanceof ToolItem && ((ToolItem) item).getMaterial() == ToolMaterials.GOLD;
                boolean goldArmor = item instanceof ArmorItem && ((ArmorItem) item).getMaterial() == ArmorMaterials.GOLD;

                if (goldTool || goldArmor) {
                    if (Math.random() < 0.16666666666666666D) {
                        callbackInfo.setReturnValue(false);
                    }
                }
            }
        }
    }
}
