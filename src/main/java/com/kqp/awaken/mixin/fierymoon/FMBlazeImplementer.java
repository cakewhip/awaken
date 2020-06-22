package com.kqp.awaken.mixin.fierymoon;

import com.kqp.awaken.effect.EntityFeatureGroup;
import com.kqp.awaken.mixin.accessor.MobEntityAccessor;
import com.kqp.awaken.world.data.AwakenLevelData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to buff the blaze during fiery moon.
 */
@Mixin(BlazeEntity.class)
public class FMBlazeImplementer {
    private static final EntityFeatureGroup BUFF = new EntityFeatureGroup()
            .setGroupName("zombified_piglin_fiery_moon_buffs")
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1D)
            .addEntityAttributeMultiplier(EntityAttributes.GENERIC_MAX_HEALTH, 1D)
            .addEntityAttributeAddition(EntityAttributes.GENERIC_ARMOR, 20D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void buffBlaze(EntityType<? extends BlazeEntity> entityType, World world, CallbackInfo callbackInfo) {
        if (!world.isClient && world.getDimensionRegistryKey() == DimensionType.OVERWORLD_REGISTRY_KEY) {
            AwakenLevelData levelData = AwakenLevelData.getFor(world.getServer());

            if (levelData.isFieryMoonActive()) {
                BlazeEntity blaze = (BlazeEntity) (Object) this;

                BUFF.applyToAndHeal(blaze);
            }
        }
    }

    @Inject(method = "initGoals", at = @At("RETURN"))
    private void targetVillagers(CallbackInfo callbackInfo) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();

        targetSelector.add(1, new FollowTargetGoal(
                (BlazeEntity) (Object) this,
                VillagerEntity.class,
                false
        ));
    }
}
