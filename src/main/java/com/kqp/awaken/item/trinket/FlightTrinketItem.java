package com.kqp.awaken.item.trinket;

import com.kqp.awaken.effect.EntityFeatureGroup;
import net.minecraft.util.Identifier;

import java.util.Optional;

/**
 * Flight trinket item.
 */
public class FlightTrinketItem extends AwakenTrinketItem {
    public final double maxFlySpeed;
    public final double flySpeed;
    public final int flyTime;
    public final boolean canFloat;
    public final Optional<Identifier> flightParticleId;
    public final Optional<Identifier> floatParticleId;

    public FlightTrinketItem(String trinketGroup,
                             String trinketSlot,
                             EntityFeatureGroup efg,
                             double maxFlySpeed,
                             double flySpeed,
                             int flyTime,
                             boolean canFloat,
                             String rendererId,
                             Identifier flightParticleId,
                             Identifier floatParticleId) {
        super(trinketGroup, trinketSlot, efg, rendererId);

        this.maxFlySpeed = maxFlySpeed;
        this.flySpeed = flySpeed;
        this.flyTime = flyTime;
        this.canFloat = canFloat;
        this.flightParticleId = Optional.ofNullable(flightParticleId);
        this.floatParticleId = Optional.ofNullable(floatParticleId);
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

    public Optional<Identifier> getFlightParticleId() {
        return flightParticleId;
    }

    public Optional<Identifier> getFloatParticleId() {
        return floatParticleId;
    }
}
