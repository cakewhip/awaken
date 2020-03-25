package com.kqp.terminus.util;

import java.util.List;

public class StringUtil {
    public static String commaSeparated(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() == 2) {
            return list.get(0) + " and " + list.get(1);
        } else {
            String ret = "";

            for (int i = 0; i < list.size() - 1; i++) {
                ret += list.get(i) + ", ";
            }

            ret += "and " + list.get(list.size() - 1);

            return ret;
        }
    }
    public static String commaSeparatedOr(List<String> list) {
        if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() == 2) {
            return list.get(0) + " or " + list.get(1);
        } else {
            String ret = "";

            for (int i = 0; i < list.size() - 1; i++) {
                ret += list.get(i) + ", ";
            }

            ret += "or " + list.get(list.size() - 1);

            return ret;
        }
    }
}
