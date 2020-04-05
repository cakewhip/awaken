package com.kqp.awaken.mixin;

import com.kqp.awaken.entity.attribute.TEntityAttributes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to apply ranged damage buffs.
 */
@Mixin(ArrowEntity.class)
public class ArrowEntityMixin {
    @Inject(method = "<init>*", at = @At("RETURN"))
    public void overrideArrowDamage(World world, LivingEntity owner, CallbackInfo callbackInfo) {
        EntityAttributeInstance attribute = owner.getAttributeInstance(TEntityAttributes.RANGED_DAMAGE);

        if (attribute != null) {
            ArrowEntity arrow = (ArrowEntity) (Object) this;
            arrow.setDamage(attribute.getValue());
        }
    }
}
