package com.kqp.terminus.data.event;

import com.kqp.terminus.Terminus;

public class AwakenEvent extends Event {
    @Override
    public void invoke() {
        Terminus.worldProperties.setAwakening();
    }
}
