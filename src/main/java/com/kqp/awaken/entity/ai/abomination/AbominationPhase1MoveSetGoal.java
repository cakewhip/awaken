package com.kqp.awaken.entity.ai.abomination;

import com.kqp.awaken.entity.ai.MoveSetGoal;
import com.kqp.awaken.entity.mob.AbominationEntity;

public class AbominationPhase1MoveSetGoal extends MoveSetGoal<AbominationEntity> {
    public AbominationPhase1MoveSetGoal(AbominationEntity mob) {
        super(mob, new int[] { 0, 1, 1, 1, 1 });

        this.addMove(new SpawnSpawnlingsMove(4 * 20));
        this.addMove(new SmashAttackMove(5 * 20));
    }

    @Override
    public boolean canStart() {
        return this.mob.aboveHalfHealth();
    }

    @Override
    public boolean shouldContinue() {
        return this.mob.aboveHalfHealth();
    }


}
