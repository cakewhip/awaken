package com.kqp.terminus.data.event;

import com.kqp.terminus.Terminus;

/**
 * Event for enabling the awakening, AKA phase 1.
 */
public class AwakenEvent extends Event {
    @Override
    public void invoke() {
        Terminus.worldProperties.setAwakening();
    }
}
