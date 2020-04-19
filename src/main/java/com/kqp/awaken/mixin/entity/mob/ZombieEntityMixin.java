package com.kqp.awaken.mixin.entity.mob;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.util.EntityAttributeUtil;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.ZombieEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to give zombies better stats and equipment post-awakening.
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {
    private static EntityAttributeUtil.EntityAttributeModifierGroup AWAKENED_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("awakened", "zombie")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.75D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.75D);

    private static EntityAttributeUtil.EntityAttributeModifierGroup BLOOD_MOON_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("blood_moon", "zombie")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5D)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void addAwakenBuffs(CallbackInfo callbackInfo) {
        ZombieEntity zombie = (ZombieEntity) (Object) this;
        AwakenLevelData awakenLevelData = AwakenLevelData.getFor(zombie.world);

        if (awakenLevelData.isWorldAwakened()) {
            AWAKENED_MODS.apply(zombie, true);

            if (awakenLevelData.isBloodMoonActive()) {
                BLOOD_MOON_MODS.apply(zombie, true);
            }
        }
    }
}
