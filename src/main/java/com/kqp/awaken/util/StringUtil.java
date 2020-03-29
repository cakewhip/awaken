package com.kqp.awaken.util;

import java.util.List;

/**
 * Utility class for manipulating strings.
 */
public class StringUtil {
    /**
     * Creates a comma separated string using 'and'.
     *
     * @param list List of strings
     * @return Comma separated string
     */
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

    /**
     * Creates a comma separated string using 'or'.
     *
     * @param list List of strings
     * @return Comma separated string
     */
    public static String commaSeparatedOr(List<String> list) {
        if (list.size() > 1) {
            String ret = "";

            for (int i = 0; i < list.size() - 1; i++) {
                ret += list.get(i) + ", ";
            }

            ret += "or " + list.get(list.size() - 1);

            return ret;
        } else if (list.size() == 1) {
            return list.get(0);
        } else if (list.size() == 2) {
            return list.get(0) + " or " + list.get(1);
        } else {
            return "None";
        }
    }
}
