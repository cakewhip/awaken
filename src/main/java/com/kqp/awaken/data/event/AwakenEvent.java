package com.kqp.awaken.data.event;

import com.kqp.awaken.init.Awaken;

/**
 * Event for enabling the awakening, AKA phase 1.
 */
public class AwakenEvent extends Event {
    @Override
    public void invoke() {
        Awaken.worldProperties.setAwakening();
    }
}
