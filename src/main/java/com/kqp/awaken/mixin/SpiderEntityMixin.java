package com.kqp.awaken.mixin;

import com.kqp.awaken.Awaken;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.IWorld;
import net.minecraft.world.LocalDifficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to give spiders better stats and a higher chance to have a skeleton rider.
 */
@Mixin(SpiderEntity.class)
public abstract class SpiderEntityMixin {
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
            0.1D,
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
            SpiderEntity spider = (SpiderEntity) (Object) this;

            spider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(AWAKENED_HEALTH_MOD);
            spider.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(AWAKENED_DAMAGE_MOD);
            spider.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).addPersistentModifier(AWAKENED_SPEED_MOD);

            if (Awaken.worldProperties.isBloodMoonActive()) {
                spider.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).addPersistentModifier(BLOOD_MOON_HEALTH_MOD);
                spider.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).addPersistentModifier(BLOOD_MOON_DAMAGE_MOD);
            }
        }
    }

    @Inject(at = @At("RETURN"), method = "initialize")
    public void addSkeletonRider(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, @Nullable EntityData entityData, @Nullable CompoundTag entityTag, CallbackInfoReturnable callbackInfo) {
        if (Awaken.worldProperties.isBloodMoonActive()) {
            SpiderEntity spider = (SpiderEntity) (Object) this;

            if (spider.getPassengerList().size() == 0 && world.getRandom().nextFloat() < 0.5F) {

                SkeletonEntity skeletonEntity = EntityType.SKELETON.create(spider.world);

                skeletonEntity.refreshPositionAndAngles(spider.getX(), spider.getY(), spider.getZ(), spider.yaw, 0.0F);
                skeletonEntity.initialize(world, difficulty, spawnType, null, null);
                world.spawnEntity(skeletonEntity);
                skeletonEntity.startRiding(spider);
            }
        }
    }
}
