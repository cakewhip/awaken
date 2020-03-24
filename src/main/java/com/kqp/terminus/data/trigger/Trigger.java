package com.kqp.terminus.data.trigger;

import com.kqp.terminus.data.event.Event;
import com.kqp.terminus.util.JsonUtil;

public abstract class Trigger {
    public final String tag;

    public boolean remove = false;
    public final String event;

    public Trigger(String tag, String event) {
        this.tag = tag;
        this.event = event;
    }

    public void trigger() {
        Event e = JsonUtil.readEvent(event);
        e.invoke();
        remove = true;
    }
}
