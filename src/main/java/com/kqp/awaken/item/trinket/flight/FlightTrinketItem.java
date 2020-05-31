package com.kqp.awaken.item.trinket.flight;

import com.kqp.awaken.item.trinket.AwakenTrinketItem;

/**
 * Flight trinket item.
 */
public abstract class FlightTrinketItem extends AwakenTrinketItem {
    public final double maxFlySpeed;
    public final double flySpeed;
    public final int flyTime;
    public final boolean canFloat;

    public FlightTrinketItem(String trinketGroup, String trinketSlot, int durability, double maxFlySpeed, double flySpeed, int flyTime, boolean canFloat) {
        super(trinketGroup, trinketSlot, durability);

        this.maxFlySpeed = maxFlySpeed;
        this.flySpeed = flySpeed;
        this.flyTime = flyTime;
        this.canFloat = canFloat;
    }

    /**
     * The max flying speed.
     *
     * @return max flying speed
     */
    public double getMaxFlySpeed() {
        return maxFlySpeed;
    }

    /**
     * Fly speed.
     * <p>
     * I believe the minimum (to fight gravity) is like ~0.075.
     *
     * @return flying acceleration
     */
    public double getFlySpeed() {
        return flySpeed;
    }

    /**
     * The max amount of time (in ticks) that a the player can fly.
     *
     * @return flying time
     */
    public int getMaxFlyTime() {
        return flyTime;
    }

    /**
     * Whether or not the flying item lets the player float in the air after exhausting their flight time.
     *
     * @return true or false
     */
    public boolean canFloat() {
        return canFloat;
    }
}
