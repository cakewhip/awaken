package com.kqp.awaken.item.trinket;

public interface FlyingItem {
    /**
     * The max flying speed.
     *
     * @return max flying speed
     */
    double getMaxFlySpeed();

    /**
     * Fly speed.
     * <p>
     * I believe the minimum (to fight gravity) is like ~0.075.
     *
     * @return flying acceleration
     */
    double getFlySpeed();

    /**
     * The max amount of time (in ticks) that a the player can fly.
     *
     * @return flying time
     */
    int getMaxFlyTime();
}
