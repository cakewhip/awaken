package com.kqp.terminus.client;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.kqp.terminus.client.screen.CelestialAltarScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;

public class TerminusClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.<CelestialAltarContainer>registerFactory(
                Terminus.TContainers.CELESTIAL_ALTAR_ID,
                (container) -> new CelestialAltarScreen(
                        container,
                        MinecraftClient.getInstance().player.inventory,
                        new TranslatableText(Terminus.TContainers.CELESTIAL_ALTAR_TRANSLATION_KEY)
                )
        );
    }
}
