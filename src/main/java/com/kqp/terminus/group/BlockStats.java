package com.kqp.terminus.group;

/**
 * Data class to hold block properties.
 */
public class BlockStats {
    public float hardness, resistance;
    public int lightLevel;

    public BlockStats(float hardness, float resistance, int lightLevel) {
        this.hardness = hardness;
        this.resistance = resistance;
        this.lightLevel = lightLevel;
    }
}
