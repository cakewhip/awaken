package com.kqp.awaken.data;

import com.kqp.awaken.data.trigger.Trigger;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

/**
 * Data class used to hold world data related to Awaken.
 *
 * TODO: redo events so that triggers live inside the event
 */
public class AwakenWorldProperties {
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

        if (dirty) {
            AwakenProgression.evaluateStage();
            dirty = false;
        }
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
}
