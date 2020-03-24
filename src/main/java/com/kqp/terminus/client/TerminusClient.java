package com.kqp.terminus.client;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.client.container.CelestialAltarContainer;
import com.kqp.terminus.client.container.CelestialAltarResultSlot;
import com.kqp.terminus.client.screen.CelestialAltarScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.container.Container;
import net.minecraft.item.ItemStack;
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

        ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.SYNC_RESULTS_ID, (packetContext, data) -> packetContext.getTaskQueue().execute(() -> {
            if (MinecraftClient.getInstance().currentScreen instanceof CelestialAltarScreen) {
                ((CelestialAltarScreen) MinecraftClient.getInstance().currentScreen).syncScrollbar();
            }
        }));

        ClientSidePacketRegistry.INSTANCE.register(Terminus.TNetworking.SYNC_RESULT_SLOT_ID, (packetContext, data) -> {
            int slotIndex = data.readInt();
            ItemStack stack = data.readItemStack();
            int currentIndex = data.readInt();

            packetContext.getTaskQueue().execute(() -> {
                if (MinecraftClient.getInstance().currentScreen instanceof CelestialAltarScreen) {
                    ((CelestialAltarScreen) MinecraftClient.getInstance().currentScreen).getContainer().setStackInSlot(slotIndex, stack);
                    ((CelestialAltarResultSlot) ((CelestialAltarScreen) MinecraftClient.getInstance().currentScreen).getContainer().getSlot(slotIndex)).currentIndex = currentIndex;
                }
            });
        });
    }
}
