package com.kqp.terminus.mixin;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.MobDecorator;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
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
    @Inject(at = @At("TAIL"), method = "initAttributes")
    protected void overrideAttributes(CallbackInfo callbackInfo) {
        if (Terminus.worldProperties.isWorldAwakened()) {
            SpiderEntity spider = (SpiderEntity) (Object) this;

            spider.getAttributeInstance(EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.28D);
            spider.getAttributeInstance(EntityAttributes.ARMOR).setBaseValue(6.0D);
            spider.getAttributeInstance(EntityAttributes.ATTACK_DAMAGE).setBaseValue(12.0D);
        }
    }

    @Inject(at = @At("RETURN"), method = "initialize")
    public void addSkeletonRider(IWorld world, LocalDifficulty difficulty, SpawnType spawnType, @Nullable EntityData entityData, @Nullable CompoundTag entityTag, CallbackInfoReturnable callbackInfo) {
        if (Terminus.worldProperties.isWorldAwakened()) {
            SpiderEntity spider = (SpiderEntity) (Object) this;

            if (spider.getPassengerList().size() == 0 && world.getRandom().nextFloat() < 0.75F) {

                SkeletonEntity skeletonEntity = (SkeletonEntity) EntityType.SKELETON.create(spider.world);

                skeletonEntity.refreshPositionAndAngles(spider.getX(), spider.getY(), spider.getZ(), spider.yaw, 0.0F);
                skeletonEntity.initialize(world, difficulty, spawnType, (EntityData) null, (CompoundTag) null);
                world.spawnEntity(skeletonEntity);
                skeletonEntity.startRiding(spider);
            }
        }
    }
}
