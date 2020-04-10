package com.kqp.awaken.data.message;

import com.kqp.awaken.init.Awaken;
import com.kqp.awaken.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Used to template event messages.
 */
public class MessageTemplater {
    /**
     * Map of things to template.
     */
    private static Map<String, MessageTemplate> TEMPLATE_MAP = new HashMap();

    /**
     * Adds some things to template.
     */
    static {
        addTemplate("undefeated", () -> {
            ArrayList<String> list = new ArrayList();
            if (!Awaken.worldProperties.isPostDragon())
                list.add("the End Dragon");
            if (!Awaken.worldProperties.isPostWither())
                list.add("the Wither");
            if (!Awaken.worldProperties.isPostRaid())
                list.add("a raid");
            if (!Awaken.worldProperties.isPostElderGuardian())
                list.add("an Elder Guardian");

            return StringUtil.commaSeparated(list);
        });

        addTemplate("sucker", () -> Awaken.server.getPlayerManager().getPlayerList().size() == 1 ? "Sucker" : "Suckers");
    }

    private static void addTemplate(String key, MessageTemplate template) {
        TEMPLATE_MAP.put(key, template);
    }

    /**
     * Templates the passed String and returns it.
     *
     * @param in String to template
     * @return String, but templated
     */
    public static String templateString(String in) {
        StringBuilder sb = new StringBuilder();

        int start = -1;

        for (int i = 0; i < in.length(); i++) {
            char c = in.charAt(i);

            if (c == '$') {
                start = i;
            } else if (c == '}') {
                String key = in.substring(start + 2, i);
                sb.append(TEMPLATE_MAP.get(key).template());

                start = -1;
            } else if (start == -1) {
                sb.append(c);
            }
        }

        return sb.toString();
    }
}
