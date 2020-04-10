package com.kqp.awaken.mixin;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.EntityAttributeUtil;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
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
    private static EntityAttributeUtil.EntityAttributeModifierGroup AWAKENED_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("awakened", "spider")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.75D)
                    .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.1D)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.75D);

    private static EntityAttributeUtil.EntityAttributeModifierGroup BLOOD_MOON_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("blood_moon", "spider")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5D)
                    .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 0.5D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void addAwakenBuffs(CallbackInfo callbackInfo) {
        if (Awaken.worldProperties.isWorldAwakened()) {
            SpiderEntity spider = (SpiderEntity) (Object) this;

            AWAKENED_MODS.apply(spider, true);

            if (Awaken.worldProperties.isBloodMoonActive()) {
                BLOOD_MOON_MODS.apply(spider, true);
            }
        }
    }

    @Inject(method = "initialize", at = @At("RETURN"))
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
