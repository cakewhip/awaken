package com.kqp.awaken.util;

import net.minecraft.network.MessageType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

/**
 * Utility class for broadcasting messages to each player's chat.
 */
public class Broadcaster {
    public static void broadcastMessage(MinecraftServer server, String message, Formatting color, boolean bold, boolean italic) {
        LiteralText text = new LiteralText(message);

        Style style = Style.EMPTY
                .withColor(color)
                .withBold(bold)
                .withItalic(italic);

        text.setStyle(style);

        server.getPlayerManager().getPlayerList().forEach(player -> {
            player.sendMessage(text, MessageType.CHAT);
        });
    }
}