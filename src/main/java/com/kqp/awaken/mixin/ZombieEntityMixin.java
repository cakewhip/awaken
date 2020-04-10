package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.entity.attribute.TEntityAttributes;
import com.kqp.awaken.util.MobDecorator;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to give zombies better stats and equipment post-awakening.
 */
@Mixin(ZombieEntity.class)
public abstract class ZombieEntityMixin {
    private static final EntityAttributeModifier AWAKENED_HEALTH_MOD = new EntityAttributeModifier(
            "awakened_health_mod",
            0.75D,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final EntityAttributeModifier AWAKENED_DAMAGE_MOD = new EntityAttributeModifier(
            "awakened_damage_mod",
            1.75D,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final EntityAttributeModifier AWAKENED_SPEED_MOD = new EntityAttributeModifier(
            "awakened_speed_mod",
            0.2D,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final EntityAttributeModifier BLOOD_MOON_HEALTH_MOD = new EntityAttributeModifier(
            "blood_moon_health_mod",
            0.5D,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    private static final EntityAttributeModifier BLOOD_MOON_DAMAGE_MOD = new EntityAttributeModifier(
            "blood_moon_damage_mod",
            0.5D,
            EntityAttributeModifier.Operation.MULTIPLY_TOTAL
    );

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void addAwakenBuffs(CallbackInfo callbackInfo) {
        if (Awaken.worldProperties.isWorldAwakened()) {
            ZombieEntity zombie = (ZombieEntity) (Object) this;

            zombie.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(AWAKENED_HEALTH_MOD);
            zombie.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(AWAKENED_DAMAGE_MOD);
            zombie.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(AWAKENED_SPEED_MOD);

            if (Awaken.worldProperties.isBloodMoonActive()) {
                zombie.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(BLOOD_MOON_HEALTH_MOD);
                zombie.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(BLOOD_MOON_DAMAGE_MOD);
            }
        }
    }
}
