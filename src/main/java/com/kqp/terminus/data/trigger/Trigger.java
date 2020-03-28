package com.kqp.terminus.data.trigger;

import com.kqp.terminus.data.event.Event;
import com.kqp.terminus.util.JsonUtil;

/**
 * Trigger object that can be used to invoke a given event.
 */
public abstract class Trigger {
    /**
     * Tag to identify the trigger.
     */
    public final String tag;

    /**
     * Whether to remove the trigger or not.
     */
    public boolean remove = false;

    /**
     * Event JSON file name to invoke.
     */
    public final String event;

    public Trigger(String tag, String event) {
        this.tag = tag;
        this.event = event;
    }

    /**
     * Activates the trigger and invokes the event.
     */
    public void activate() {
        Event e = JsonUtil.readEvent(event);
        e.invoke();
        remove = true;
    }
}
