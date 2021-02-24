package utils.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollInput extends GLFWScrollCallback {

    public static double dWheel = 0;

    @Override
    public void invoke(long window, double xOffset, double yOffset) {
        dWheel = yOffset;
    }

}
