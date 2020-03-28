package com.kqp.terminus.data.trigger;

import net.minecraft.util.Tickable;

/**
 * Trigger that activates after a certain amount of world ticks have passed.
 */
public class TimeTrigger extends Trigger implements Tickable {
    /**
     * Countdown variable.
     */
    private int counter = 0;

    public TimeTrigger(String tag, String event, int counter) {
        super(tag, event);

        this.counter = counter;
    }

    @Override
    public void tick() {
        this.counter--;

        if (counter <= 0) {
            activate();
        }
    }
}
