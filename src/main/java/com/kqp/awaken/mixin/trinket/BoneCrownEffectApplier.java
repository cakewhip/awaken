package com.kqp.awaken.mixin.trinket;

import com.kqp.awaken.entity.ai.BoneCrownTargetGoal;
import com.kqp.awaken.init.AwakenAbilities;
import com.kqp.awaken.init.AwakenItems;
import com.kqp.awaken.mixin.accessor.MobEntityAccessor;
import com.kqp.awaken.util.EquipmentUtil;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Used to implement the bone crown effect.
 */
@Mixin(AbstractSkeletonEntity.class)
public abstract class BoneCrownEffectApplier {
    @Inject(method = "initGoals", at = @At("TAIL"))
    private void addBoneCrownTargetGoal(CallbackInfo callbackInfo) {
        GoalSelector targetSelector = ((MobEntityAccessor) this).getTargetSelector();
        targetSelector.add(0, new BoneCrownTargetGoal((AbstractSkeletonEntity) (Object) this));
    }

    @Redirect(method = "initGoals", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ai/goal/GoalSelector;add(ILnet/minecraft/entity/ai/goal/Goal;)V"))
    private void overrideTargetGoals(GoalSelector goalSelector, int weight, Goal goal) {
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
                        player -> !AwakenAbilities.BONE_CROWN_EFFECT.get((PlayerEntity) player).flag
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
