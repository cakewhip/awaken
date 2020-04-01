package com.kqp.awaken.data;

import com.kqp.awaken.Awaken;
import com.kqp.awaken.data.trigger.Trigger;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.util.Formatting;
import net.minecraft.util.Tickable;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class used to hold world data related to Awaken.
 *
 * TODO: redo events so that triggers live inside the event
 */
public class AwakenWorldData {
    public static final String FILE_NAME = "awaken.json";

    /**
     * If the ender dragon has been killed.
     */
    private boolean postDragon = false;

    /**
     * If the wither has been killed.
     */
    private boolean postWither = false;

    /**
     * If a raid has been defeated.
     */
    private boolean postRaid = false;

    /**
     * If an elder guardian has been killed.
     */
    private boolean postElderGuardian = false;

    /**
     * If the world has entered the awakening phase.
     */
    private boolean worldAwakened = false;

    /**
     * Whether the blood moon is active or not.
     */
    private boolean bloodMoonActive = false;

    /**
     * How long the blood moon has been active.
     */
    private long bloodMoonTickTime = 0;

    /**
     * List of active triggers in the world.
     * See {@link Trigger} for more info.
     */
    public List<Trigger> triggers = new ArrayList();

    /**
     * Whether the world properties have been changed.
     * Used to trigger stage evaluations (see {@link AwakenProgression#evaluateStage()}).
     */
    private boolean dirty = false;

    /**
     * Called on every tick using {@link net.fabricmc.fabric.api.event.world.WorldTickCallback}.
     */
    public void tick() {
        for (int i = 0; i < triggers.size(); i++) {
            Trigger trigger = triggers.get(i);

            if (trigger instanceof Tickable) {
                ((Tickable) trigger).tick();
            }

            if (trigger.remove) {
                triggers.remove(i);
                i--;
            }
        }

        tickBloodMoon();

        if (dirty) {
            AwakenProgression.evaluateStage();
            dirty = false;
        }
    }

    /**
     * Updates stuff for blood moon handling.
     */
    public void tickBloodMoon() {
        World world = Awaken.server.getWorld(DimensionType.OVERWORLD);
        long time = world.getTimeOfDay() % 24000;

        if (bloodMoonActive) {
            if (time < AwakenConfig.NIGHT_START || time >= AwakenConfig.NIGHT_END) {
                // Not night and blood moon active, end it

                endBloodMoon();
                return;
            }

            bloodMoonTickTime++;
        } else if (time == AwakenConfig.NIGHT_START) {
            // Moon is rising, roll blood moon chance

            if (isWorldAwakened() && world.random.nextFloat() < AwakenConfig.BLOOD_MOON_CHANCE) {
                startBloodMoon();
            }
        }
    }

    private void startBloodMoon() {
        Awaken.info("Starting blood moon");
        bloodMoonActive = true;
        bloodMoonTickTime = 0;

        Broadcaster.broadcastMessage("The blood moon rises...", Formatting.DARK_RED, true, false);
    }

    private void endBloodMoon() {
        Awaken.info("Ending blood moon");
        bloodMoonActive = false;
    }

    public void markDirty() {
        dirty = true;
    }

    public boolean isPostDragon() {
        return postDragon;
    }

    public void setPostDragon() {
        this.postDragon = true;
        markDirty();
    }

    public boolean isPostWither() {
        return postWither;
    }

    public void setPostWither() {
        this.postWither = true;
        markDirty();
    }

    public boolean isPostRaid() {
        return postRaid;
    }

    public void setPostRaid() {
        this.postRaid = true;
        markDirty();
    }

    public boolean isPostElderGuardian() {
        return postElderGuardian;
    }

    public void setPostElderGuardian() {
        this.postElderGuardian = true;
        markDirty();
    }

    public boolean isWorldAwakened() {
        return worldAwakened;
    }

    public void setAwakening() {
        this.worldAwakened = true;
    }

    public boolean isBloodMoonActive() {
        return bloodMoonActive;
    }
}
