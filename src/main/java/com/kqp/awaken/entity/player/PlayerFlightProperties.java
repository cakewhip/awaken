package com.kqp.awaken.entity.player;

import com.kqp.awaken.item.trinket.FlyingItem;

/**
 * Various info on a player's flying abilities.
 */
public interface PlayerFlightProperties {
    void setSecondSpacing(boolean secondSpacing);

    boolean isSecondSpacing();

    void setFlying(boolean flying);

    boolean isFlying();

    FlyingItem getFlyingItem();

    boolean canFly();

    int getFlyTime();

    void setFlyTime(int flyTime);

    boolean canFloat();

    boolean isFloating();

    void setFloating(boolean floating);
}
