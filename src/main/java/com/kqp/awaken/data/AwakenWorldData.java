package com.kqp.awaken.data;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.Broadcaster;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * Data class used to hold world data related to Awaken.
 * <p>
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

    public AwakenWorldData(CompoundTag tag) {
        postDragon = tag.getBoolean("PostDragon");
        postWither = tag.getBoolean("PostWither");
        postRaid = tag.getBoolean("PostRaid");
        postElderGuardian = tag.getBoolean("PostElderGuardian");
        worldAwakened = tag.getBoolean("WorldAwakened");
        bloodMoonActive = tag.getBoolean("BloodMoonActive");
        bloodMoonTickTime = tag.getLong("BloodMoonTickTime");
    }

    /**
     * Called on every tick using {@link net.fabricmc.fabric.api.event.world.WorldTickCallback}.
     */
    public void tick() {
        tickBloodMoon();
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

    public boolean isPostDragon() {
        return postDragon;
    }

    public void setPostDragon() {
        this.postDragon = true;
    }

    public boolean isPostWither() {
        return postWither;
    }

    public void setPostWither() {
        this.postWither = true;
    }

    public boolean isPostRaid() {
        return postRaid;
    }

    public void setPostRaid() {
        this.postRaid = true;
    }

    public boolean isPostElderGuardian() {
        return postElderGuardian;
    }

    public void setPostElderGuardian() {
        this.postElderGuardian = true;
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

    public void writeToTag(CompoundTag tag) {
        tag.putBoolean("PostDragon", postDragon);
        tag.putBoolean("PostWither", postWither);
        tag.putBoolean("PostRaid", postRaid);
        tag.putBoolean("PostElderGuardian", postElderGuardian);
        tag.putBoolean("WorldAwakened", worldAwakened);
        tag.putBoolean("BloodMoonActive", bloodMoonActive);
        tag.putLong("BloodMoonTickTime", bloodMoonTickTime);
    }
}
