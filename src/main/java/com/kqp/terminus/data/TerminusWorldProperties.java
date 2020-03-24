package com.kqp.terminus.data;

import com.kqp.terminus.data.trigger.Trigger;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.List;

public class TerminusWorldProperties {
    public static final String FILE_NAME = "terminus.json";

    private boolean postDragon = false;
    private boolean postWither = false;
    private boolean postRaid = false;
    private boolean postElderGuardian = false;
    private boolean worldAwakened = false;
    
    public List<Trigger> triggers = new ArrayList();

    private boolean dirty = false;
    
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
            TerminusProgression.evaluateStage();
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
