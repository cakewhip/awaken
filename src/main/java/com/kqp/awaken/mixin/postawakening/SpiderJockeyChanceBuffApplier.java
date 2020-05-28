package com.kqp.awaken.mixin.postawakening;

import com.kqp.awaken.data.AwakenLevelData;
import jdk.internal.jline.internal.Nullable;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.mob.SpiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Used to give spiders a higher chance to have a skeleton rider post-awakening.
 */
@Mixin(SpiderEntity.class)
public abstract class SpiderJockeyChanceBuffApplier {
    @Inject(method = "initialize", at = @At("RETURN"))
    public void addSkeletonRider(WorldAccess world, LocalDifficulty difficulty, SpawnReason SpawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag, CallbackInfoReturnable callbackInfo) {
        SpiderEntity spider = (SpiderEntity) (Object) this;

        if (!spider.world.isClient) {
            AwakenLevelData awakenLevelData = AwakenLevelData.getFor(spider.world.getServer());
            if (awakenLevelData.isWorldAwakened()) {
                if (spider.getPassengerList().size() == 0 && world.getRandom().nextFloat() < 0.5F) {

                    SkeletonEntity skeletonEntity = EntityType.SKELETON.create(spider.world);

                    skeletonEntity.refreshPositionAndAngles(spider.getX(), spider.getY(), spider.getZ(), spider.yaw, 0.0F);
                    skeletonEntity.initialize(world, difficulty, SpawnReason, null, null);
                    world.spawnEntity(skeletonEntity);
                    skeletonEntity.startRiding(spider);
                }
            }
        }
    }
}
