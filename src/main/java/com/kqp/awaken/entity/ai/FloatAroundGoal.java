package com.kqp.awaken.entity.ai;

import net.minecraft.block.Blocks;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.MobEntityWithAi;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.Random;

public class FloatAroundGoal extends Goal {
    protected final MobEntityWithAi mob;
    protected int chance;
    protected boolean ignoringChance;

    public BlockPos target;

    public FloatAroundGoal(MobEntityWithAi mob) {
        this.mob = mob;
        this.chance = 120;
        this.setControls(EnumSet.of(Goal.Control.MOVE));
    }

    public boolean canStart() {
        if (this.mob.hasPassengers()) {
            return false;
        } else {
            if (!this.ignoringChance) {
                if (this.mob.getDespawnCounter() >= 100) {
                    return false;
                }

                if (this.mob.getRandom().nextInt(this.chance) != 0) {
                    return false;
                }
            }

            BlockPos blockPos = getTargetPosition();

            if (blockPos == null) {
                return false;
            } else {
                this.target = blockPos;
                this.ignoringChance = true;

                return true;
            }
        }
    }

    private BlockPos getTargetPosition() {
        World world = this.mob.world;
        Random r = world.random;

        for (int i = 0; i < 16; i++) {
            BlockPos rPos = this.mob.getBlockPos().add(
                    r.nextInt(64) - 32,
                    r.nextInt(64) - 32,
                    r.nextInt(64) - 32
            );

            if (world.getBlockState(rPos).getBlock().is(Blocks.AIR) && world.getBlockState(rPos.add(0, 1, 0)).getBlock().is(Blocks.AIR)) {
                return rPos;
            }
        }

        return null;
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle() && !this.mob.hasPassengers();
    }

    @Override
    public void stop() {
        super.stop();
        this.target = null;
    }
}