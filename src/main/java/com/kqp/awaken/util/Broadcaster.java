package com.kqp.awaken.util;

import com.kqp.awaken.init.Awaken;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

/**
 * Utility class for broadcasting messages to each player's chat.
 */
public class Broadcaster {
    public static void broadcastMessage(String message, Formatting color, boolean bold, boolean italic) {
        LiteralText text = new LiteralText(message);

        Style style = new Style();
        style.setColor(color);
        style.setBold(bold);
        style.setItalic(italic);
        text.setStyle(style);

        Awaken.server.getPlayerManager().getPlayerList().forEach(player -> {
            player.sendChatMessage(text, MessageType.CHAT);
        });
    }
}