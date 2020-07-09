package com.kqp.awaken.entity.ai;

import com.kqp.awaken.mixin.accessor.MeleeAttackGoalAccessor;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.MobEntityWithAi;

public class BetterMeleeAttackGoal extends MeleeAttackGoal {
    private final int tickAttackSpeed;

    public BetterMeleeAttackGoal(MobEntityWithAi mob, float attackSpeed) {
        super(mob, 1.0F, false);

        this.tickAttackSpeed = (int) ((1F / attackSpeed) * 20F);
    }

    @Override
    protected void method_28346() {
        ((MeleeAttackGoalAccessor) this).setAttackTimer(tickAttackSpeed);
    }
}
