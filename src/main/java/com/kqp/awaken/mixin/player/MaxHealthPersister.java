package com.kqp.awaken.mixin.player;

import com.kqp.awaken.item.health.HealthIncreaseItem;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to ensure max health items' effects persist through death.
 */
@Mixin(ServerPlayerEntity.class)
public class MaxHealthPersister {
    @Inject(method = "copyFrom", at = @At("RETURN"))
    private void copyFrom(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfo callbackInfo) {
        for (HealthIncreaseItem healthIncreaseItem : HealthIncreaseItem.HEALTH_INCREASE_ITEMS) {
            for (int i = 0; i < healthIncreaseItem.healthModifiers.size(); i++) {
                EntityAttributeModifier mod = healthIncreaseItem.healthModifiers.get(i);

                if (oldPlayer.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).hasModifier(mod)) {
                    ((ServerPlayerEntity) (Object) this).getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(mod);
                }
            }
        }
    }
}
