package com.kqp.awaken.entity.ai;

import net.minecraft.entity.mob.HostileEntity;

public abstract class Move<T extends HostileEntity> {
    public final int coolDown;

    public Move(int coolDown) {
        this.coolDown = coolDown;
    }

    public abstract void start(T mob);
    public abstract void tick(T mob);
    public abstract void stop(T mob);
}
