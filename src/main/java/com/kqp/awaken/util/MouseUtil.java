package com.kqp.awaken.util;

import com.sun.prism.impl.BufferUtil;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

import java.nio.DoubleBuffer;

/**
 * Utility class for getting the client's mouse position.
 */
public class MouseUtil {
    public static double getMouseX() {
        DoubleBuffer mouseBuf = BufferUtil.newDoubleBuffer(1);
        GLFW.glfwGetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), mouseBuf, null);

        return mouseBuf.get(0);
    }

    public static double getMouseY() {
        DoubleBuffer mouseBuf = BufferUtil.newDoubleBuffer(1);
        GLFW.glfwGetCursorPos(MinecraftClient.getInstance().getWindow().getHandle(), null, mouseBuf);

        return mouseBuf.get(0);
    }
}
