package com.kqp.awaken.mixin.combat;

import com.kqp.awaken.effect.EntityEquipmentListener;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * Used to listen for equipping and un-equipping.
 */
@Mixin(LivingEntity.class)
public abstract class EquipmentListener {
    @Inject(
            method = "method_30129", // this is where they add/remove modifiers that items give the player
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/ItemStack;areEqual(Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void listenForEquips(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> callbackInfo,
                                 Map<EquipmentSlot, ItemStack> equipment,
                                 EquipmentSlot[] equipmentSlots,
                                 int numEquipment,
                                 int index,
                                 EquipmentSlot currentSlot,
                                 ItemStack prevItemStack,
                                 ItemStack currItemStack) {
        if (!ItemStack.areEqual(prevItemStack, currItemStack)) {
            LivingEntity living = (LivingEntity) (Object) this;
            Optional<ItemStack> equip = Optional.empty(), unEquip = Optional.empty();

            if (!currItemStack.isEmpty()) {
                equip = Optional.of(currItemStack);
            }

            if (!prevItemStack.isEmpty()) {
                unEquip = Optional.of(prevItemStack);
            }

            if (equip.isPresent() || unEquip.isPresent()) {
                Set<ItemStack> listeners = new HashSet();

                for (EquipmentSlot slot : equipmentSlots) {
                    ItemStack equippedStack = living.getEquippedStack(slot);

                    if (equippedStack.getItem() instanceof EntityEquipmentListener) {
                        listeners.add(equippedStack);
                    }
                }

                // The equipment found in the previous loop does not contain the previously equipped stack
                if (prevItemStack.getItem() instanceof EntityEquipmentListener) {
                    listeners.add(prevItemStack);
                }

                for (ItemStack itemStack : listeners) {
                    EntityEquipmentListener listener = (EntityEquipmentListener) itemStack.getItem();

                    equip.ifPresent(equippedStack -> listener.onEquip(living, itemStack, equippedStack, currentSlot));
                    unEquip.ifPresent(unEquippedStack -> listener.onUnEquip(living, itemStack, unEquippedStack, currentSlot));
                }
            }
        }
    }
}
