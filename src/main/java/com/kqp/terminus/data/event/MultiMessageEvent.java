package com.kqp.terminus.data.event;

import com.kqp.terminus.data.message.MessageTemplater;
import com.kqp.terminus.util.Broadcaster;
import net.minecraft.util.Formatting;

public class MultiMessageEvent extends Event {
    public final Message[] messages;

    public MultiMessageEvent(Message... messages) {
        this.messages = messages;
    }

    @Override
    public void invoke() {
        Broadcaster broadcaster = new Broadcaster();
        for (Message message : messages) {
            broadcaster.broadcastMessage(MessageTemplater.templateString(message.text), message.color, message.bold, message.italic);
        }
    }

    public static class Message {
        private final String text;
        private final Formatting color;
        private final boolean bold;
        private final boolean italic;

        public Message(String text, Formatting color, boolean bold, boolean italic) {
            this.text = text;
            this.color = color;
            this.bold = bold;
            this.italic = italic;
        }
    }
}
