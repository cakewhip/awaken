package com.kqp.terminus.data.message;

import com.kqp.terminus.Terminus;
import com.kqp.terminus.util.StringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageTemplater {
    private static Map<String, MessageTemplate> TEMPLATE_MAP = new HashMap();

    static {
        addTemplate("undefeated", () -> {
            ArrayList<String> list = new ArrayList();
            if (!Terminus.worldProperties.isPostDragon())
                list.add("the End Dragon");
            if (!Terminus.worldProperties.isPostWither())
                list.add("the Wither");
            if (!Terminus.worldProperties.isPostRaid())
                list.add("a raid");
            if (!Terminus.worldProperties.isPostElderGuardian())
                list.add("an Elder Guardian");

            return StringUtil.commaSeparated(list);
        });

        addTemplate("sucker", () -> Terminus.server.getPlayerManager().getPlayerList().size() == 1 ? "Sucker" : "Suckers");
    }

    private static void addTemplate(String key, MessageTemplate template) {
        TEMPLATE_MAP.put(key, template);
    }

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
