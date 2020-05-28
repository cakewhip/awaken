package com.kqp.awaken.mixin.combat.attribute;

import com.kqp.awaken.init.AwakenEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to apply melee entity attributes.
 */
@Mixin(LivingEntity.class)
public class MeleeEntityAttributesApplier {
    @Inject(method = "getAttributeValue", at = @At("HEAD"), cancellable = true)
    public void applyMeleeDamageAttributes(EntityAttribute entityAttribute, CallbackInfoReturnable<Double> callbackInfo) {
        if (((Object) this) instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) (Object) this;

            if (entityAttribute == EntityAttributes.GENERIC_ATTACK_DAMAGE) {
                double damage = player.getAttributes().getValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);

                EntityAttributeInstance attribute = player.getAttributeInstance(AwakenEntityAttributes.MELEE_DAMAGE);
                attribute.setBaseValue(damage);
                damage = attribute.getValue();
                attribute.setBaseValue(0D);

                ItemStack held = player.getMainHandStack();
                attribute = null;

                if (held.getItem() instanceof SwordItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.SWORD_DAMAGE);
                } else if (held.getItem() instanceof AxeItem) {
                    attribute = player.getAttributeInstance(AwakenEntityAttributes.AXE_DAMAGE);
                }

                if (attribute != null) {
                    attribute.setBaseValue(damage);
                    damage = attribute.getValue();
                    attribute.setBaseValue(0D);
                }

                callbackInfo.setReturnValue(damage);
            }
        }
    }
}
