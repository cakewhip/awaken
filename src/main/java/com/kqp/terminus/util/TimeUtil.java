package com.kqp.terminus.util;

import com.kqp.terminus.Terminus;
import net.minecraft.util.Util;

public class TimeUtil {
    public static void bench(Runnable runnable) {
        long start = Util.getMeasuringTimeMs();

        runnable.run();

        Terminus.info("Took " + (Util.getMeasuringTimeMs() - start) + "ms");
    }
}
