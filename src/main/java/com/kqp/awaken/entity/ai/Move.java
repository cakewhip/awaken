package com.kqp.awaken.entity.ai;

import net.minecraft.entity.mob.HostileEntity;

public abstract class Move<T extends HostileEntity> {
    public final int duration;

    public Move(int duration) {
        this.duration = duration;
    }

    public abstract void start(T mob);

    public abstract void tick(T mob);

    public abstract void stop(T mob);
}
