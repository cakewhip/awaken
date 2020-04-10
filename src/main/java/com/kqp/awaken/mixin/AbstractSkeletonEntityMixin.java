package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.entity.attribute.TEntityAttributes;
import com.kqp.awaken.util.EntityAttributeUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to give skeletons better stats and armor post-awakening.
 */
@Mixin(AbstractSkeletonEntity.class)
public abstract class AbstractSkeletonEntityMixin {
    private static EntityAttributeUtil.EntityAttributeModifierGroup AWAKENED_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("awakened", "skeleton")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.75D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D)
                    .add(TEntityAttributes.RANGED_DAMAGE, 1.75D);

    private static EntityAttributeUtil.EntityAttributeModifierGroup BLOOD_MOON_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("blood_moon", "skeleton")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5D)
                    .add(TEntityAttributes.RANGED_DAMAGE, 0.5D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void addAwakenBuffs(CallbackInfo callbackInfo) {
        if (Awaken.worldProperties.isWorldAwakened() && ((Object) this) instanceof SkeletonEntity) {
            SkeletonEntity skeleton = (SkeletonEntity) (Object) this;

            AWAKENED_MODS.apply(skeleton, true);

            if (Awaken.worldProperties.isBloodMoonActive()) {
                BLOOD_MOON_MODS.apply(skeleton, true);
            }
        }
    }
}
