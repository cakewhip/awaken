package com.kqp.awaken.mixin.attribute;

import com.kqp.awaken.entity.attribute.ImmunityTimeProvider;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.util.AttributeUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Used to apply the immunity time attribute.
 */
@Mixin(LivingEntity.class)
public class ImmunityTimeAttributeApplier implements ImmunityTimeProvider {
    @ModifyConstant(
            method = "damage",
            constant = @Constant(intValue = 20)
    )
    private int applyImmunityTimeAttribute(int timeUntilRegen) {
        if ((Object) this instanceof PlayerEntity) {
            return this.getImmunityTime();
        }

        return timeUntilRegen;
    }

    @Override
    public int getImmunityTime() {
        EntityAttributeInstance immunityAttribute = ((PlayerEntity) (Object) this).getAttributeInstance(AwakenEntityAttributes.IMMUNITY_TIME);

        return (int) AttributeUtil.applyAttribute(immunityAttribute, 20);
    }
}
