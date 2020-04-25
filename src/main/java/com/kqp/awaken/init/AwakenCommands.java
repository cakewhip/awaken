package com.kqp.awaken.init;

import com.kqp.awaken.data.AwakenLevelData;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.minecraft.server.command.CommandManager;

public class AwakenCommands {
    public static void init() {
        CommandRegistry.INSTANCE.register(false, dispatcher -> {
            dispatcher.register(CommandManager.literal("trigger_awakening").executes(ctx -> {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(ctx.getSource().getWorld());

                awakenLevelData.setPostDragon();
                awakenLevelData.setPostElderGuardian();
                awakenLevelData.setPostRaid();
                awakenLevelData.setPostWither();

                awakenLevelData.setAwakening();

                return 1;
            }));

            dispatcher.register(CommandManager.literal("trigger_blood_moon").executes(ctx -> {
                AwakenLevelData awakenLevelData = AwakenLevelData.getFor(ctx.getSource().getWorld());

                awakenLevelData.startBloodMoon(ctx.getSource().getMinecraftServer());

                return 1;
            }));
        });
    }
}
