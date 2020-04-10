package com.kqp.awaken.data;

import com.kqp.awaken.data.trigger.SleepTrigger;
import com.kqp.awaken.data.trigger.TimeTrigger;
import com.kqp.awaken.data.trigger.Trigger;
import com.kqp.awaken.init.Awaken;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Manages how the world has progressed.
 * <p>
 * TODO: find a better way to store the progresion
 */
public class AwakenProgression {
    /**
     * Stage is determined by how many evils the player has defeated.
     *
     * @return Stage integer
     */
    public static int getStage() {
        int stage = 0;

        if (Awaken.worldProperties.isPostDragon())
            stage++;
        if (Awaken.worldProperties.isPostWither())
            stage++;
        if (Awaken.worldProperties.isPostRaid())
            stage++;
        if (Awaken.worldProperties.isPostElderGuardian())
            stage++;

        return stage;
    }

    /**
     * Evaluates what stage the world is at and what events to put in place.
     */
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
        Awaken.worldProperties.triggers.add(trigger);
    }

    public static void removeTrigger(String tag) {
        for (int i = 0; i < Awaken.worldProperties.triggers.size(); i++) {
            if (Awaken.worldProperties.triggers.get(i).tag.equals(tag)) {
                Awaken.worldProperties.triggers.remove(i);
                i--;
            }
        }
    }

    public static void removeTriggers(String... tags) {
        Set<String> tagSet = Arrays.stream(tags).collect(Collectors.toSet());

        for (int i = 0; i < Awaken.worldProperties.triggers.size(); i++) {
            if (tagSet.contains(Awaken.worldProperties.triggers.get(i).tag)) {
                Awaken.worldProperties.triggers.remove(i);
                i--;
            }
        }
    }

    public static boolean hasTrigger(String tag) {
        for (int i = 0; i < Awaken.worldProperties.triggers.size(); i++) {
            if (Awaken.worldProperties.triggers.get(i).tag.equals(tag)) {
                return true;
            }
        }

        return false;
    }
}
