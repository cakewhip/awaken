package com.kqp.awaken.client;

import com.kqp.awaken.world.data.AwakenLevelData;
import com.kqp.awaken.world.data.AwakenLevelDataContainer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class AwakenClientLevelData implements AwakenLevelDataContainer {
    public static final AwakenClientLevelData INSTANCE = new AwakenClientLevelData();

    private AwakenLevelData awakenLevelData;

    @Override
    public AwakenLevelData getAwakenLevelData() {
        return this.awakenLevelData;
    }

    @Override
    public void setAwakenServerLevelData(AwakenLevelData awakenLevelData) {
        this.awakenLevelData = awakenLevelData;
    }
}
