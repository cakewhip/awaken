package com.kqp.awaken.mixin.entity.mob;

import com.kqp.awaken.data.AwakenLevelData;
import com.kqp.awaken.entity.ai.BoneCrownTargetGoal;
import com.kqp.awaken.init.AwakenEntityAttributes;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.util.EntityAttributeUtil;
import com.kqp.awaken.util.TrinketUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.mob.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
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
                    .add(AwakenEntityAttributes.RANGED_DAMAGE, 1.75D);

    private static EntityAttributeUtil.EntityAttributeModifierGroup FIERY_MOON_MODS =
            new EntityAttributeUtil.EntityAttributeModifierGroup("fiery_moon", "skeleton")
                    .add(EntityAttributes.GENERIC_MAX_HEALTH, 0.5D)
                    .add(AwakenEntityAttributes.RANGED_DAMAGE, 0.5D);

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void addAwakenBuffs(CallbackInfo callbackInfo) {
        if (((Object) this) instanceof SkeletonEntity) {
            SkeletonEntity skeleton = (SkeletonEntity) (Object) this;

            if (!skeleton.world.isClient) {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(skeleton.world.getServer());

                if (awakenLevelData.isWorldAwakened()) {
                    AWAKENED_MODS.apply(skeleton, true);

                    if (awakenLevelData.isFieryMoonActive()) {
                        FIERY_MOON_MODS.apply(skeleton, true);
                    }
                }
            }
        }
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void applyBoneCrownEffect1(CallbackInfo callbackInfo) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();
        targetSelector.add(0, new BoneCrownTargetGoal((AbstractSkeletonEntity) (Object) this));
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    private void applyBoneCraftEffect2(GoalSelector goalSelector, int weight, Goal goal) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();

        if (goalSelector == targetSelector) {
            // Really in-extensible way of checking if it's the player selector, but it'll work
            // TODO: make an accessor for the FollowTargetGoal
            if (goal instanceof FollowTargetGoal && weight == 2) {
                goal = new FollowTargetGoal(
                        (AbstractSkeletonEntity) (Object) this,
                        PlayerEntity.class,
                        10,
                        true,
                        false,
                        player -> !TrinketUtil.hasTrinket((LivingEntity) player, AwakenItems.Trinkets.BONE_CROWN)
                );
            } else if (goal instanceof RevengeGoal) {
                // I don't know how I feel about entirely removing skeleton friendly fire
                // TODO: investigate having the custom target selector be prioritized over the revenge one
                goal = new RevengeGoal(
                        (AbstractSkeletonEntity) (Object) this,
                        AbstractSkeletonEntity.class);
            }
        }

        goalSelector.add(weight, goal);
    }
}
