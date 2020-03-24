package com.kqp.terminus.data.trigger;

import net.minecraft.util.Tickable;

public class TimeTrigger extends Trigger implements Tickable {
    private int counter = 0;

    public TimeTrigger(String tag, String event, int counter) {
        super(tag, event);

        this.counter = counter;
    }

    @Override
    public void tick() {
        this.counter--;

        if (counter <= 0) {
            trigger();
        }
    }
}
