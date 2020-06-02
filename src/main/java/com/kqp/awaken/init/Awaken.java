package com.kqp.awaken.init;

import com.kqp.awaken.util.TimeUtil;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main entry point for Awaken.
 */
public class Awaken implements ModInitializer {
    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "awaken";
    public static final String MOD_NAME = "Awaken";

    @Override
    public void onInitialize() {
        info("Initializing Awaken");

        TimeUtil.profile(() -> {
            AwakenStatusEffects.init();
            AwakenEntityAttributes.init();
            AwakenAbilities.init();
            AwakenTrinkets.init();
            AwakenArmor.init();
            AwakenBlocks.init();
            AwakenItems.init();
            AwakenEntities.init();
            AwakenNetworking.init();
            AwakenLootTable.init();
            AwakenCallbacks.init();
            AwakenCommands.init();
        }, (time) -> Awaken.info("Awaken took " + time + "ms to load"));
    }

    public static void info(String message) {
        LOGGER.log(Level.INFO, "[" + MOD_NAME + "] " + message);
    }

    public static void warn(String message) {
        LOGGER.log(Level.WARN, "[" + MOD_NAME + "] " + message);
    }

    public static void error(String message) {
        LOGGER.log(Level.ERROR, "[" + MOD_NAME + "] " + message);
    }
}