package com.kqp.awaken.data.event;

/**
 * Event class to be invoked by a {@link com.kqp.awaken.data.trigger.Trigger}.
 */
public abstract class Event {
    /**
     * Called whenever this event is triggered.
     */
    public abstract void invoke();
}
