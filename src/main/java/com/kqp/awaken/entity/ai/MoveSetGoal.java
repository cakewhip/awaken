package com.kqp.awaken.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;

import java.util.ArrayList;
import java.util.List;

public class MoveSetGoal<T extends HostileEntity> extends Goal {
    public final T mob;

    private final List<Move> moveSet;
    private final int[] pattern;

    private int currentPatternIndex;
    private int coolDown;

    public Move currentMove;

    public MoveSetGoal(T mob, int[] pattern) {
        this.mob = mob;

        this.moveSet = new ArrayList();
        this.pattern = pattern;
    }

    @Override
    public void start() {
        this.currentPatternIndex = 0;
        this.selectMove(this.currentPatternIndex);
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void tick() {
        this.coolDown = Math.max(this.coolDown - 1, 0);

        if (this.coolDown <= 0) {
            // Get current move and stop it
            Move currentMove = moveSet.get(this.pattern[this.currentPatternIndex]);
            currentMove.stop(this.mob);

            // Reset current pattern index if exceeding pattern length
            this.currentPatternIndex++;
            if (this.currentPatternIndex >= this.pattern.length) {
                this.currentPatternIndex = 0;
            }

            // Get next move index from pattern
            int nextMoveIndex = this.pattern[currentPatternIndex];

            // Select next move
            this.selectMove(nextMoveIndex);
        } else {
            this.currentMove.tick(this.mob);
        }
    }

    private void selectMove(int moveSetIndex) {
        this.currentMove = moveSet.get(moveSetIndex);
        this.currentMove.start(this.mob);
        this.coolDown = this.currentMove.coolDown;
    }

    public void addMove(Move move) {
        this.moveSet.add(move);
    }
}
