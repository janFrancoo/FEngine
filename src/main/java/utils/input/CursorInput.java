package utils.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorInput extends GLFWCursorPosCallback {

    public static double dX = 0;
    public static double dY = 0;

    private static double prevPosX = 0, prevPosY = 0;

    @Override
    public void invoke(long window, double xPos, double yPos) {
        dX = xPos - prevPosX;
        dY = yPos - prevPosY;

        prevPosX = xPos;
        prevPosY = yPos;
    }

}
