package com.kqp.terminus.util;

import net.minecraft.util.Util;

public class TimeUtil {
    public static void profile(Runnable runnable, ProfileCallback callback) {
        long start = Util.getMeasuringTimeMs();

        runnable.run();

        callback.print(Util.getMeasuringTimeMs() - start);
    }
}
