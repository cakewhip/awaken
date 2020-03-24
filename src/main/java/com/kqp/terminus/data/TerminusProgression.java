package com.kqp.terminus.data;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.data.trigger.SleepTrigger;
import com.kqp.terminus.data.trigger.TimeTrigger;
import com.kqp.terminus.data.trigger.Trigger;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class TerminusProgression {
    public static int getStage() {
        int stage = 0;

        if (Terminus.worldProperties.isPostDragon())
            stage++;
        if (Terminus.worldProperties.isPostWither())
            stage++;
        if (Terminus.worldProperties.isPostRaid())
            stage++;
        if (Terminus.worldProperties.isPostElderGuardian())
            stage++;

        return stage;
    }

    public static void evaluateStage() {
        int stage = getStage();

        switch (stage) {
            case 1:
                addTrigger(new SleepTrigger("1", "1_evil_defeated"));
                break;
            case 2:
                if (!hasTrigger("1")) {
                    addTrigger(new SleepTrigger("2", "2_evil_defeated"));
                }
                break;
            case 3:
                if (!hasTrigger("1") && !hasTrigger("2")) {
                    addTrigger(new SleepTrigger("3", "3_evil_defeated"));
                }
                break;
            case 4:
                removeTriggers("1", "2", "3");

                addTrigger(new TimeTrigger("all_defeated", "4_evil_defeated", 5 * 20));
                addTrigger(new SleepTrigger("start_awakening", "start_awakening"));
                addTrigger(new SleepTrigger("start_awakening", "start_awakening_message"));
                break;
            default:
                break;
        }
    }

    public static void addTrigger(Trigger trigger) {
        Terminus.worldProperties.triggers.add(trigger);
    }

    public static void removeTrigger(String tag) {
        for (int i = 0; i < Terminus.worldProperties.triggers.size(); i++) {
            if (Terminus.worldProperties.triggers.get(i).tag.equals(tag)) {
                Terminus.worldProperties.triggers.remove(i);
                i--;
            }
        }
    }

    public static void removeTriggers(String... tags) {
        Set<String> tagSet = Arrays.stream(tags).collect(Collectors.toSet());

        for (int i = 0; i < Terminus.worldProperties.triggers.size(); i++) {
            if (tagSet.contains(Terminus.worldProperties.triggers.get(i).tag)) {
                Terminus.worldProperties.triggers.remove(i);
                i--;
            }
        }
    }

    public static boolean hasTrigger(String tag) {
        for (int i = 0; i < Terminus.worldProperties.triggers.size(); i++) {
            if (Terminus.worldProperties.triggers.get(i).tag.equals(tag)) {
                return true;
            }
        }

        return false;
    }
}
